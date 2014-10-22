
import java.io.*;
import java.net.*;

public class TCPClient {

	public static void main(String[] args) throws Exception {
		
		String sentence;
		String modifiedSentence;
		
		// erzeuge Eingabestrom für Standardeingabe des Clients:
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		// erzeuge Client-Socket, verbinde mit Server:
		Socket clientSocket = new Socket("localhost", 6789);
		
		// erzeuge Ausgabestrom für Socket:
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		
		// erzeuge Eingabestrom für Socket:
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		
		// Fixiere Usereingabe:
		sentence = inFromUser.readLine();
		
		// schreibe Zeile in Socket, die zum Server gesendet wird:
		outToServer.writeBytes(sentence + '\n');
		
		// Lese Zeile aus Socket, was vom Server gesendet wurde:
		modifiedSentence = inFromServer.readLine();
		
		System.out.println("FROM SERVER: " + modifiedSentence);
		
		clientSocket.close();
	}
}
