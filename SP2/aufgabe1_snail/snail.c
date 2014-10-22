#include <netdb.h>
#include <pwd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <unistd.h>
#define BUFSIZE 1024
#define MAX_HOSTNAME sysconf(_SC_HOST_NAME_MAX)
#define MAX_LINE sysconf(_SC_LINE_MAX)

/*
static int myStatusCode (char *textFromSocket, char *expectedStatusCode, FILE *mySocketFile) {
	char container[4];
	container[3] = '\0';
	strncpy(container, textFromSocket, 3);
	if(strcmp(container, expectedStatusCode) != 0) {
		fprintf(stdout, "%s\n", textFromSocket);
		fclose(mySocketFile);
		exit(EXIT_FAILURE);
	}
	return(atoi(container));
}

static char *myTextFromSocket(FILE *mySocketFile, char *response) {
	int charCounter = 0;
	int currentLetter;
	int currentLetterDelay = 0;
	
	while (1) {
		currentLetter = fgetc(mySocketFile);
		charCounter++;
		if((currentLetter == EOF) && (!feof(mySocketFile))) {
			fprintf(stderr, "Felher beim Lesen vom Stream!\n");
			fclose(mySocketFile);
			exit(EXIT_FAILURE);
		}
		// Satzende <CR><LF>
		if((currentLetter == '\r') && (currentLetter == '\n')) {
			break;
		}
		response[charCounter-1] = currentLetter;
		currentLetterDelay = currentLetter;
	}
	response[charCounter-2] = '\0';
	return(response);
}
*/

int main(int argc, char *argv[]) {
	/* -------------------- Akteure, in Reihenfolge ----------------------- */
	int ok = 0;						// Zum Überprüfen aller Return-Werte
	int hasSubject = 0;				// falls Betreff vorhanden = 1
	char subject[256];				// Betreffszeile
	char emailAddress[256];			// Zieladresse für DNS-Anfrage
	
	uid_t myUserID;
	struct passwd *myPID;
	char myFullName[256];
	char myUserName[256];
	char myHostName[MAX_HOSTNAME];
	char myFQDN[256];
	
	int mySocket;					// Socket
	struct addrinfo hints;			// DNS-"Suchvorgabe"
	struct addrinfo *result, *rp;	// DNS-Ergebnis
	struct sockaddr_in address;		// Adresse zum Connecten
	
	FILE *mySocket_readStream;		// Lese-Zeiger
	FILE *mySocket_writeStream;		// Schreib-Zeiger
	char buf[BUFSIZE];				// Lese/Schreib-Buffer
	char *textFromSocket;			// TMP
	int n;							// Anzahl geschriebener Zeichen
	
	
	/* -------------- Einlesen von der Kommandozeile ------------------- */
	if ((argc < 2) || (argc == 3) || (argc > 4)) {
		printf("Bad usage. Try again: snail [-s <subject>] <address>\n");
	}
	
	if (argc == 4) {
		if (strcmp(argv[1], "-s") != 0) {
			printf("Bad usage 2. Try again: snail [-s <subject>] <address>\n");
		}
		hasSubject = 1;
		strcpy(subject, argv[2]);	// Setze Betreff
		printf("Mit Betreff: %s\n", subject);
		strcpy(emailAddress, argv[3]); // Setze E-Mail Adresse (Fall mit Betreff)
		printf("Mit Ziel E-Mail-Adresse: %s\n", emailAddress);
	}
	strcpy(emailAddress, argv[1]);	// Setze E-Mail Adresse (Fall ohne Betreff)
	
	
	/* ---------- Klarheit über eigene Situation schaffen ------------ */
	myUserID = getuid();						// Eigene UserID,
	printf("myUserID: %d\n", myUserID);		// Grundlage für struct passwd
	myPID = getpwuid(myUserID);					// stuct passwd auslesen
	strcpy(myFullName, myPID->pw_gecos);		// passwd: Voller Name
	printf("myFullName: %s\n", myFullName);
	strcpy(myUserName, myPID->pw_name);		// passwd: Benutzername
	printf("myUserName: %s\n", myUserName);	
	
	myHostName[MAX_HOSTNAME-1] = '\0';
	ok = gethostname(myHostName, sizeof(myHostName)); // Hostname
	if (ok != 0) {
		fprintf(stderr, "Hostname nicht ermittelbar!\n");
		exit(EXIT_FAILURE);
	}
	printf("myHostName: %s\n", myHostName);
	
	
	/* ---- Vorbereiten der Hints: für Client-Name & Servername relevant ------- */
	memset(&hints, 0, sizeof(struct addrinfo)); // Init. Clearen
	hints.ai_family = AF_UNSPEC;		// IPv4 oder IPv6
	hints.ai_socktype = SOCK_STREAM; 	// TCP-Sockets
	hints.ai_flags = AI_CANONNAME;		// beliebiger Kanonischer Name
	hints.ai_protocol = 0;				// Any protocol
	
	
	/* -------------------- DNS-Anfrage eigener Hostname ------------------- */
	ok = getaddrinfo(myHostName, "17434", &hints, &result); // hier: Statisch
	if (ok != 0) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(ok));
		exit(EXIT_FAILURE);
	}
	strcpy(myFQDN, result->ai_canonname);	// FQDN auslesen
	freeaddrinfo(result);	// No longer needed!
	printf("myFQDN: %s\n", myFQDN);
	
	memset(&hints, 0, sizeof(struct addrinfo)); // Init. Clearen
	hints.ai_family = AF_UNSPEC;		// IPv4 oder IPv6
	hints.ai_socktype = SOCK_DGRAM; 	// UDP-Sockets
	hints.ai_flags = AI_CANONNAME;		// beliebiger Kanonischer Name
	hints.ai_protocol = 0;				// Any protocol
	
	
	/* ----- DNS Anfrage, Socket anlegen, Verbindung herstellen ------------- */
	ok = getaddrinfo("localhost", "11123", &hints, &result);
	if (ok != 0) {
		fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(ok));
		exit(EXIT_FAILURE);
	}
	for (rp = result; rp->ai_next != NULL; rp = rp->ai_next) {
		// Socket anlegen:
		mySocket = socket(rp->ai_family, rp->ai_socktype, rp->ai_protocol);
		if (mySocket == -1) { continue; } // falls fehlgeschlagen, neuer Versuch
		// Socket connecten:
		if (connect(mySocket, rp->ai_addr, rp->ai_addrlen) == 0) {
			break;	// Schleife beenden, connect erfolgreich
		}
		close(mySocket);	// Ansonsten Socket schließen
	}
	if(rp == NULL) {
		fprintf(stderr, "No address succeeded. Connection not possible!\n");
		exit(EXIT_FAILURE);
	}
	freeaddrinfo(result);	// No longer needed!
	
	fprintf(stdout, "======= Schon mal bis hier!\n");
	
	/* ------------------ Anlegen Lese-/Schreibzeiger ---------------- */
	mySocket_readStream = fdopen(mySocket, "r"); // Anlegen Lese-Zeiger
	if (mySocket_readStream == NULL) {
		fprintf(stderr, "Fehler beim Anlegen des Lese-Zeigers!\n");
		exit(EXIT_FAILURE);
	}
	
	mySocket_writeStream = fdopen(dup(mySocket), "w"); // Anlegen Schreib-Zeiger
	if (mySocket_writeStream == NULL) {
		fprintf(stderr, "Fehler beim Anlegen des Schreib-Zeigers!\n");
		exit(EXIT_FAILURE);
	}
	
	
	/* ---------------------- Kommunikation ---------------------- */
	fgets(buf, 5, mySocket_readStream);
	fputc(buf, fgetc(mySocket_readStream));
	fprintf(stdout, "Dies: %s\n", buf);
	
	fprintf(stdout, "========= Schon mal bis hier2!\n");
	
	fclose(mySocket_readStream);
	fclose(mySocket_writeStream);
	close(mySocket); // eigentlich nicht nötig
}