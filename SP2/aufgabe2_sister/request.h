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
 * @param client Client-connection stream. It is the caller's responsibility to
 *               close it after this function has returned.
 */
void handleRequest(FILE *client);


#endif /* REQUEST_H */
