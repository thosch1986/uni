#ifndef __ARGUMENTPARSER_H__
#define __ARGUMENTPARSER_H__

/**
 * @file  argumentParser.h
 * @brief Simple command-line argument parsing module.
 *
 * This module is useful for parsing the @c argv array passed to a program via
 * the command line, which can consist of the following argument classes:
 * - The program name (@c argv[0]), which can be retrieved by calling
 *   getProgramName().
 * - Key-value pairs (called "option") in the form @a "-key=value". These can
 *   be queried using getValueForOption(). These options have to be specified
 *   after all "arguments".
 * - Everything which has neither of the above formats is called an "argument".
 *   The user can retrieve the number of arguments via
 *   getNumberOfArguments() and iterate over them by calling
 *   getArgument().
 *
 * Command-line argument format supported by this modul:
 * <command> [arguments..] [options..]
 * After the command an arbitray number of arguments followed by an arbitrary
 * number of options can be specified. Both, arguments and options, are
 * optional.
 */

/**
 * @brief Initializes the simple command-line parsing module.
 *
 * This function must be invoked before any of the other functions.
 *
 * @param argc Number of command-line arguments.
 * @param argv Array of command-line arguments. The contents of this array are
 *             not modified in any manner.
 * @return 0 on success. If @a argc or @a argv is invalid, -1 is returned and @a
 *         errno is set to @c EINVAL.
 */
int initArgumentParser(int argc, char* argv[]);

/**
 * @brief Retrieves the program name.
 * @return The value of @c argv[0].
 */
char* getCommand();

/**
 * @brief Gets the corresponding value for a given option key (@a "-key=value")
 *
 * If the same key appears several times in the command line, the value of its
 * first occurrence is returned.
 *
 * @param key The key without leading dash.
 * @return The value associated with the given key, or @c NULL if no such pair
 *         exists.
 */
char* getValueForOption(char* keyName);

/** Retrieves the number of arguments. */
int getNumberOfArguments();

/**
 * @brief Retrieves an argument.
 * @param index Zero-based index of the argument.
 * @return The argument with the given index, or @c NULL if @a index is
 *         greater than or equal to the number of arguments.
 */
char* getArgument(int index);

#endif // __ARGUMENTPARSER_H__
