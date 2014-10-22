/**
 * @file  sbuf.h
 * @brief Semaphore buffer.
 */

#ifndef SBUF_H
#define SBUF_H

#include "sem.h"

/** Opaque type of a semaphore buffer. */
typedef struct SBUF SBUF;

/**
 * @brief Creates a new semaphore buffer.
 *
 * This function creates a new semaphore buffer. If an error occurs during the
 * initialization, the implementation frees all resources already allocated by
 * then.
 *
 * @param maxNumberOfSems Maximum number of semaphores, must be positive.
 * @return Handle for the created semaphore buffer, or @c NULL if an error
 * occurred.
 */
SBUF* sbufCreate(int maxNumberOfSems);
/**
 * @brief Add a new semaphore to the buffer.
 *
 * This function adds a new semaphore to the buffer and returns its unique id.
 *
 * @param cl Handle of the semaphore buffer to use.
 * @param sem Handle of the semaphore to add to the buffer.
 * @return Id of the added semaphore, can be used in sbufGetSem(), or @c -1 if
 * an error occurred.
 */
int sbufAddSem(SBUF* cl, SEM* sem);
/**
 * @brief Get number of semaphores stored in the buffer.
 *
 * This function returns the number of semaphores currently stored in the
 * buffer.
 *
 * @param cl Handle of the semaphore buffer to use.
 * @return Number of semaphores stored in the buffer.
 */
int sbufGetNumberOfSems(SBUF* cl);
/**
 * @brief Get semaphore handle for the given semaphore id.
 *
 * This function returns the semaphore handle for the semaphore stored with
 * the given id, as returned by sbufAddSem().
 *
 * @param cl Handle of the semaphore buffer to use.
 * @param index Id of the semaphore to select.
 * @return Handle for the semaphore, or @c NULL if it wasn't found or an error
 * occurred.
 */
SEM* sbufGetSem(SBUF* cl, int index);

#endif /* SBUF_H */
