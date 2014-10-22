/**
 * @file  request.h
 * @brief Header for the request-handling module.
 */

#ifndef REQUEST_H
#define REQUEST_H

#include <stdio.h>


/**
 * @brief  Initializes the request-handling module.
 * @note   This function must be invoked after cmdlineInit().
 * @return 0 on success, -1 if the command-line arguments are invalid. If a
 *         non-recoverable error occurs during initialization (e.g. a failed
 *         memory allocation), the function does not return, but instead prints
 *         a meaningful error message and terminates the process.
 */
int initRequestHandler(void);

/**
 * @brief Handles requests coming from a client.
 *
 * This function does the actual work of communicating with the client. It
 * should be called from the connection-handling module.
 *
 * @param fromClient Client-connection stream opened for reading. It is the
 * caller's responsibility to close it after this function has returned.
 * @param toClient Client-connection stream opened for writing. It is the
 * caller's responsibility to close it after this function has returned.
 */
void handleRequest(FILE *fromClient, FILE *toClient);

/**
 * @brief Handles internal server errors.
 *
 * This function handles internal server errors which occur before the
 * handleRequest() function can be called. It should be called from the
 * connection-handling module.
 *
 * @param client The filedescriptor for the client connection.
 */
void handleInternalError(int client);

#endif /* REQUEST_H */
