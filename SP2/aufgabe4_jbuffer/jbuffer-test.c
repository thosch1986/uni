#include "jbuffer.h"
#include <errno.h>
#include <pthread.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>


static void die(const char message[]) __attribute__((noreturn));


static void printHeadline(const char headline[]) {
	size_t length = strlen(headline) + 1;
	putchar('\n');
	for (size_t i = 1; i <= length; ++i) {
		putchar('-');
	}
	putchar('\n');
	printf("%s:\n", headline);
	for (size_t i = 1; i <= length; ++i) {
		putchar('-');
	}
	putchar('\n');
}


static void printPut(BNDBUF *buffer, int value) {
	printf("put: %d\n", value);
	bbPut(buffer, value);
}


static void printGet(BNDBUF *buffer, int expected) {
	int value = bbGet(buffer);
	printf("get: %d (expected: %d)\n", value, expected);
	if (value != expected) {
		fputc('\n', stderr);
		fputs("Failed!\n", stderr);
		exit(EXIT_FAILURE);
	}
}


static void die(const char message[]) {
	perror(message);
	exit(EXIT_FAILURE);
}


static void startThread(pthread_t *thread, void *(*function)(void *),
						BNDBUF *buffer) {
	errno = pthread_create(thread, NULL, function, buffer);
	if (errno != 0)
		die("pthread_create()");
}


static void *getBlocking(void *arg) {
	printGet(arg, 20);
	return NULL;
}


static void *addBlocking(void *arg) {
	printPut(arg, 40);
	return NULL;
}


static void joinThread(pthread_t thread) {
	errno = pthread_join(thread, NULL);
	if (errno != 0)
		die("pthread_join()");
}


int main(void) {

	BNDBUF *buffer = bbCreate(5);
	
	printHeadline("Put & get");
	printPut(buffer, 0);
	printGet(buffer, 0);
	
	printHeadline("Fill & empty");
	printPut(buffer, 10);
	printPut(buffer, 11);
	printPut(buffer, 12);
	printPut(buffer, 13);
	printPut(buffer, 14);
	printGet(buffer, 10);
	printGet(buffer, 11);
	printGet(buffer, 12);
	printGet(buffer, 13);
	printGet(buffer, 14);
	
	printHeadline("Get from empty buffer (-> blocking)");
	pthread_t thread;
	startThread(&thread, getBlocking, buffer);
	puts("Sleep for 4 seconds before adding an element... (no deadlock should"
	     " occur)");
	sleep(4);
	printPut(buffer, 20);
	joinThread(thread);
	
	printHeadline("Fill");
	printPut(buffer, 30);
	printPut(buffer, 31);
	printPut(buffer, 32);
	printPut(buffer, 33);
	printPut(buffer, 34);
	
	printHeadline("Add to full buffer (-> blocking)");
	startThread(&thread, addBlocking, buffer);
	puts("Sleep for 4 seconds before removing an element... (no deadlock "
	     "should occur)");
	sleep(4);
	printGet(buffer, 30);
	joinThread(thread);
	
	printHeadline("Empty");
	printGet(buffer, 31);
	printGet(buffer, 32);
	printGet(buffer, 33);
	printGet(buffer, 34);
	printGet(buffer, 40);
	
	putchar('\n');
	puts("All tests successful.");
	putchar('\n');
	puts("Note 1: The tests did not include massively parallel read/write "
	     "accesses.");
	puts("Note 2: Never rely solely on testcases - they cannot replace brain "
	     "work!");
	bbDestroy(buffer);
	return EXIT_SUCCESS;
}
