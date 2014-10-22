/**
 * @file  connection.h
 * @brief Header for the connection-handling module.
 */

#ifndef CONNECTION_H
#define CONNECTION_H


/**
 * @brief  Initializes the connection-handling module.
 * @note   This function must be invoked after cmdlineInit().
 * @note   It is the responsibility of this function to initialize the
 *         request-handling module by calling initRequestHandler().
 * @return 0 on success, -1 if the command-line arguments are invalid. If a
 *         non-recoverable error occurs during initialization (e.g. a failed
 *         memory allocation), the function does not return, but instead prints
 *         a meaningful error message and terminates the process.
 */
int initConnectionHandler(void);

/**
 * @brief Handles an incoming connection.
 *
 * This function should be called from the main module whenever a new connection
 * has been accepted.
 *
 * @param clientSock Socket representing the accepted connection from the
 *                   client. Upon completing the request or as soon as it is no
 *                   longer needed, this socket is closed.
 * @param listenSock Socket the server is listening on. If the implementation
 *                   launches a child process to handle the connection, this
 *                   socket must be closed in that child process.
 */
void handleConnection(int clientSock, int listenSock);


#endif /* CONNECTION_H */
