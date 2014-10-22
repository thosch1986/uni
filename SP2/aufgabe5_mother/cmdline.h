/**
 * @file  cmdline.h
 * @brief Command-line argument parsing module.
 *
 * This module is useful for parsing the @c argv array passed to a program via
 * the command line, which can consist of the following argument classes:
 * - The program name (@c argv[0]), which can be retrieved by calling
 *   cmdlineGetProgramName().
 * - Key-value pairs in the form @a "-key=value" or @a "--key=value". These can
 *   be queried using cmdlineGetValueForKey().
 * - Boolean flags in the form @a "-flag" or @a "--flag". The cmdlineGetFlag()
 *   function determines whether a specific flag has been set.
 * - Everything which has neither of the above formats is called an "extra
 *   argument". The user can retrieve the number of extra arguments via
 *   cmdlineGetExtraArgCount() and iterate over them by calling
 *   cmdlineGetExtraArg().
 *
 * Both keys and flags must begin with one or two dashes followed by one
 * alpha-numeric character and an arbitrary number of additional characters
 * (excluding @c '\0' and @c '=').
 */

#ifndef CMDLINE_H
#define CMDLINE_H


#include <stdbool.h>


/**
 * @brief Initializes the command-line parsing module.
 *
 * This function must be invoked before any of the other functions.
 *
 * @param argc Number of command-line arguments.
 * @param argv Array of command-line arguments. The contents of this array are
 *             not modified in any manner.
 * @return 0 on success. If @a argc or @a argv is invalid, -1 is returned and @a
 *         errno is set to @c EINVAL.
 */
int cmdlineInit(int argc, char *argv[]);

/**
 * @brief Retrieves the program name.
 * @return The value of @c argv[0].
 */
const char *cmdlineGetProgramName(void);

/**
 * @brief Gets the corresponding value for a given option key (@a "-key=value"
 *        or @a "--key=value").
 *
 * If the same key appears several times in the command line, the value of its
 * latest occurrence is returned.
 *
 * @param key The key. Leading dashes may be omitted.
 * @return The value associated with the given key, or @c NULL if no such pair
 *         exists.
 */
const char *cmdlineGetValueForKey(const char key[]);

/**
 * @brief Checks if a given flag (@a "-flag" or @a "--flag") was passed via the
 *        command line.
 * @param flag Name of the flag. Leading dashes may be omitted.
 * @return @c true if the flag is set, otherwise @c false.
 * @note This function cannot be used to query for the existence of a given key
 *       in a key-value pair. The canonical way for this is to call
 *       cmdlineGetValueForKey() and check its return value.
 */
bool cmdlineGetFlag(const char flag[]);

/** Retrieves the number of extra command-line arguments. */
unsigned int cmdlineGetExtraArgCount(void);

/**
 * @brief Retrieves an extra argument.
 * @param index Zero-based index of the extra argument.
 * @return The extra argument with the given index, or @c NULL if @a index is
 *         greater than or equal to the number of extra arguments.
 */
const char *cmdlineGetExtraArg(unsigned int index);


#endif /* CMDLINE_H */
