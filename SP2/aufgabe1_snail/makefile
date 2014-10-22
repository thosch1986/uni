CC = gcc
RM		= rm -f -r
CFLAGS	= -std=c99 -pedantic -D_XOPEN_SOURCE=700 -Wall -Werror

.PHONY: all clean

all: clean snail

clean:
	$(RM) snail.o snail

snail: snail.o
	$(CC) $(CFLAGS) -o snail snail.o

snail.o: snail.c
	$(CC) $(CFLAGS) -c snail.c