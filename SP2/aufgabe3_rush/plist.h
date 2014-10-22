/**
 * @file
 * @brief List for bookkeeping of a shell's child processes.
 *
 * Since <a href=http://pubs.opengroup.org/onlinepubs/9699919799/>SuSv4</a>
 * offers no portable way to determine the current state of a process, the shell
 * itself has to keep a record of its child processes and their current state.
 *
 * Newly created processes can be added with plistAdd(), while terminated
 * processes can be removed with plistRemove().
 *
 * Information about a specific process can be obtained by calling plistGet().
 * To iterate over the entire list of child processes, plistIterate() can be
 * used.
 *
 * When a child process has changed its state (which is signaled to the shell
 * via @c SIGCHLD), the shell must update the process entry by calling
 * plistNotifyEvent(). The processes which have recently changed their state can
 * be obtained and processed with plistHandleEvents().
 */

#ifndef PLIST_H
#define PLIST_H


#include "shellutils.h"


/**
 * @brief Adds a new process to the list.
 *
 * The process is assumed to be in the running state.
 *
 * During the insert operation, the passed @c cmdLine is copied to an internally
 * allocated buffer. The caller may free or otherwise reuse the memory occupied
 * by @c cmdLine after return from plistAdd().
 *
 * @param pid     Process ID of the entry that is to be created.
 * @param cmdLine Command line corresponding to the process with ID pid.
 *
 * @return Pointer to the created entry. If an error occurs, @c NULL is returned
 *         and @c errno is set accordingly:
 *         - @c EINVAL: An entry with the given PID already exists in the list.
 *         - @c ENOMEM: A memory allocation has failed.
 *
 * @note This function should @b not be used in a signal handler since it uses
 *       @c malloc() to allocate memory on the heap.
 */
const ProcInfo *plistAdd(pid_t pid, const char cmdLine[], bool background);

/**
 * Returns the process information for a specific PID, or @c NULL if no entry
 * with that PID exists.
 *
 * The data returned by this function will only be valid until the respective
 * entry has been removed from the list. It is the caller's responsibility to
 * copy data that is needed for a longer period of time (if any).
 *
 * @note This function can be safely used in a signal handler provided it does
 *       not overlap any other calls to a plist operation.
 */
const ProcInfo *plistGet(pid_t pid);

/**
 * @brief Removes a specific process from the list.
 *
 * @param pid ID of the process to be removed.
 *
 * @retval  0 Success.
 * @retval -1 No process with the given PID was found in the list.
 *
 * @note This function should @b not be used in a signal handler since it uses
 *       @c free() to release memory to the heap.
 */
int plistRemove(pid_t pid);

/**
 * @brief Invokes a callback function on each element in the list.
 *
 * The callback function is passed the process information of the current list
 * element. It shall return 0 to request further processing of the list. Any
 * other return value will cause the early termination of the list walk.
 *
 * The callback function may remove the current element by calling
 * plistRemove(), but must not modify the process list in any other way.
 *
 * @param callback Pointer to the function to be invoked for each list element.
 *
 * @return 0 if the entire process list was walked, otherwise the value returned
 *         by the last invocation of the callback function.
 *
 * @note This function can be safely used in a signal handler provided it does
 *       not overlap any other calls to a plist operation.
 */
int plistIterate(int (*callback)(const ProcInfo *));

/**
 * @brief Notifies the process list that a process has changed its state.
 *
 * The process's new state is determined by evaluating @c event, which should be
 * retrieved using @c waitpid().
 *
 * A flag is set for the list element to indicate the change.
 * plistHandleEvents() can be used to query the processes whose state-change
 * flag is set.
 *
 * @param pid   ID of the process.
 * @param event Event that changed the process status as retrieved from
 *              @c waitpid().
 *
 * @retval  0 Success.
 * @retval -1 No process with the given PID was found in the list.
 *
 * @note This function can be safely used in a signal handler provided it does
 *       not overlap any other calls to a plist operation.
 */
int plistNotifyEvent(pid_t pid, int event);

/**
 * @brief Invokes a callback function on each element in the list whose state
 *        has changed since the last call to plistHandleEvents().
 *
 * This function only handles processes whose status change has been signaled
 * via plistNotifyEvent(). Newly added processes are not regarded. If a process
 * has changed its state multiple times, only the last change is handled.
 *
 * For each element that is processed, the state-change flag is reset.
 *
 * The callback function is passed the process information of the current list
 * element, along with the event that has caused the state change. It shall
 * return 0 to request further processing of the list. Any other return value
 * will cause the early termination of the list walk.
 *
 * The callback function may remove the current element by calling
 * plistRemove(), but must not modify the process list in any other way.
 *
 * @param callback Pointer to the function to be invoked for each list element.
 * 
 * @return 0 if the entire process list was walked, otherwise the value returned
 *         by the last invocation of the callback function.
 *
 * @note This function can be safely used in a signal handler provided it does
 *       not overlap any other calls to a plist operation.
 */
int plistHandleEvents(int (*callback)(const ProcInfo *, int));


#endif /* PLIST_H */
