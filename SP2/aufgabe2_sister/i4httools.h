/**
 * @file  i4httools.h
 * @brief Miscellaneous routines for building an HTTP server.
 *
 * This module contains some routines that are useful for implementing a web
 * server.
 */

#ifndef I4HTTOOLS_H
#define I4HTTOOLS_H

#include <stdio.h>


/**
 * @brief Checks if a relative path leaves the root directory.
 *
 * This routine checks if the relative path component of a request ascends the
 * VFS beyond the root directory. Such requests should not be served.
 *
 * @note This function stops processing as soon as the path ascends beyond the
 *       root of the web directory.
 * @param relPath Requested URL.
 * @return Positive depth of the request relative to the web directory,
 *         or -1 if the relative URL ascends beyond the web directory.
 */
int checkPath(const char relPath[]);

/**
 * @brief Outputs an "OK" HTTP response to a stream.
 *
 * This routine generates an "OK (200)" status line on the given stream, which
 * should normally be the client connection.
 *
 * @param client The output stream.
 */
void httpOK(FILE *client);

/**
 * @brief Outputs a "moved permanently" HTTP response and error page to a
 *        stream.
 *
 * This routine generates a "moved permanently (301)" HTTP response on the given
 * stream, which should normally be the client connection.
 *
 * @param client     The output stream.
 * @param newRelPath Path to which the request is redirected.
 *
 * @note This function is not needed for assignment 2 (@c sister), only for
 *       assignment 5 (@c mother).
 */
void httpMovedPermanently(FILE *client, const char newRelPath[]);

/**
 * @brief Outputs a "bad request" HTTP response and error page to a stream.
 *
 * This routine generates an "HTTP/1.0 400 Bad Request" status line and a
 * corresponding HTML error page on the given stream, which should normally be
 * the client connection.
 *
 * @param client  The output stream.
 * @param request Request string.
 */
void httpBadRequest(FILE *client, const char request[]);

/**
 * @brief Outputs a "forbidden" HTTP response and error page to a stream.
 *
 * This routine generates an "HTTP/1.0 403 Forbidden" status line and a
 * corresponding HTML error page on the given stream, which should normally be
 * the client connection.
 *
 * @param client  The output stream.
 * @param relPath Requested URL.
 */
void httpForbidden(FILE *client, const char relPath[]);

/**
 * @brief Outputs a "not found" HTTP response and error page to a stream.
 *
 * This routine generates an "HTTP/1.0 404 Not Found" status line and a
 * corresponding HTML error page on the given stream, which should normally be
 * the client connection.
 *
 * @param client  The output stream.
 * @param relPath Requested URL.
 */
void httpNotFound(FILE *client, const char relPath[]);

/**
 * @brief Outputs an "internal server error" HTTP response and error page to a
 *        stream.
 *
 * This routine generates an "HTTP/1.0 500 Internal Server Error" status line
 * and a corresponding HTML error page on the given stream, which should
 * normally be the client connection.
 *
 * @param client  The output stream.
 * @param relPath Requested URL. May be @c NULL if not applicable.
 */
void httpInternalServerError(FILE *client, const char relPath[]);


#endif /* I4HTTOOLS_H */
