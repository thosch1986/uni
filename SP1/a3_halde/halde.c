#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "halde.h"

/* --- Megabyte & Magick-Paket --- */
#define MIB (1024*1024)
#define MAGIC ((void*)0xbaadf00d)

/* --- Datenstruktur: Verkettete Liste für dyn. Speicherverwaltung --- */
typedef struct mblock {
	struct mblock *next;	// Zeiger zur Verkettung
	size_t size;			// Größe Speicherbereich
	char mem_area[];		// Zeiger auf Speicherbereich, Konstanter Zeiger auf Strukturende
} mblock;

/* --- Ausgangssituation --- */
static char memory[128 * MIB];		// Verwalten von 128 MIB statisch allokiertem Speicherbereich
static mblock *fsp = NULL;

/* ---------- "malloc()-Nachbau" ----------- */
void *malloc(size_t size) {
	
	if(size == 0) {
		return NULL;
	}
	
	// struct mblock reinlegen:
	fsp = (mblock *) memory;
	fsp->next = NULL;
	fsp->size = sizeof(memory) - sizeof(mblock);
	
	
	mblock *iterator = NULL;
	mblock *iteratorOld = NULL;
	
	for (iterator = fsp; iterator != NULL; iterator = iterator->next) {
		/* wenn passende Stelle mit genug Speicher gefunden, 'raus springen und anlegen */
		if (iterator->size >= size) {
			break;
		}
		iteratorOld = iterator;
	}
	
	/* --- kein ausreichender Speicherplatz vorhanden --- */
	if (iterator == NULL) {
		errno = ENOMEM;
		return NULL;
	}
	
	/* Anlage size & mblock */
	if (iterator->size > size + sizeof(mblock)) {
		mblock *newBlock = (mblock *) (iterator->mem_area + size);
		newBlock->next = iterator->next;
		newBlock->size = (iterator->size) - (size + sizeof(mblock));
		iterator->next = newBlock;
	}
	
	/* Zeiger umbiegen */
	if (iteratorOld == NULL) {
		fsp = iterator->next;
	} else {
		iteratorOld->next = iterator->next;
	}
	
	// Eigentliche Speicherallokation: FEHLER!!!!!!
	iterator->size = size;
	iterator->next = MAGIC;
	
	return iterator + 1;	// Überspringen eines mblocks
}


/* ---------- "free()-Nachbau" ---------- */
void free(void *ptr) {
	/* siehe manpage */
	if(ptr == NULL) {
		return;
	}
	
	mblock *p = (mblock *) ptr - 1;		/* warum '-1' ? */
	
	/* --- Prüfung auf MAGIC --- */
	if (p->next != MAGIC) {
		abort();
	}
	
	/* --- Speicher wieder zu Verfügung stellen und fsp umbiegen */
	p->next = fsp;
	fsp = p;
}


/* ---------- "realloc()-Nachbau" ---------- */
void *realloc(void *ptr, size_t size) {
	
	if (size == 0 && ptr != NULL) {
		free(ptr);
		return NULL;
	}
	if (ptr == NULL) {
		return malloc(size);
	}
	
	mblock *p (mblock *) ptr - 1;	// wir arbeiten nur mit diesem Pointer
	void *ptrNew = malloc(size);
	
	if(ptrNew == NULL) {
		return NULL;
	}
	
	// Falls Speicher größer:
	if (size > p->size) {
		/* kopiere Inhalt (dest, src, size) */
		memcpy(ptrNew, ptr, p->size);
	} else {
		/* falls Speicher kleiner, Inhaltskopie */
		memcpy(ptrNew, ptr, size);
	}
	
	// Freigabe von altem Speicher, Rückgabe neuer Pointer
	free(ptr);
	return ptrNew;
}


/* ---------- "calloc()-Nachbau" ---------- */
void *calloc(size_t numElements, size_t size) {
	/* vgl. man-Page, size-Überprüfung via malloc() */
	if (numElements == 0) {
		return NULL;
	}
	// Überlaufbehandlung 'numElements*size'
	void *ptr = malloc(size * numElements);	// errno via malloc()
	if (ptr == NULL) {
		return NULL;
	}
	
	// setze alles ab ptr mit '0'
	memset(ptr, 0, size * numElements);
	return ptr;
}
