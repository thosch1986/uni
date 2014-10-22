#include <stdio.h>
#include <string.h>
#include <limits.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <stdlib.h>
#include <errno.h>

#include "cmdline.h"
#include "i4httools.h"
#include "request.h"



const char *myRootPath;

int initRequestHandler(void) {
	struct stat myStat1;
	
	if (cmdlineGetExtraArgCount() >= 1) {
		fprintf(stderr, "Too many arguments. Usage: ./sister --wwwpath=<dir> [--port=<p>]\n");
		return -1;
	}
	
	if ((myRootPath = cmdlineGetValueForKey("wwwpath")) == NULL) {
		fprintf(stderr, "No root path. Usage: ./sister --wwwpath=<dir> [--port=<p>]\n");
		return -1;
	}
	
	if (lstat(myRootPath, &myStat1) == -1) {  // Schreibe myRootPath in myStat1
		perror("lstat");
		return -1;
	}
	
	return 0;
}

void handleRequest(FILE *client) {
	// http-Anfragezeile vom Socket lesen
	// Interpretation relativ zur Kommandozeile
	
	// Speicherblocks
	char myBuffer[sysconf(_SC_LINE_MAX)];
	char *myHttpCommand = NULL;
	char *myRelPath = NULL;
	char *myFullPath = NULL;
	struct stat myStat2;
	FILE *realFile;
	int c;						// fuer fgetc
	
	while (fgets(myBuffer, LINE_MAX - 1, client) != NULL && !(feof(client))) {
		// Kommando und Pfad trennen
		myHttpCommand = strtok(myBuffer, " ");
		myRelPath = strtok(NULL, " ");
		
		// Kommando ueberpruefen
		if (strcmp(myHttpCommand, "GET") != 0) {
			httpBadRequest(client, myBuffer);
			break;
		}
		
		// Full-Path zusammensetzen
		myFullPath = (char *) malloc(strlen(myRootPath) + strlen(myRelPath) + 1);
		if (myFullPath == NULL) {
			perror("malloc failed");
		}
		
		strncpy(myFullPath, myRootPath, strlen(myRootPath) + 1);
		myFullPath[strlen(myRootPath)] = '\0';
		
		strncat(myFullPath, myRelPath, strlen(myRelPath) + 1);
		myFullPath[strlen(myRelPath) + strlen(myRootPath)] = '\0';
		
		// Pfad ueberpruefen
		if (checkPath(myRelPath) == -1) {
			// falls root-Path verlassen wird
			httpForbidden(client, myRelPath);
			free(myFullPath);
			break;
		}
		
		// Status ueberpruefen
		if (lstat(myFullPath, &myStat2) == -1) {
			perror("lstat");
			break;
		}
		
		if (S_ISREG(myStat2.st_mode)) {
			// wenn reguläre Datei mit Gruppenleserechten
			realFile = fopen(myFullPath, "r");
			if (realFile == NULL) {
				perror("fopen");
				switch (errno) {
					case EPERM:
					case EACCES:
						httpForbidden(client, myRelPath);
						break;
					default:
						httpInternalServerError(client, myRelPath);
						break;
				}
			}
			
			httpOK(client);
			
			// Client-Kommunikation
			while ((c = fgetc(realFile)) != EOF) {
				fputc(c, client);
			}
			
			if (fclose(realFile) == EOF) {
				perror("fclose");
				exit(EXIT_FAILURE);
			}
		} else {
			httpForbidden(client, myRelPath);
			break;
		}
	}
	
	free(myFullPath);
}
