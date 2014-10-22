/**
 * @file  getargs.h
 * @brief Command-line argument parsing module.
 */

#ifndef GETARGS_H
#define GETARGS_H


/**
 * @brief Parses a command-line argument array.
 *
 * This function must be invoked before any of the other functions. It parses
 * the given argument array containing key-to-value mappings. The array has to
 * come in a form like this:
 *
 * <tt>program -key0 value0 -key1 value1 ...</tt>
 *
 * Keys have to begin with a dash (@c '-') whereas values must not. The value
 * associated with a given key can be retrieved using getOptionVal(). The
 * array may contain additional arguments that are not part of the mapping
 * before or after the key-value pairs, but not in between. They can be
 * retrieved using getLeadingArg() or getTrailingArg(), respectively. If there
 * are no key-value pairs, all arguments are considered to be both, leading and
 * trailing.
 *
 * @param argc Number of command-line arguments.
 * @param argv Array of command-line arguments.
 * @return 0 on success; -1 if a parsing error occurred.
 */
int parseArgs(int argc, char *argv[]);

/**
 * @brief Retrieves the program name.
 * @return The value of @c argv[0].
 */
const char *getProgramName(void);

/**
 * @brief Retrieves the corresponding value for a given option key.
 *
 * If the command line contains more than one pair with the same key, the value
 * of the last pair is returned.
 * @param option Option key. Must begin with a dash ('-').
 * @return Value corresponding to the key, or @c NULL if no mapping with this
 *         key was found.
 */
const char *getOptionVal(const char option[]);

/**
 * @brief Retrieves the number of command-line arguments leading the options, not
 *        including the program name.
 * @return Number of leading arguments
 */
unsigned int getLeadingArgCount(void);

/**
 * @brief Retrieves a leading argument.
 * @param i Argument index.
 * @return Corresponding leading argument, or NULL if i is out of range.
 * @note   The program name (@c argv[0]) itself is omitted, so <tt>i = 0</tt>
 *         will yield @c argv[1]. It can be retrieved by calling
 *         getProgramName().
 */
const char *getLeadingArg(unsigned int i);

/**
 * @brief Retrieves the number of command-line arguments trailing the options.
 * @return Number of trailing arguments
 */
unsigned int getTrailingArgCount(void);

/**
 * @brief Retrieves a trailing argument.
 * @param i Argument index.
 * @return Corresponding trailing argument, or @c NULL if @c i is out of range.
 */
const char *getTrailingArg(unsigned int i);


#endif /* GETARGS_H */
