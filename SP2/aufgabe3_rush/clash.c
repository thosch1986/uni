#include "plist.h"
#include "shellutils.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>


static void printProcEvent(pid_t pid, const char cmdLine[], int event) {
	fprintf(stderr, "[%d] \"%s\" ", pid, cmdLine);
	if (WIFEXITED(event) != 0)
		fprintf(stderr, "exited with status %d", WEXITSTATUS(event));
	else if (WIFSIGNALED(event) != 0)
		fprintf(stderr, "terminated by signal %d", WTERMSIG(event));
	else if (WIFSTOPPED(event) != 0)
		fprintf(stderr, "stopped by signal %d", WSTOPSIG(event));
	else if (WIFCONTINUED(event) != 0)
		fputs("continued", stderr);
	putc('\n', stderr);
}


static void die(const char message[]) {
	perror(message);
	exit(EXIT_FAILURE);
}


static void changeCwd(char *argv[]) {
	if (argv[1] == NULL || argv[2] != NULL) {
		fprintf(stderr, "Usage: %s <dir>\n", argv[0]);
		return;
	}
	if (chdir(argv[1]) != 0)
		perror(argv[0]); 
}


static void printJobs(char *argv[]) {
	if (argv[1] != NULL) {
		fprintf(stderr, "Usage: %s\n", argv[0]);
		return;
	}
	plistIterate(shPrintProcState);
}


static void execute(ShCommand *cmd) {

	pid_t pid = fork();
	if (pid == -1) 
		die("fork");

	if (pid == 0) { // Child process

		// Execute the desired program
		execvp(cmd->argv[0], cmd->argv);
		die(cmd->argv[0]);

	} else {		// Parent process

		if (cmd->background == true) { // Child runs in background
			const ProcInfo *info = plistAdd(pid, cmd->cmdLine, cmd->background);
			if (info == NULL)
				die("plistAdd");
			shPrintProcState(info);
		} else {					   // Child runs in foreground
			int event;
			if (waitpid(pid, &event, 0) == -1)
				die("waitpid");
			printProcEvent(pid, cmd->cmdLine, event);
		}
	}
}


int main(void) {

	for (;;) {

		// Collect zombie processes
		pid_t pid;
		int   event;
		while ((pid = waitpid(-1, &event, WNOHANG)) > 0) {
			const ProcInfo *info = plistGet(pid);
			if (info == NULL) {
				fprintf(stderr, "Unknown child process %d\n", pid);
				continue;
			}
			printProcEvent(pid, info->cmdLine, event);
			if (plistRemove(pid) != 0)
				fprintf(stderr, "Unknown child process %d\n", pid);
		}

		// Show prompt
		shPrompt();

		// Read command line
		char cmdLine[MAX_CMDLINE_LEN];
		if (fgets(cmdLine, sizeof(cmdLine), stdin) == NULL) {
			if (ferror(stdin) != 0)
				die("fgets");
			break;
		}

		// Parse command line
		ShCommand *cmd = shParseCmdLine(cmdLine);
		if (cmd == NULL) {
			perror("shParseCmdLine");
			continue;
		}

		// Execute command
		if (cmd->parseError != NULL) {
			if (strlen(cmdLine) > 0)
				fprintf(stderr, "Syntax error: %s\n", cmd->parseError);
		} else if (strcmp("cd", cmd->argv[0]) == 0) {
			changeCwd(cmd->argv);
		} else if (strcmp("jobs", cmd->argv[0]) == 0) {
			printJobs(cmd->argv);
		} else {
			execute(cmd);
		}
	}

	putchar('\n');
	return EXIT_SUCCESS;
}
