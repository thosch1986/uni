#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define MAXLENGTH 102

// Funktionen bekannt machen:
static int compareString(const void *x, const void *y);

int main(int argc, char *argv[]) {
	
	char **base;
	int length;
	
	if (argc != 1) {
		base = ++argv;
		length = argc - 1;
	} else {
		base = NULL;
		length = 0;
		
		char buffer[MAXLENGTH];
		char *stringPointer;
		int c_ar_size = 1;	// Was ist das?
		int length2;		// Was ist das?
		
		// Einlesen von StdIn:
		while ( fgets(buffer, MAXLENGTH, stdin) != NULL) {
			length2 = strlen(buffer);	// Länge des Strings ohne Null
			if (buffer[length2-1] == '\n') {
				buffer[length2-1] = '\0';	// Wenn Newline, ersetze durch '\0'
				--length2;
			}
			
			if (length2 == 0) {
				continue;	// Bei Leerzeile, einfach weitermachen.
			}
			
			if (length2 > 100) {	// Falls Wort zu lange
				fputs(": Wort zu lang!\n", stderr);
				int cache = fgetc(stdin);		// TODO: Fehlerüberprüfung fgetc
				while (cache != (int)'\n') {
					if(cache == EOF) {
						break;
					}
					cache = fgetc(stdin);
				}
				continue;
			}
			
			stringPointer = malloc(sizeof(char) * length2 + 1); // Wartum + 1???
			if (stringPointer == NULL) {
				fputs("kein Speicher vom Betriebssystem", stderr);
				return EXIT_FAILURE;
			}
			
			// Speicherung des eingelesenen Strings:
			strncpy(stringPointer, buffer, length2);
			stringPointer[length2] = '\0'; // Auf letzte Stelle ein '\0' setzen.
			
			base = (char **) realloc(base, sizeof(char *) * c_ar_size); // TODO: blockweises Allokieren effizienter (Was ist das?)
			if (base == NULL) {
				fputs("kein Speicher vom Betriebssystem", stderr);
				return EXIT_FAILURE;
			}
			
			base[length] = stringPointer;
			c_ar_size++;
			length++;
			// Einlesen von stdin beendet.
		}
	}
	
	// Funktionscast auf Int:
	int (* vergleich) (const void *, const void *);  // Funktionspointer
	vergleich = &compareString;		// Adresse von Funktion CS zeigt nun auf vergleich
	
	// Aufruf Sortierfunktion:
	qsort(base, length, sizeof(char *), vergleich);
	
	// Ausgabe:
	for (int i=0; i < length; i++) {
		fputs(base[i], stdout);
		putchar((int) '\n');	// Newline anhängen
	}
	
	// Speicher freigeben:
	if (argc == 1) {
		for (int i=0; i < length; i++) {
			free(base[i]);
		}
		free(base);
	}
	
	return 0;
}


// Vergleichsfunktion unter Zuhilfename von strcmp():
static int compareString(const void *x, const void *y) {
	const char **xx = (const char **) x;
	const char **yy = (const char **) y;
	
	return(strcmp(*xx, *yy));
}

