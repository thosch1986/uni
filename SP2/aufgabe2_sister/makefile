CC      = gcc
RM      = rm -f -r
CFLAGS  = -std=c99 -pedantic -D_XOPEN_SOURCE=700 -Wall -Werror
BIN		= sister
SRC     = connection-fork.c request-http.c sister.c
OBJ		= $(SRC:%.c=%.o)

.PHONY: all clean

clean: $(BIN) $(OBJ)
	$(RM) $^

all: $(BIN)
	
sister: $(BIN) $(OBJ)
	$(CC) $(CFLAGS) -o $@ $^

%.o: %.c
	$(CC) $(CFLAGS) -c $^
