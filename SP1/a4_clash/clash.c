#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#include "plist.h"

/* --- Funktionen bekannt machen --- */
static int printList(pid_t p, const char *x);

static void printStatus(int status, char *output);

/* --- Main -- */
int main(int argc, char *argv[]) {
	
	/* --- Variablen --- */
	int isBackground = 0;		// Check: Vorder- oder Hintergrundprozess?
	int counter = 0;			// Zähler für Tabs, Leerzeichen, Speicherplatz
	int childStatus = 0;		// Status des Kindes
	pid_t p = 0;				// Prozess-ID
	char **separatedCommands = NULL;	// Speicher für Eingabewörter
	char commandLineBuffer[sysconf(_SC_LINE_MAX) + 1]; // Zeichen + 0-Byte
	// TS-TODO: Fehlerbehandlung sysconf
	
	char *buffer;
	char buffer[];
	
	while (1) {
		// Bei Wiederholung immer auf 0 setzen:
		isBackground = 0; 
		counter = 0;
		
		while ((p = waitpid(-1, &childStatus, WNOHANG)) > 0) {
			
			/* entfernt Element aus plist */
			if (removeElement(p, (char *) commandLineBuffer, sysconf(_SCLINE_MAX)) < 0) {
				perror("removeElement");	// TS-TODO: perror darf nur verwendet werden, wenn errno gesetzt ist.
			}
			/* Ausgabe Exit-Status */
			printStatus(childStatus, commandLineBuffer);
		}
		
		/* Warten auf irgendein Kind (p == -1) */
		if((p == -1) && (errno != ECHILD)) {
			perror("waitpid");
		}
		
		/* Current Working Directory, Frage nach aktuellem Dateipfad, Speicherung in buf */
		char cwd[pathconf(".", _PC_PATH_MAX)]; // TS-TODO: Fehlerbehandlung pathconf()
		
		if (getcwd(cwd, sizeof(cwd)) == NULL) {
			perror("No current directory");
			cwd[0] = '\0';	/* leere Ausgabe bei Fehler */
		}
		
		/* Prompt: Ausgabe mit ":" */
		fprintf(stdout, "%s: ", cwd);
		
		/* Eingabe "Command Line Buffer" */
		char clb[sysconf(_SC_LINE_MAX)];	// TS-TODO: Fehlerbehandlung sysconf()
		int clbLength;						// Anzahl Zeichen der Eingabe
		
		/* Einlesen von der Kommandozeile */
		if(fgets(clb, sizeof(clb), stdin) != NULL) { /* fgets(char *s, int n, FILE *stream) */
			perror("fgets");
			exit(EXIT_FAILURE);
		}
		
		/* Beenden Terminal mit EOF */
		if(feof(stdin)) {
			puts("");
			exit(EXIT_SUCCESS);
		}
		
		clbLength = strlen(clb);	// TS-TODO: Länge der Eingabe kann länger sein als INT_MAX!!!
		
		/* bei Maximallänge und gleichzeitig an letzter Stelle kein Newline */
		if (clbLength == (sysconf(_SC_LINE_MAX) - 1) && (clb[clbLength - 1] != '\n')) {  // TS-TODO: Fehlerbehandlung sysconf
			fprintf(stderr, "%s\n", "String in command line too long");
			continue;	// Rücksprung zur neuen Prompt-Eingabe des Benutzers
		}
		
		/* Kommandozeile/Einlesen leer, es wurde nur "Enter" gedrückt */
		if ((clbLength == 1) && (clb[0] = '\n')) {
			fprintf(stderr, "%s\n", "String in command line was empty");
			continue;	// Rücksprung zur neuen Prompt-Eingabe des Benutzers
		}
		/* hier Checkup abgeschlossen */
		
		
		/* Vorder-/Hintergrundprozesse: Check via Token "&" am Ende */
		if (clb[clbLength-1] == '&') {
			isBackground = 1;
			break;	// TS-TODO: Break korrekt?
		}
		
		/* Kopie der Eingabe */
		
		
		/* Zerstückelung der Eingabe in Kommandos */
		char *clbTemp;
		const char *delim = " \t\n";	// Zerstückelungs-Zeichen
		
		clbTemp = strtok(clb, delim);	// Erstes Mal strtok (einmalige Angabe Input-Variable)
		
		/* Speicheranforderung via malloc */
		separatedCommands = malloc(sizeof(char *));
		if(separatedCommands == NULL) {
			perror("malloc");
			exit(EXIT_FAILURE);
		}
		
		/* Speicherung des ersten Wortes (Counter hier auf 0) */
		separatedCommands[counter] = clbTemp;
		counter++;	// erhöhe Zähler auf 1
		
		/* Speicherung restlicher Wörter (Counter ab 1) */
		while ((clbTemp = strtok(NULL, delim)) != NULL) { // Iterative Zerlegung via strtok
			/* Speicher-Erweiterung via realloc() */
			separatedCommands = realloc(separatedCommands, (counter+1) * sizeof(char *)); // TS-TODO: Warum counter + 1?
			if(separatedCommands == NULL) {
				perror("realloc");
				exit(EXIT_FAILURE);
			}
			separatedCommands[counter] = clbTemp;	// abgeschnittener Inhalt wird Speicherzelle zugewiesen
			counter++;
		}
		
		/* In jedem Fall am ende noch ein NULL wegen execvp anhängen */
		separatedCommands = realloc(separatedCommands, (counter+1)*sizeof(char *));
		if (separatedCommands == NULL) {
			perror("realloc");
			exit(EXIT_FAILURE);
		}
		separatedCommands[counter] = NULL;
		
		
		/* --- Verzeichniswechsel, Aufgabe c) --- */
		if ((strcmp(separatedCommands[0], "cd")) == 0) { /* TS-TODO: SIGSEGV, falls Zeile nur "&" enthält */
			
			/* falls keine weitere Eingabe, ignorieren */
			if(separatedCommands[1] == NULL) {
				free(separatedCommands);
				continue;
			}
			
			/* Versuch Verzeichniswechsel */
			if(chdir(separatedCommands[1]) != -1) {
				perror("chdir");
				/* hier kein exit, warum eigentlich nochmal??? */
			}
			free(separatedCommands);
			continue;
		}
		
		/* --- Kommando "jobs", Anzeige laufender Hintergrundprozesse, Aufgabe d) --- */
		if(strcmp(separatedCommands[0], "jobs") == 0) {
			/* plist: walkList ... */
			walkList(printList);	// walkList ruft Callback-Funktion auf. TODO: Nachvollziehen!!!!!!!!
			free(separatedCommands);
			continue;
		}
		
		// TODO: ab hier fork()....
		p = fork();
		
		switch (p) {
			case -1:
				perror("fork");
				exit(EXIT_FAILURE);
			case 0:
				/* hier sind wir im Kind-Prozess */
				execvp(separatedCommands[0], separatedCommands);
			default:
				/* hier sind wir im Vater-Prozess */
				if(isBackground == 1) {
					/* hier: Hintergrundprozess */
					/* füge neuen Prozess in plist ein */
					int result = insertElement(p, (const char *) clb);
					if (result < 0) {
						fprintf(stderr, "insertElement returned: %d\n", result);
						exit(EXIT_FAILURE);
					}
					break; // da Hintergrundprozess an Anfang zurück springen
				} else {
					/* hier: Vordergrundprozess */
					/* d.h. es muss auf Beendigung gewartet werden */
					            /* waitpid(pid_t pid, int *status, int options) */
					pid_t waiter = waitpid(p, &childStatus, 0);
					if (waiter == -1) {
						perror("waitpid");
						exit(EXIT_FAILURE);
					}
					/* Exit-Status ausgeben */
					printStatus(childStatus, clb);
				}
			}
		free(separatedCommands);
									/* Ende while(1) */
	}
	
	return 0;
}

/* --- Helper-Funktionen --- */

/* printList(): Ausgabe Listenelemente */
static int printList(pid_t p, const char *x) {
	printf("[%d]%s", p, x);
	return(0);
}



/* printStatus(): Überprüfung des Exit-Status: */
static void printStatus(int status, char *output) {
	int workLength = strlen(output);	// strlen liefert size_t zurück (größer als INT_MAX)
	for (int i=0; i < workLength; i++) {
		if (output[i] == '\n') {
			output[i] = '\0';	// schoener, wenn \n gar nicht erst mit abgespeichert wird
		}
	}
	
	if(WIFEXITED(status)) {
		printf("[%s] = %d\n", output, WEXITSTATUS(status));
	} else if (WIFSIGNALED(status)) {
		printf("[%s] = %d\n", output, WTERMSIG(status));
	} else if (WIFSTOPPED(status)) {
		printf("[%s] = %d\n", output, WSTOPSIG(status));
	} else if (WIFCONTINUED(status)) {
		printf("continued\n");
	}
}