#include <dirent.h>
#include <errno.h>
#include <fnmatch.h>
#include <libgen.h>
#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>

/* Kommandozeile: max. Laenge */
#define MYLINE sysconf(_SC_LINE_MAX)
/* Pfad: max. Laenge */
#define MYPATH pathconf(".", _PC_PATH_MAX)	

/* "Inhaltsverzeichnis": Funktionen bekannt machen */
static int parseArguments(int argc, char *argv[]);  /* Anzahl Argumente, Pruefung auf vorhandensein */
static long int strtolParser(int, char *argv[]);     /* ggf. Aufruf von strtolParser() */
static void search(int i, char *path);              /* Rekursive Suche */
static void nullCheck(char **argv, int i);          /* befindet sich Wert hinter Argument ?*/
static int patternCheck(char *path, char *arguments_name);
static void checkAndPrint(char *path, struct stat);

/* Globale Variablen */
static int   arguments_maxdepth = -1;
static char *arguments_name;		/* String, der fnmatch uebergeben wird */
static int   arguments_name_isSet = 0;
static char  arguments_type = 'x';	/* X als Standardwert */
static int   arguments_size = -1;		/* Standardwert: -1 */
static int   arguments_size_prefix = 4;	/* 1: >; 2: <; 3: ==; 4: standard */

int main(int argc, char *argv[]) {
	if ((MYLINE == -1) || (MYPATH == -1)) {
		fprintf(stderr, "%s\n", "sysconf");
		exit(EXIT_FAILURE);
	}
	
	int pathCounter = parseArguments(argc, argv);
	/* Verarbeiten der Argumente */
	for (int i=1; i < argc; i++) {
		search(arguments_maxdepth, argv[i+1]);	/* <<<<<< HIER SUCH-AUFRUF!!!!! */
	}
	exit(EXIT_SUCCESS);
}


static int parseArguments(int argc, char *argv[]) {
	int pathCounter = 0;
	
	for (int i=1; i < argc; i++) {
		/* ----- Ueberpruefung auf Parameter ----- */
		if (strcmp(argv[i], "-maxdepth") == 0) {
			nullCheck(argv, i);		/* TODO: Pruefen, ob Wert dahinter vorhanden */
			arguments_maxdepth = strtolParser(i+1, argv); /* ToDo: Warum strtol??? */			i++;
		} else if (strcmp(argv[i], "-name") == 0) {
			nullCheck(argv, i);
			arguments_name = argv[i+1];
			arguments_name_isSet = 1;
			i++;
		} else if (strcmp(argv[i], "-type") == 0) {
			nullCheck(argv, i);
			/* Ueberpruefung auf Verzeichnisse (d) oder reg. Dateien (f) */
			if((strcmp(argv[i+1], "d") != 0) && (strcmp(argv[i+1], "f"))) {
				fprintf(stderr, "%s\n", "Invalid value");
				exit(EXIT_FAILURE);
			}
			arguments_type = argv[i+1][0];
			i++;
		} else if (strcmp(argv[i], "-size") == 0) {
			/* Prefix-Test */
			if(argv[i+1][0] == '+') {
				argv[i+1]++;	// argv sollte nicht veraendert werden
				nullCheck(argv, i);
				arguments_size = strtolParser(i+1, argv); /* strtolParser */
				arguments_size_prefix = 1;
			} else if (argv[i+1][0] == '-') {
				argv[i+1]++;	// argv sollte nicht veraendert werden
				nullCheck(argv, i);
				arguments_size = strtolParser(i+1, argv);
				arguments_size_prefix = 2;
			} else {
				nullCheck(argv, i);
				arguments_size = strtolParser(i+1, argv);
				arguments_size_prefix = 3;
			}
			i++;
		}
	}
	return pathCounter;
}

static long int strtolParser (int i, char *argv[]) {
	char *endPtr;
	char *nptr = argv[i];
	long int value;
	errno = 0;
	value = strtol(nptr, &endPtr, 10);		/* Basis: 10, dann Fehlerbehandlung lt. manpage */
	if (((errno = ERANGE) && (value == LONG_MAX || value == LONG_MIN)) || (errno = EINVAL && value == 0)) {
		fprintf(stderr, "%s\n", "strtol");
		exit(EXIT_FAILURE);
	}
	
	if (endPtr == nptr) {
		fprintf(stderr, "%s\n", "no digits were found");
		exit(EXIT_FAILURE);
	}
	if (value < 0) {	/* keine negativen Werte zulassen */
		fprintf(stderr, "%s\n", "negative value");
		exit(EXIT_FAILURE);
	}
	if (value > INT_MAX) {
		fprintf(stderr, "%s\n", "value higher than INT_MAX");
		exit(EXIT_FAILURE);
	}
	return value;
}

static void search(int i, char *path) {
	errno = 0;
	struct stat buf;
	if (lstat(path, &buf) == -1) {		/* Statusausgabe Verzeichnis */
		if(errno == ENOENT) {
			perror("no such file or directory. Try again");
			return;
		} else if (errno == EACCES) {
			perror("permission denied. Try again");
		} else {
			//fprintf(stderr, "%s", path);
			perror("lstat 11");			/* <<<<< BAD-ADDRESS */
			exit(EXIT_FAILURE);
		}
	}
	
	checkAndPrint(path, buf);		/* ToDO: nachvollziehen */
	
	if(i == 0) {
		return;
	}
	if(!(S_ISDIR(buf.st_mode)))	 {	// Wenn kein Verzeichnis, dann NICHT weitermachen
		return;
	}
	
	errno = 0;
	DIR *entry = opendir(path);		// errno wird immer gesetzt!
	if((entry == NULL) && (errno != 0)) {
		perror("opendir");
		return;
	}
	
	struct dirent *dirEntry;	/* Container fuer gelesenen Verzeichnisinhalt */
	while ((errno = 0) && ((dirEntry = readdir(entry)) != NULL)) {
		if (strcmp(dirEntry->d_name, ".") || strcmp(dirEntry->d_name, "..")) {
			continue; 		/* Ignorieren von "." und ".." via continue */
		}
		/* Neuer Pfad: alter Pfad + Verzeichnisinhalt	 */
		char newPath[strlen(dirEntry->d_name) + strlen(path) + 2];
		strcpy(newPath, path);
		if((newPath[strlen(newPath) -1] != '/') && (strcmp("/", newPath) != 0)) {
			strcat(newPath, "/");
		}
		/* gefundenes Verzeichnis an newPath haengen */
		strncat(newPath, dirEntry->d_name, strlen(dirEntry->d_name));
		
		errno = 0; 
		if(lstat(newPath, &buf) == -1) {
			if(errno == ENOENT) {
				perror("no such file or directory. Try again");
				return;
			} else if (errno == EACCES) {
				perror("permission denied. Try again");
			} else {
				perror("lstat 22");
				exit(EXIT_FAILURE);
			}
		}
		
		/* Rekursive Suche mit neuem Pfad */
		if (S_ISDIR(buf.st_mode) || (S_ISREG(buf.st_mode))) {
			if (i > 0) {
				search(i-1, newPath);
			}
			if (i == -1) {
				search (-1, newPath);
			}
		} else {
			continue;
		}
		
		if(errno != 0) {
			perror("readdir");
		}
		
		if(closedir(entry) == -1) {
			perror("closedir");
			exit(EXIT_FAILURE);
		}
	}
}

static void nullCheck(char **argv, int i) {
	/* Argument, aber kein Wert eingegeben */
	if ((argv[i+1] == NULL) || (argv[i+1][0])) {
		fprintf(stderr, "no value after argument\n");
		exit(EXIT_FAILURE);
	}
}

static int patternCheck(char *path, char *arguments_name) {
	int x = fnmatch(arguments_name, basename(path), FNM_PERIOD);
	if (x == FNM_NOMATCH) {
		return(0);
	}
	if (x != 0) {
		fprintf(stderr, "fnmatch");
		return(EXIT_FAILURE);
	}
	return(1);
}

static void checkAndPrint(char *path, struct stat buf) {
	
	if (arguments_size >= 0) {
		if((arguments_size_prefix == 1) && (buf.st_size <= arguments_size)) {
			return;
		}
		if((arguments_size_prefix == 2) && (buf.st_size <= arguments_size)) {
			return;
		}
		if((arguments_size_prefix == 3) && (buf.st_size <= arguments_size)) {
			return;
		}
		if((arguments_name_isSet == 1) && (patternCheck(path, arguments_name) == 0)) {
			return;
		}
	}
	
	if((arguments_type == 'd') && (S_ISREG(buf.st_mode))) {
		return;
	}
	if((arguments_type == 'f') && (S_ISDIR(buf.st_mode))) {
		return;
	}
	puts(path);
}
