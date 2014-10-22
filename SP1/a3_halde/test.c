#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "halde.h"

void* printMalloc(size_t count) {
	void* ptr = malloc(count);
	fprintf(stderr, "malloc(%zu) returned %p\n", count, ptr);
	return ptr;
}

int main(int argc, char *argv[]) {

	exit(EXIT_SUCCESS);
}
