#include <limits.h>
#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "sbuf.h"

#define LINEMAX 100

/* Haupt-Thread befindet sich in main() */
int schreibThread();

int main(int argc, char *argv[]) {

	if ((sysconf(_SC_LINE_MAX) == -1)) {
		fprintf(stderr, "%s\n", "sysconf");
		exit(EXIT_FAILURE);
	}
	
	/* --- Überprüfung Argumente --- */
	if ((argc < 2) || (argc > 2)) {
		fprintf(stderr, "too many or too less arguments: usage: piper <pipes...>");
		exit(EXIT_FAILURE);
	}
	
	/* --- Parsen der Pipes --- */
	int numPipes = atoi(argv[1]);
	if (numPipes == 0) {
		fprintf(stderr, "wrong argument: no number");
		exit(EXIT_FAILURE);
	}
	
	printf("Zahl von Argumenten: %d", numPipes);
	pthread_t tids[numPipes];
	
	/* --- Threads erzeugen --- */
	for (int i=0; i < numPipes; i++) {
		/* errno wird nicht automatisch gesetzt */
		if ((errno = pthread_create(&tids, NULL, schreibThread,  /*welches Argument ? */) != 0) {
			perror("pthread_create");
			exit(EXIT_FAILURE);
		}
	}
	
	if ((fflush(stdout) != 0) {
		perror("fflush");
	}
}