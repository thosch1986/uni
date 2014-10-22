#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <limits.h>

#ifndef ARG_MAX
#define ARG_MAX sysconf(_SC_LINE_MAX)
#endif

//static void collectBGProg();
//static void initHandler();

int main(int argc, char *argv[]) {
	
	char commandLine[ARG_MAX];
	COMMAND *cmd;
	struct sigaction mySignal;
	struct sigaction mySignal2;
	
	sigemptyset(&mySignal.sa_mask);
	mySignal.sa_handler = collectBGProg;
	mySignal.sa_flags = SA_NOCLDSTOP | SA_RESTART;
	// Bei Signal "SIGCHLD", benutze Signal-Handler mySignl
	if (sigaction(SIGCHLD, &mySignal, NULL) < 0) {
		perror("Fehler bei Sigaction.");
		exit(EXIT_FAILURE);
	}
	
	sigemptyset(&mySignal2.sa_mask);
	mySignal.sa_handler = intHandler;
	mySignal2.sa_flags = SA_RESTART;
	if (sigaction(SIGINT, &mySignal2, NULL) < 0) {
		perror("Fehler bei Sigaction.");
		exit(EXIT_FAILURE);
	}
	
	while (1) {
		// Clash sammelt 
		//prompt(); // ?
		
		/* get command */
		if(fgets(commandLine, ARG_MAX, stdin) < 0) {
			if(feof(stdin)) {
				putchar('\n');
				exit(EXIT_SUCCESS);
			} else {
				perror("fgets");
				exit(EXIT_FAILURE);
			}
		}
		
		// Einlesen von Kommandozeile
		cmd = parseCommandLine(commandLine);
		if (cmd == NULL) {		// Fehler
			perror("parseCommandLine");
			continue; // noch mal probieren
		} else if (cmd->parseError != NULL) { // Syntax error
			fprintf(stderr, "Syntax error: %s\n", cmd->parseError);
		} else if (cmd->argv[0] == NULL) { // wenn gar nichts eingegeben...
			// ... mache nichts
		} else if (strcmp("cd", cmd->argv[0]) == 0) { // cd "angemeldet"
			if (cmd->argv[1] == NULL || cmd->argv[2] != NULL) {
				fprintf(stderr, "cd: exactly one argument needed\n");
			} else if (chdir(cmd->argv[1])) {
				perror("cd")
			}
		} else if (strcmp("jobs", cmd->argv[0]) == 0) { // jobs "angemeldet"
			printJobs();
		} else {
			execute(cmd);
		}
		
		free(cmd);
	}
}


static void execute(COMMAND *cmd) {
	int fd;				// Filedeskriptor
	pid_t pid;			// PID
	int status;			// status
	sigset_t set;		// Set
	sigset_t oldset;	// Old-Set
	
	if(cmd->outFile != NULL) {
		mode_t mode = S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH;
		if((fd = open(cmd->outFile, O_WRONLY | O_CREAT | O_TRUNC, mode)) == -1) {
			perror("Probleme bei open")
			exit(EXIT_FAILURE);
		}
		
		if((dup2(fd, STDOUT_FILENO)) == -1) {
			perror("Konnte dup2 nicht anwenden.");
			exit(EXIT_FAILURE);
		}
	}
	
	sigemptyset(&set);
	sigaddset(&set, SIGCHLD);
	if (sigprocmask(SIG_BLOCK, &set, &oldset) != 0) {
		perror("sigprocmask fail");
		exit(EXIT_FAILURE);
	}
	
	switch (pid = fork()) {
		case -1: // Fehler
			perror("fork failed");
			exit(EXIT_FAILURE);
		case 0:	// im Kind
			if(sigprocmask(SIG_SETMASK, &oldset, NULL) != 0) {
				perror("sigprocmask failed");
				exit(EXIT_FAILURE);
			}
			
			if (cmd->background == 1) {
				struct sigaction intActBack;
				sigemptyset(&intActBack.sa_mask);
				intActBack.sa_handler = SIG_IGN; // ignoriere
				intActBack.sa_flags = SA_RESTART;
				if (sigaction(SIGINT, &intActBack, NULL) != 0) {
					perror("Fehler bei Sigaction.");
					exit(EXIT_FAILURE);
				}
			}
			
			execvp(cmd->argv[0], cmd->argv);
			perror(cmd->argv[0]);
			exit(EXIT_FAILURE);
		// kein Vater
	}
	/* don't wait for background process */
	if (cmd->background) { // hintergr
		if (insertElement (pid, cmd->cmdLine) < 0) {
			perror ("insertElement");
			exit (EXIT_FAILURE);
		}
	
		if(sigprocmask(SIG_SETMASK, &oldset, NULL) != 0) {
			perror("sigprocmask fail"); //auch wenns quatsch ist...
			exit(EXIT_FAILURE);	
		}

		return;
	}

	//vordergr
	vorderGr = pid;

	// unblock
  	if(sigprocmask(SIG_SETMASK, &oldset, NULL) != 0) {
		perror("sigprocmask fail"); //auch wenns quatsch ist...
		exit(EXIT_FAILURE);	
	}



	// do wait for foreground process 

	// sigsuspend
	
	sigset_t toblock;
	sigset_t saved;

	sigemptyset(&toblock);
	sigaddset(&toblock, SIGCHLD);
	if(sigprocmask(SIG_BLOCK, &toblock, &saved)) {
		perror("sigproc fail");
		exit(EXIT_FAILURE);
	}

	while(event == 0) {
		sigsuspend(&saved);
	}

	event = 0;

	if(sigprocmask(SIG_SETMASK, &saved, NULL)) {
		perror("sigproc fail");
		exit(EXIT_FAILURE);
	}


	/*if (waitpid (pid, &status, 0) == -1) {
		perror ("wait for foreground process");
		exit (EXIT_FAILURE);
	}
	*/
	printStat (cmd->cmdLine, status);
	if(fd > 0 ) {
	  if(close(fd) == -1) {
	    perror("Problems to close");
	    exit(EXIT_FAILURE);
	  }
	}
}

/* Helfer für jobs-command */
static int printJob(pid_t pid, const char *cl) {
	fprintf(stderr, "[%d] %s\n", pid, cl);
	return 0;
}

static void printJobs() {
	walkList(printJob);
}

// Sammeln beendeter Kinder und Print Stati
static void collectBGProc() {
	// Kontextsicherung errno
	int errno_tmp = errno;
	pid_t bg_pid;
	int bg_status;
	
	while ((bg_pid = waitpid(-1, &bg_status, WNOHANG)) > 0) {
		char cmdLine[ARG_MAX];
		
		if (bg_pid == vorderGr) {
			event = 1;
			continue;
		}
		
		if (removeElement(bg_pid, cmdline, ARG_MAX) < 0) {
			fprintf(stderr, "old child %d\n", (int) bg_pid);
		} else {
			printStat(cmdline, bg_status);
		}
	}
	errno = errno_tmp;
}

static void intHandler(void) {
	fprintf(stderr, "Interrupt!\n");
}