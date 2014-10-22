#include <stdlib.h>
#include "sem.h"
#include <stdio.h>

/** Opaque type of a bounded buffer. */
typedef struct BNDBUF BNDBUF {
	int *puffer;
	int size;
	int read;
	int write;
	SEM *belegt;	// in wie vielen Arrayplaetzen steht was drin?
	SEM *leer;		// in wie vielen Arrayplaetzen ist nichts gespeichert?
}

/**
 * @brief Creates a new bounded buffer.
 *
 * This function creates a new bounded buffer and all the required helper data
 * structures, including semaphores for synchronization. If an error occurs
 * during the initialization, the implementation frees all resources already
 * allocated by then.
 *
 * @param size The number of integers that can be stored in the bounded buffer.
 * @return Handle for the created bounded buffer, or @c NULL if an error
 *         occurred.
 */
BNDBUF *bbCreate(size_t size) {
	// Speicher für BNDBUF struct anlegen
	BNDBUF *ringpuffer = (BNDBUF *) malloc(sizeof(struct BNDBUF));
	if (ringpuffer == NULL) {
		return NULL;
	}
	
	// Extra Speicher für Daten anlegen
	ringpuffer->puffer = (int *) malloc(size*sizeof(int));
	if(ringpuffer->puffer == NULL) {
		free(ringpuffer);	// vorher angelegter Speicher ebenfalls sinnlos
		return NULL;
	}
	
	// Anfangswerte des Ringpuffers initialisieren:
	ringpuffer->size = size;
	ringpuffer->read = 0;
	ringpuffer->write = 0;
	
	ringpuffer->belegt = semCreate(0);		// Semaphor mit 0 initialisieren (aus Bib)
	if(ringpuffer->belegt == -1) {
		free(ringpuffer->puffer);
		free(ringpuffer);
		return NULL;
	}
	
	ringpuffer->frei = semCreate(size);		// Semaphor mit size initialisieren (aus Bib)
	if(ringpuffer->frei == -1) {
		free(ringpuffer->puffer);
		free(ringpuffer);
		return NULL;
	}
	return ringpuffer;
}

/**
 * @brief Destroys a bounded buffer.
 *
 * All resources associated with the bounded buffer are released.
 *
 * @param bb Handle of the bounded buffer that shall be freed. If a @c NULL
 *           pointer is passed, the implementation does nothing.
 */
void bbDestroy(BNDBUF *bb) {
	
	free(bb->puffer);
	
	if (semDestroy(bb->belegt) == NULL) {
		fprintf(stderr, "Probleme beim Löschen des Semaphors");
		exit(EXIT_FAILURE);
	}
	
	if (semDestroy(bb->leer) == NULL) {
		fprintf(stderr, "Probleme beim Löschen des Semaphors");
		exit(EXIT_FAILURE);
	}	
	
	// am Schluss den ganzen Scheiss freen:
	free(bb);
}

/**
 * @brief Adds an element to a bounded buffer.
 *
 * This function adds an element to a bounded buffer. If the buffer is full, the
 * function blocks until an element has been removed from it.
 *
 * @param bb    Handle of the bounded buffer.
 * @param value Value that shall be added to the buffer.
 */
void bbPut(BNDBUF *bb, int value) {
	// leeren Speicher erniedrigen
	P(bb->leer);
	
		bb->puffer[bb->write] = value;
		bb->write++;
		if(bb->write >= bb->size) {
			bb->write = bb->write % bb->size;
		}
	// belegten Speicher erhöhen
	V(bb->belegt);
}

/**
 * @brief Retrieves an element from a bounded buffer.
 *
 * This function removes an element from a bounded buffer. If the buffer is
 * empty, the function blocks until an element has been added.
 *
 * @param bb Handle of the bounded buffer.
 * @return The integer element.
 */
int bbGet(BNDBUF *bb) {
	// vollen Speicher erniedrigen (Umgekehrt wie bbPut)
	P(bb->belegt);		// blockiert falls alles belegt
		
		int tempValue = bb->puffer[bb->read];
		
		do {
			tempValue = bb->puffer[bb->read];
		} while (__sync_bool_compare_and_swap((int *) &(bb->read), bb->read, (bb->read+1) % bb->size));
	
	V(bb->leer);		// freigeben
	
	return tempValue;
}
