/**
 * @file  dirlisting.h
 * @brief Directory-listing helper functions.
 *
 * This module contains routines for creating a directory-listing HTML page.
 */

#ifndef DIRLISTING_H
#define DIRLISTING_H

#include <stdio.h>


/**
 * @brief Prints the beginning of a directory-listing HTML page to a stream.
 *
 * In addition to the HTML header, a page title and a pseudo-entry referring to
 * the parent directory are printed.
 *
 * @param stream  The output stream.
 * @param dirName Name of the directory.
 */
void dirlistingBegin(FILE *stream, const char dirName[]);

/**
 * @brief Prints an entry of a directory listing to a stream.
 * @param stream    The output stream.
 * @param dirName   Name of the directory containing the entry.
 * @param entryName Name of the entry.
 */
void dirlistingPrintEntry(FILE *stream, const char dirName[],
                          const char entryName[]);

/**
 * @brief Prints the end of a directory-listing HTML page to a stream.
 * @param stream The output stream.
 */
void dirlistingEnd(FILE *stream);


#endif /* DIRLISTING_H */
