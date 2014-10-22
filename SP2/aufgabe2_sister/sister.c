#include <sys/types.h>
#include <sys/socket.h>
#include <errno.h>
#include <stdio.h>
#include <unistd.h>
#include <netdb.h>
#include <stdlib.h>
#include "cmdline.h"
#include "connection.h"
#include "request.h"
#include <string.h>

int main(int argc, char *argv[]) {
	// "Darsteller":
	int myPort;					// Variable für Port
	const char *myPortCheck;	// Wurde Port übergeben?
	int listenSocket = 0;		// Socket zum "lauschen"
	int clientSocket = 0;		// Socket für eigentliche Verbindung
	
	// Initialisierung cmdline-Modul
	if (cmdlineInit(argc, argv) != 0) {
		perror("cmdlineInit");
		exit(EXIT_FAILURE);
	}
	
	// Initialisierung Connection Handler aus "connection-fork.c"
	if (initConnectionHandler() == -1) {
		fprintf(stderr, "Connection handler init failed, because: %s\n", strerror(errno));
		exit(EXIT_FAILURE);
	}
	
	// Wurde Port uebergeben?
	myPortCheck = cmdlineGetValueForKey("port");
	if (myPortCheck == NULL) {
		myPort = 2012;		// setze auf Standardport
	} else {
		myPort = atoi(myPortCheck);		// char in integer umwandeln
		if (myPort > 65535) {
			fprintf(stderr, "Sorry, %d ist kein gueltiger Port (x <= 65535!\n", myPort);
			exit(EXIT_FAILURE);
		}
	}
	
	// Socket anlegen
	listenSocket = socket(AF_INET6, SOCK_STREAM, 0);
	if(listenSocket == -1) {
		perror("Socket-Erstellung fehlgeschlagen");
		exit(EXIT_FAILURE);
	}
	
	struct sockaddr_in6 name = {
		.sin6_family = AF_INET6,
		.sin6_port = htons(myPort),
		.sin6_addr = in6addr_any
	};
	
	// Sofortige Verwendung von Port forcieren:
	int flag = 1;		// Hä?
	
	// Socket-Optionen setzen
	if (setsockopt(listenSocket, SOL_SOCKET, SO_REUSEADDR, &flag, sizeof(flag)) == -1) {
		perror("setsockopt() fehlgeschlagen");
		exit(EXIT_FAILURE);
	}
	
	// Socket binden
	if (bind(listenSocket, (struct sockaddr *) &name, sizeof(name)) == -1) {
		perror("bind() fehlgeschlagen");
		exit(EXIT_FAILURE);
	}
	
	// Am Socket "lauschen"
	if (listen(listenSocket, SOMAXCONN) == -1) {
		perror("listen() fehlgeschlagen");
		exit(EXIT_FAILURE);
	}
	
	// Verbindung zu ClientSock akzeptieren und handleConnection() aufrufen!
	while ((clientSocket == accept(listenSocket, NULL, NULL)) != -1) {
		handleConnection(clientSocket, listenSocket); // Aufruf aus "connection-fork.c"
	}
	if(close(listenSocket) == -1) {
		perror("close() fehlgeschlagen");
		exit(EXIT_FAILURE);
	}
	return(EXIT_SUCCESS);
}
