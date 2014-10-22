CC		= gcc
CFLAGS	= -std=c99 -pedantic -D_XOPEN_SOURCE=700 -Wall -Werror -o
LDFLAGS = -m32
RM		= rm -f
WSORTPATH = /proj/i4sp1/an61itip/trunk/aufgabe2/wsort.c

# $<	die erste Abhängigkeit
# $@	Name des Targets
# $+	Liste aller Abhängigkeiten
# $^	Liste aller Abhängigkeiten, wobei doppelt vorkommende Abhängigkeiten eleminiert werden.


.PHONY: all clean

all: wsort

clean:
	$(RM) halde.o wsort.o wsort wsort.c


# wsort mit halde kompilieren. Kopieren falls nicht vorhanden:
wsort: wsort.o halde.o
	$(CC) $(CFLAGS) $(LDFLAGS) -o $@ $^
	
wsort.c:
	cp $(WSORTPATH) .
	
	
# aus allen *.c-Dateien *.o-Dateien machen
.c.o:
	$(CC) -c $(CFLAGS) $<