import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ThreadPerTaskWebServer {

	public static void main(String[] args) {
		
		try {
			ServerSocket socket = new ServerSocket(80);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true) {
			
			final Socket connection = socket.accept();
			Runnable task = new Runnable() {
				public void run() {
					handleRequest(connection);
				}
			};
			new Thread(task).start();	// Sobald Bearbeitung einer Anfrage in ein Thread-Objekt ausgelagert wurde, wartet der Server auf den naechsten Auftrag!
		}
	}
}
