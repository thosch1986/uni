CC = gcc
CFLAGS = -std=c99 -pedantic -X_OPEN_SOURCE=700 -Wall -Werror
LDFLAGS = -m32
RM = rm -f
SOURCE = clash.c

.PHONY: all clean

all: clash

# $@: Name des Targets, $^: Liste aller Abhaengigkeiten

clash: clash.o plist.o
	$(CC) $(CFLAGS) -o $@ $^

%.o: %.c
	$(CC) $(CFLAGS) -c $^

clean:
	$(RM) plist.o clash.o clash