#include <stdio.h>
#include <unistd.h>
#include <sys/types.h>
#include <signal.h>
#include <stdlib.h>

#include "request.h"

int initConnectionHandler(void) {
	
	struct sigaction mySignal;
	mySignal.sa_handler = SIG_DFL;	// SIG_DEFAULT: Standardsignalbehandlung
	if (sigemptyset(&mySignal.sa_mask) == -1) {  // Signals auf 0 setzen
		perror("sigemptyset()");
		return -1;
	}
	mySignal.sa_flags = SA_NOCLDWAIT | SA_RESTART;
	if (sigaction(SIGCHLD, &mySignal, NULL) == -1) {
		perror("sigaction()");
		return -1;
	}
	
	if (initRequestHandler() == -1) {
		perror("initRequestHandler()");
		return -1;
	}
	
	// ConnectionHandler erfolgreich und ohne Fehler initialisiert!
	return 0;
}

void handleConnection(int clientSocket, int listenSocket) {
	pid_t pid;
	FILE *fp;
	
	pid = fork();		// Fork
	if(pid == 0) {
		// ----- Im Kind -----
		if (close(listenSocket) == -1) {
			perror("close() fehlgeschlagen");
			fprintf(stderr, "Schliessen von listenSocket fehlgeschlagen, trotzdem Programm-Fortfuehrung\n");
		}
		
		fp = fdopen(clientSocket, "a+");	// Socket "aufmachen"
		if (fp == NULL) {
			perror("fdopen");
			exit(EXIT_FAILURE);		// Beendet nur Kind-Prozess
		}
		
		handleRequest(fp);		// <<<<<<< EINTAUCHEN IN handleRequest() "request-httpd.c"
		
		if (fclose(fp) == EOF) {
			perror("fclose() fehlgeschlagen");
			exit(EXIT_FAILURE);
		}
		
		exit(EXIT_SUCCESS);		// Kindprozess korrekt beenden!
		
	} else if (pid > 0) {
		// ----- Im Vater -----
		if (close(clientSocket) == -1) {
			perror("close() fehlgeschlagen");
		}
	} else {
		perror("fork() fehlgeschlagen");
		exit(EXIT_FAILURE);
	}
}
