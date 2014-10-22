/**
 * @file
 * @brief Helper functions for shell implementations.
 *
 * This module offers the following functionality to facilitate implementing a
 * simple UNIX shell:
 * - Prompt output.
 * - Parsing of a user-supplied command line.
 * - Printing of a process's current state.
 */


#ifndef SHELLUTILS_H
#define SHELLUTILS_H


#include <limits.h>
#include <stdbool.h>
#include <sys/types.h>


/** @brief Maximum length of the command line (including <tt>'\\0'</tt>). */
#if (defined ARG_MAX && ARG_MAX <= 1048576)
	#define MAX_CMDLINE_LEN ARG_MAX
#else
	#define MAX_CMDLINE_LEN 1048576
#endif


/** @brief Process state. */
typedef enum {
	PS_RUNNING,   /**< The process is running. */
	PS_STOPPED,   /**< The process is currently in a stopped state. */
	PS_TERMINATED /**< The process has been terminated. */
} ProcState;

/** @brief Process-information structure. */
typedef struct {
	pid_t     pid;        /**< Process ID. */
	char     *cmdLine;    /**< Original command line. */
	bool      background; /**< Process should run in the background? */
	ProcState state;      /**< Current process state. */
} ProcInfo;

/** @brief The ShCommand structure contains a parsed command. */
typedef struct {
	char *inFile;    /**< If redirection of stdin is specified, points to the
	                      filename of the input file, otherwise to NULL. */
	char *outFile;   /**< If redirection of stdout is specified, points to the
	                      filename of the output file, otherwise to NULL. */
	bool background; /**< true if the command is to be executed in the
	                      background, false otherwise. */
	const char *parseError; /**< Points to an error message if the command line
	                             could not be parsed successfully, NULL if
	                             parsing was successful. */ 
	char *cmdLine;   /**< Copy of the original, un-tokenized command line (with
	                      '<', '>' and '&' if present). */
	char **argv;     /**< Array of the command - not including '<', inFile, '>',
	                      outFile or '&'. */
} ShCommand;


/**
 * @brief Prints a prompt symbol including the shell's current working
 *        directory.
 * @note  This function should @b not be used in a signal handler since it uses
 *        a potentially locking output mechanism to write to <tt>stderr</tt>.
 */
void shPrompt(void);

/**
 * @brief Parses a command line.
 *
 * This function parses a command-line string that may contain <tt>'&'</tt> (for
 * background execution) as well as <tt>'<'</tt> and <tt>'>'</tt> for
 * <tt>stdin</tt> and <tt>stdout</tt> redirection, respectively.
 * shParseCmdLine() will generate the <tt>argv</tt> array for the given command
 * line and return an ShCommand structure containing the result. The members of
 * the <tt>argv</tt> array will point into the original string, whose contents
 * are modified for this purpose.
 *
 * The returned ShCommand structure lies in statically allocated memory and is
 * overwritten by subsequent calls to shParseCmdLine().
 *
 * @param cmdLine The command line to be parsed. This string is tokenized during
 *				  shParseCmdLine(). It must be no more than @ref MAX_CMDLINE_LEN
 *                characters long (including the terminating <tt>'\\0'</tt>),
 *                otherwise <tt>NULL</tt> is returned and <tt>errno</tt> is set
 *                to <tt>EINVAL</tt>.
 *
 * @return Pointer to the parsed ShCommand or <tt>NULL</tt> on error, with
 *         <tt>errno</tt> set appropriately. In case a custom error message is
 *         supplied, the function returns an ShCommand structure that has its
 *         <tt>parseError</tt> field set to a custom error message. If the
 *         <tt>parseError</tt> field is set to a value other than <tt>NULL</tt>,
 *         parsing the command line has failed and the remaining fields of the
 *         ShCommand structure must not be interpreted.
 *
 * @note This function does not interfere with any other functions, but it is @b
 *       not reentrant since it uses and reuses statically allocated memory.
 */
ShCommand *shParseCmdLine(char cmdLine[]);

/**
 * @brief Prints a textual description of a process's current state to
 *        <tt>stderr</tt>.
 *
 * For instance, the output can look like this:
 * @code
 * [20122] Running   "ls -l"
 * @endcode
 * or like this:
 * @code
 * [20131] Stopped   "sleep 60"
 * @endcode
 *
 * @param info Process-information structure containing the process ID, its
 *             current state and its command line.
 *
 * @return Always 0.
 *
 * @note This function should @b not be used in a signal handler since it uses a
 *       potentially locking output mechanism to write to <tt>stderr</tt>.
 */
int shPrintProcState(const ProcInfo *info);


#endif /* SHELLUTILS_H */
