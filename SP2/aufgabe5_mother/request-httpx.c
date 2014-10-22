#include <stdio.h>
#include "i4httools.h"
#include <stdlib.h>
#include <limits.h>
#include <string.h>
#include "getargs.h"
#include <errno.h>
// fuer Aufgabe c
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <dirent.h>

extern const char *pathArgument;

/**
 * @brief  Initializes the request-handling module.
 * @note   This function must be invoked after cmdlineInit().
 * @return 0 on success, -1 if the command-line arguments are invalid. If a
 *         non-recoverable error occurs during initialization (e.g. a failed
 *         memory allocation), the function does not return, but instead prints
 *         a meaningful error message and terminates the process.
 */
int initRequestHandler(void) {
	//
	return 0;
}

/**
 * @brief Handles requests coming from a client.
 *
 * This function does the actual work of communicating with the client. It
 * should be called from the connection-handling module.
 *
 * @param fromClient Client-connection stream opened for reading. It is the
 * caller's responsibility to close it after this function has returned.
 * @param toClient Client-connection stream opened for writing. It is the
 * caller's responsibility to close it after this function has returned.
 */
void handleRequest(FILE *fromClient, FILE *toClient) {
	// Kommunikaton mit Client, eigentliche Arbeit
	// Auslesen Socket
	char puffer[PATH_MAX+13];
	char relPathArgument[PATH_MAX+13];
	char fullPath[PATH_MAX+13];
	
	// Angeforderte Datei aus Socket auslesen
	if (fgets(puffer, PATH_MAX+13, fromClient) == NULL) {
		if(ferror(socket)) {
			perror("Auslesen des Sockets fehlgeschlagen");
			exit(EXIT_FAILURE);
		}
	}
	
	// printf("Aus Socket gelesene Zeile: %s", puffer);  // GET /index.html HTTP/1.1
	char *file;
	char *token = strtok(puffer, " ");
	int i = 0;
	while (token != NULL) {
		i++;
		if (i == 1 && (strncmp(token, "GET", 3) != 0)) {
			badRequest(toClient, (const char *) puffer);
			return -1;
		}
		if (i == 2) {
			file = token;
		}
		token = strtok(NULL, " ");
	}
	
	strcpy(relPathArgument, pathArgument);
	strcat(relPathArgument, file);
	strcpy(fullPath, relPathArgument);
	if(checkPath(relPathArgument) == -1) {
		httpForbidden(toClient, (const char *) fullPath);
		fprintf(stderr, "Pfad befindet sich jenseits des www-Directory");
		exit(EXIT_FAILURE);
	}
	
	int folder = -1;
	struct stat check_path;
	if (lstat(fullPath, &check_path) == -1) {
		perror("lstat");
		return -1;
	}
	if(S_ISREG(check_path.st_mode)) {
		folder = 0;
	} else if (S_ISDIR(check_path.st_mode)) {
		folder = 1;
	}
	
	printStatusLine(toClient, HTTP_OK);
	
	if(folder == 1) {
		if(file[strlen(file)-1] = '/') {
			strncat(file, "/", strlen(file));
			httpdMovedPermanently(toClient, fullPath);
			// Verbindung wird getrennt
		}
		
		// index.html suchen
		if(strcmp("/index.html", file) == 0) {
			// Datei oeffnen
			FILE *fp;
			if((fp = fopen((const char *) fullPath, "r")) == NULL) {
				httpNotFound(toClient, fullPath);
				perror("Konnte Datei nicht finden, oder darauf zugriefen");
				return -1;
			}
			
			int aktZeichen;
			errno = 0;
			while ((aktZeichen = fgetc(fp)) != EOF) {
				if (errno != 0) {
					httpInternalServerError(toClient, fullPath);
					perror("Fehler beim Einlesen der Datei");
					return -1;
				}
			}
			
			if (fclose(fp) == -1) {
				httpInternalServerError(toClient, fullPath);
				perror("Fehler beim Schliessen der Datei");
				return -1;
			}
		// wenn kein "/index.html" vorhanden...
		} else {
			printDirHeader(toClient, fullPath); // Ueberschrift
			// CRAWLing
			DIR *webfolder = opendir(fullPath);
			if (webfolder == NULL) {
				return -1;
			}
			
			struct dirent *entry;
			while ((errno = 0 && (entry = readdir(webfolder))) != NULL) {
				if (entry->d_name[0] != '.') {
					printDirEntry(toClient, fullPath, entry->d_name);
				}
			}
			if (errno != 0) {
				perror("Fehler bei readdir");
				return -1;
			}
			
			if (closedir(webfolder) != 0) {
				perror("Fehler bei closedir");
				return -1;
			}
		}
	} else {	// kein Folder, sondern eine normale Datei
		FILE *fp;
		if (fp = fopen((const char*) fullPath, "r") == NULL) {
			httpNotFound(toClient, (const char*) fullPath);
			perror("Konnte Datei nicht finden, oder darauf zugreifen");
			return -1;
		}
		
		int aktZeichen;
		errno = 0;
		while ((aktZeichen = fgetc(fp)) != EOF) {
			if (errno != 0) {
				httpInternalServerError(toClient, (const char *) fullPath);
				perror("Fehler beim Einlesen der Datei");
				return -1;
			}
			fprintf(toClient, "%c", (char) aktZeichen);
		}
		
		if (fclose(fp) == -1) {
			httpInternalServerError(toClient, (const char *) fullPath);
			perror("Fehler beim Schliessen der Datei");
			return -1;
		}
	}
	return 0;
}

/**
 * @brief Handles internal server errors.
 *
 * This function handles internal server errors which occur before the
 * handleRequest() function can be called. It should be called from the
 * connection-handling module.
 *
 * @param client The filedescriptor for the client connection.
 */
void handleInternalError(int client) {
	// my Internal Error
}