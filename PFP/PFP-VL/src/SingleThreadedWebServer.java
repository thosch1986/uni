import java.net.ServerSocket;
import java.net.Socket;


public class SingleThreadedWebServer {

	public static void main(String[] args) throws Exception {
		
		ServerSocket socket = new ServerSocket(80);	// Anfrage = Arbeitspaket
		
		while(true) {
			
			Socket connection = socket.accept();
			handleRequest(connection);
		}
	}

	private static void handleRequest(Socket connection) {
		// TODO Auto-generated method stub
		
	}
}
