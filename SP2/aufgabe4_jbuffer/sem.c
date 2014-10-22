/**
 * @file  sem.h
 * @brief Semaphore implementation for the synchronization of POSIX threads.
 *
 * This module implements counting P/V semaphores suitable for the
 * synchronization of POSIX threads. POSIX mutexes and condition variables are
 * utilized to implement the semaphor operations.
 */


/** Opaque type of a semaphore. */
typedef struct SEM SEM {
	int value;
	pthread_mutex_t mVar;
	pthread_cond_t cVar;
} SEM;

/**
 * @brief Creates a new semaphore.
 *
 * This function creates a new semaphore. If an error occurs during the
 * initialization, the implementation frees all resources already allocated by
 * then and sets @c errno to an appropriate value.
 *
 * It is legal to initialize the semaphore with a negative value. If this is the
 * case, in order to reset the semaphore counter to zero, the V-operation must be
 * performed @c (-initVal) times.
 *
 * @param initVal The initial value of the semaphore.
 * @return Handle for the created semaphore, or @c NULL if an error occurred.
 */
SEM *semCreate(int initVal) {
	SEM *semaphor;
	
	semaphor = (SEM *) malloc(sizeof(struct SEM));
	if (semaphor == NULL) {
		return NULL;
	}
	
	semaphor->value = initVal;
	
	if (pthread_cond_init(&semaphor->cVar, NULL) != 0) {
		perror("Fehler bei pthread_cond_init");
		semDestroy(semaphor);
		return NULL;
	}
	
	if (pthread_mutex_init(&semaphor->mVar, NULL) != 0) {
		perror("Fehler bei pthread_mutex_init");
		semDestroy(semaphor);
		return NULL;
	}
	
	return semaphor;
}

/**
 * @brief Destroys a semaphore and frees all associated resources.
 * @param sem Handle of the semaphore to destroy. If a @c NULL pointer is
 *            passed, the implementation does nothing.
 */
void semDestroy(SEM *sem) {
	
	if (pthread_mutex_destroy(&(sem->mVar)) == EBUSY) {
		if (pthread_mutex_unlock(&(sem->mVar)) != 0) {
			perror("Not all resources may have been freed - pthread_mutex_unlock");
		}
	}
	
	if (pthread_mutex_destroy(&(sem->mVar)) != 0) {
		perror("Konnte pthread_mutex_destroy nicht anwenden");
	}
	
	if (pthread_cond_destroy(&(sem->cVar)) != 0) {
		perror("Konnte pthread_cond_destroy nicht anwenden");
	}
	
	free(sem);
}

/**
 * @brief P-operation.
 *
 * Attempts to decrement the semaphore value by 1. If the semaphore value is not a
 * positive number, the operation blocks until a V-operation increments the value
 * and the P-operation succeeds.
 *
 * @param sem Handle of the semaphore to decrement.
 */
void P(SEM *sem) {
	// Semaphor-Implementierung
	if (pthread_mutex_lock(&(sem->mVar)) != 0) {
		perror("P Funktion error lock");
	}
	
	while (sem->a == 0) {
		pthread_cond_wait(&(sem->mVar), &(sem->cVar));
		// laut manpage kein eror
	}
	sem->a--;
	
	if (pthread_mutex_unlock(&(sem->mVar)) != 0) {
		perror("P Funktion error unlock");
	}
}

/**
 * @brief V-operation.
 *
 * Increments the semaphore value by 1 and notifies P-operations that are
 * blocked on the semaphore of the change.
 *
 * @param sem Handle of the semaphore to increment.
 */
void V(SEM *sem) {
	// Semaphor-Implementierung
	if (pthread_mutex_lock(&(sem->mVar)) != 0) {
		perror("P Funktion error lock");
	}

	sem->a++;
	pthread_cond_broadcast(&(sem->cVar));

	if (pthread_mutex_unlock(&(sem->mVar)) != 0) {
		perror("P Funktion error unlock");
	}
}

