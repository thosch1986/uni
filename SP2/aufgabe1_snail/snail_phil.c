#include <stdio.h>
#include <stdlib.h>
#include "getargs.h"
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#include <string.h>
#include <unistd.h>
#include <pwd.h>

static void receive_sock(FILE* sockstream, int expecstatuscode) {
       	   // socket auslesen
           char puffer[1024];
	   if(fgets(puffer,1024, sockstream) == NULL ) {
    		if(ferror(sockstream)) {
			perror("Fehler beim auslesen.");
			exit(EXIT_FAILURE);
		}
	   }

	   int value;
	   char *meldung;
	  
	   // statuscode checken
	   value = strtol(puffer,&meldung, 10);
	   if(value == expecstatuscode) {
		fprintf(stdout, "%s", puffer);
	   } else {
		fprintf(stderr,"Statusmeldung und erwarteter Statuscode stimmen nicht ueberein.\n %s", puffer);
		exit(EXIT_FAILURE);
	   }
}

static void send_sock(FILE* sockstream, char *string) {
	fprintf(stdout, "%s", string);
	fprintf(sockstream, "%s", string);
}

static void send_sockconstchar(FILE* sockstream, const char *string) {
	fprintf(stdout, "%s", string);
	fprintf(sockstream, "%s", string);
}

static void send_sockchar(FILE* sockstream, char c) {
        //fprintf(stdout, "%c", c);
	fprintf(sockstream, "%c", c);
}


int main (int argc, char *argv[]) {
    const char *optionVal;
    const char *address;
   
    // Argumente Parsen -----------------------------------------------------
    if(parseArgs(argc, argv) != 0) {
        perror("Fehler beim Parsen der Argumente.");
        exit(EXIT_FAILURE);
    } else {
	// optionVal gibt Wert zurueck, oder NULL wenn Schalter nicht in argv
        if((optionVal = getOptionVal("-s")) == NULL) {
            optionVal = "kein Betreff";
        }
	// so ist natuerlich nur ein Wort als subject anzunehmen... steht doch so in der aufgabe?       
        // oder man schreibts in Anfuerhrungszeichen "Laengerer Betreff"
	if((address = getTrailingArg(0)) == NULL) {
            perror("Keine Emailaddresse angegeben.\n");
            exit(EXIT_FAILURE);
        }
        
      	// printf("optionVal: %s \n", optionVal);
        // printf("address: %s \n", address);
    }
    
    // Bestimmung des eigenen Clientnamen Name@<adresse.de>------------------
    // koennte man zusammen mit hostname-DNS bestimmung auslagern...
    int gai_result;
    struct addrinfo hint, *info, *akt;  // in hint liste info = liste mit addrinfos, akt anfangszeiger

    char clienthostname[1024];
    clienthostname[1023] = '\0';
    gethostname(clienthostname, 1023); //liefert sowas wie "faui0sr0"
  
    memset(&hint, 0, sizeof(hint)); // alles auf 0 setzen
	hint.ai_family = AF_UNSPEC; // entweder ipv4 oder ipv6
	hint.ai_socktype = SOCK_STREAM;
	hint.ai_flags = AI_CANONNAME; 

    if ((gai_result = getaddrinfo(clienthostname, "http", &hint, &info)) != 0) {
	perror("Konnte getaddrinfo nicht auf clienthostname anwenden");   
	exit(EXIT_FAILURE);
    }

    // akt-zeiger auf erstes element von der info-Liste setzten = info
    for(akt = info; akt != NULL; akt = akt->ai_next) {
	if(akt->ai_canonname != NULL) {
		 strcpy(clienthostname, akt->ai_canonname);
	         // printf("Client-Hostname: %s\n", clienthostname);
		 break;
	}      
    }
    if(akt==NULL) {
	perror("Konnte Client-Hostnamen nicht finden");
	exit(EXIT_FAILURE);
    }


    // Absenderadresse ermitteln  <name>@fauiXX.ce.fau.de-------------------
    uid_t userid;
    struct passwd *pwd;    
    char username[1024];  
    username[1023] = '\0';   
    char realid[1024];
    realid[1023] = '\0';
    char *realid2;	

    userid = getuid(); // UserID auslesen
    if((pwd=getpwuid(userid)) == NULL) {
	perror("Konnte Username nicht auslesen.");
	exit(EXIT_FAILURE);
    } else {
  	 strcpy(username, pwd->pw_name);
   	 // printf("Username: %s \n", username);
	 strcpy(realid, pwd->pw_gecos);
         // printf("Real ID: %s \n", realid);
	 // Muesste man mit einem Tokenizer noch trennen von Hauptfach/Nebenfach gequatsche aber dadran sollte es nicht scheitern...
	if((realid2 = strtok(realid, ",")) == NULL) {
		fprintf(stderr, "Konnte den Real Name nicht extrahieren.");
		exit(EXIT_FAILURE);
	}

    }


     // Komplette Absenderadresse zusammensetzten -------------------------
     char absender[1024] = "";
     strcpy(absender, username);
     strcat(absender, "@");
     strcat(absender, clienthostname);
     // printf("Absender-Adresse: %s \n", absender);







    // Eigentliches simail-Programm ----------------------------------------
    // Adresse des hostnames Parsen - ipv4/ipv6 ----------------------------

    char *hostname ="lists.informatik.uni-erlangen.de";
    int gai_ret; // getaddrinfo_return value
    int sock;  // wird Verbindungsnummer des Sockets oder 0 im Fehlerfall.
    struct addrinfo hints;
    struct addrinfo *sa_head;
    struct addrinfo *sa;  // zum abschreiten von socket-adressinfo liste.
  


    memset(&hints, 0, sizeof(hints));
   	 hints.ai_socktype = SOCK_STREAM; // nur TCP-Sockets
   	 hints.ai_family = PF_UNSPEC; // beliebiges ip-Protokoll
	 hints.ai_flags = AI_ADDRCONFIG; // nur lokal verf Addrtypen

    gai_ret = getaddrinfo(hostname, "25", &hints, &sa_head); 
    if(gai_ret != 0) {
	perror("Probleme mit getaddrinfo");
	exit(EXIT_FAILURE);
    }
        
    // Erfolgreiche Abfrage gelungen, *sa_head zeiger auf Anfang einer Socket-Addressinfo Liste.
    // dessen naechste Struktur ist mit ai_next abfragbar. Notw. fuer socket->connect

   /* Liste der Adressen durchtesten */
   for(sa = sa_head; sa!=NULL; sa=sa->ai_next) {
         sock = socket(sa->ai_family,sa->ai_socktype,sa->ai_protocol);
         if(0 == connect(sock, sa->ai_addr, sa->ai_addrlen)) {

		
		// Connection mit Server-Socket geglueckt --------------------------
        	  //      printf("Socketnummer: %d \n", sock);
            	
		FILE *sockstream;
		if((sockstream = fdopen(sock, "a+")) == NULL) {
			perror("Probleme mit fdopen.");
			exit(EXIT_FAILURE);
		}
		

		// printf("Connected \n ----------------------------------\n");
		receive_sock(sockstream, 220);
		send_sock(sockstream, "HELO "); send_sock(sockstream, clienthostname); send_sock(sockstream, "\r\n");
		receive_sock(sockstream, 250);
		send_sock(sockstream, "MAIL FROM: <"); send_sock(sockstream, absender); send_sock(sockstream, ">\r\n");
		receive_sock(sockstream, 250);
		send_sock(sockstream, "RCPT TO: <"); send_sockconstchar(sockstream, address); send_sock(sockstream, ">\r\n");
		receive_sock(sockstream, 250);
		send_sock(sockstream, "DATA\r\n");
		receive_sock(sockstream, 354);
		send_sock(sockstream, "From: "); send_sock(sockstream, realid2);
		send_sock(sockstream, " <"); send_sock(sockstream, absender); send_sock(sockstream, ">\r\n");
		send_sock(sockstream, "To: <");	send_sockconstchar(sockstream, address); send_sock(sockstream, "> \r\n");
		send_sock(sockstream, "Subject: "); send_sockconstchar(sockstream, optionVal); send_sock(sockstream, "\r\n");

		
		send_sock(sockstream, "\r\n"); //hier ist noch ne komische leerzeile
		//fgetc texteingab
		char c;	
		while((c=fgetc(stdin)) != EOF) {
			if(ferror(stdin)) {
				perror("Problem beim Einlesen von der Standarteingabe");
				exit(EXIT_FAILURE);	
			}
	
			send_sockchar(sockstream, c);
		}
		

		send_sock(sockstream, "\r\n.\r\n"); // beendet DATA eingabe
		receive_sock(sockstream, 250);
		send_sock(sockstream, "QUIT\r\n");
		receive_sock(sockstream, 221);

		// close sockstream, wenn alle Arbeit erledigt.
		fclose(sockstream);
		    break;
	 } else {
		perror("Verbindungsprobleme bei connect()");
		exit(EXIT_FAILURE);
         }

	 // Socket schliessen
         close(sock);
   }
   if(sa == NULL) {
	perror("Fehler beim Abschreiten der Liste");
	exit(EXIT_FAILURE);
   }

   freeaddrinfo(sa_head);    
   exit(EXIT_SUCCESS);
	


}
