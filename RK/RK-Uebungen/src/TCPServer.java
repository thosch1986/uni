
import java.io.*;
import java.net.*;

public class TCPServer {

	public static void main(String[] args) throws Exception {
		
		String clientSentence = null;
		String capitalizedSentence;
		
		// erzeuge WelcomeSocket für Port 6789:
		ServerSocket welcomeSocket = new ServerSocket(6789);
		
		while(true) {
			
			// blockiert, erzeuge Socket bei Verbindungswunsch von Client:
			Socket connectionSocket = welcomeSocket.accept();
			
			// erzeuge Eingabestrom fuer Socket vom Client:
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			
			// erzeuge Ausgabestrom fuer Socket:
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			
			// lese Zeile aus Socket, wurde vom Client geschickt:
			capitalizedSentence = clientSentence.toUpperCase() + '\n';
			
			// schreibe Zeile in Socket, wird zu Client gesendet:
			outToClient.writeBytes(capitalizedSentence);
		}
	}
}
