# targets definieren 
.PHONY : all clean

#target all -> Projekt bauen
all : jbuffer-test

#Semaphor - Implementierung compilieren
sem.o		:	sem.c 		sem.h
	gcc -std=c99 -pedantic -Wall -Werror -D_XOPEN_SOURCE=600 -c -pthread -o sem.o sem.c

#jbuffer - Implementierung compilieren, sem.o einbinden
jbuffer.o 	: 	jbuffer.c jbuffer.h 
	gcc -c -std=c99 -pedantic -Wall -Werror -D_XOPEN_SOURCE=600 -pthread -o jbuffer.o jbuffer.c

#jbuffer - Testprogramm compilieren und mit dem Modulen zum lauffaehigen Programm binden
jbuffer-test:		jbuffer.o sem.o	jbuffer-test.c 
	gcc  -std=c99 -pedantic -Wall -Werror -D_XOPEN_SOURCE=600 -pthread -o jbuffer-test  sem.o jbuffer.o jbuffer-test.c



# clean - Target : alle *.o - Dateien löschen
clean		:
	rm -f sem.o jbuffer.o 

#manueller Compileraufruf:
#gcc -std=c99 -pedantic -Wall -Werror -D_XOPEN_SOURCE=600 -pthread -o jbuffer-test sem.o jbuffer.c jbuffer-test.c 
