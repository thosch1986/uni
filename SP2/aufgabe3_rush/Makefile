CC      = gcc
CFLAGS  = -std=c99 -pedantic -D_XOPEN_SOURCE=700 -Wall -Werror
RM      = rm -f
SRC     = rush.c
SOBJ    = $(SRC:%.c=%.o)
OBJ     = $(SOBJ) plist.o shellutils.o

.PHONY: all clean

all: rush

clean:
	$(RM) $(OBJ)

rush: $(OBJ)
	$(CC) $(CFLAGS) -o $@ $^

$(SOBJ): $(SRC)
	$(CC) $(CFLAGS) -c $^
