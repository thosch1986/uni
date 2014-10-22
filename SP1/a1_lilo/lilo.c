#include <stdio.h>
#include <stdlib.h>

// Anlegen der Struktur: Wie soll ein Listenelement beschaffen sein?
struct element {
	int value;				// Wert des Elements
	struct element *next;	// Pointer auf nächstes Element
};

// Anfangszeiger Head zeigt auf Null:
static struct element *head;

// Bekanntmachen der Funktionen:
int insertElement(int value);
int removeElement();

// Main:
int main(int argc, char *argv[]) {
	printf("insert 47: %d\n", insertElement(47));
	printf("insert 11: %d\n", insertElement(11));
	printf("insert 23: %d\n", insertElement(23));
	printf("insert 11: %d\n", insertElement(11));
	printf("remove: %d\n", removeElement());
	printf("remove: %d\n", removeElement());
	return(EXIT_SUCCESS);
}

// Funktion: "insertElement(int value)"
int insertElement(int value) {
	struct element *pointer = head;				// Zeiger auf Anfang
	struct element *delayPointer = pointer;	// Schleppzeiger auf Zeiger
	struct element *newElement = NULL;			// Neues Element zeigt ins "Nirvana"
	
	// Fehlermeldung, falls Wert nicht-negative Ganzzahl:
	if (value < 0) {
		return -1;
	}
	
	while (pointer != NULL) {			// führe aus, solange Listenende nicht erreicht
		// Fehlermeldung, falls Wert schon vorhanden:
		if (pointer->value == value) {
			return -1;
		}
		delayPointer = pointer;			// ziehe Schleppzeiger nach
		pointer = pointer->next;		// wandere weiter
	}
	
	// Hier sind wir am Listenende angekommen
	// => wir machen uns bereit für Einfügen des Wertes
	
	// Das bedeutet: Allokation von neuem Speicher für unser neues Listenelement
	//newElement = malloc(sizeof(struct element)); // ergibt Seg-Fault auf dem Mac, warum???
	newElement = calloc(1, sizeof(struct element));
	// Fehlermeldung falls Speicherallokation fehlschlägt:
	if (newElement == NULL) {
		return -1;
	}
	
	// Fall 1: Liste leer/keine Verkettung:
	if (head == NULL) {
		head = newElement;		// head zeigt nun auf freien Speicher
		head->value = value;	// head-Element Wert zuweisen
	} else {
		// Fall 2: Liste nicht leer/Verkettung vorhanden:
		delayPointer->next = newElement;	// Schleppzeiger nächstes zeigt auf neue Speicherstelle
		delayPointer->value = value;		// Schleppzeiger Wert zuweisen
	}
	
	// lt. Aufgabenstellung: Wert zurückgeben:
	return value;
}

int removeElement() {
	struct element *pointer = NULL;		// "Arbeitszeiger"
	int value = 0;						// Variable für Wertrückgabe
	
	// Falls Liste bereits leer:
	if (head == NULL) {
		return -1;
	}
	
	value = head->value;	// Wert ist der Wert auf den Head zeigt
	head = head->next;		// Lasse head auf nächsten Wert zeigen
	free(head);			// "Löschung" des ursprünglichen Head-Wertes
	head = pointer;			// Arbeitszeiger zeigt auf Head
	return value;			// Rückgabe des entfernten Wertes
}