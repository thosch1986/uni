import java.io.*;
import java.net.*;

public class TCPServer {
	public static void main(String argv[]) throws Exception
	{
		int clientNumber = -1;
		int oldClientNumber = -1;
		
		ServerSocket welcomeSocket = new ServerSocket(9999);
		
		while(true){
			
			Socket connectionSocket = welcomeSocket.accept();
			
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
						
			for (int serverNumber = 0; serverNumber < 1000000; serverNumber++) {
				
				
								
				
				oldClientNumber = clientNumber;
				clientNumber = Integer.parseInt(inFromClient.readLine());
				if (oldClientNumber + 1 != clientNumber) {
					System.out.println("recived: " + clientNumber + " expected: " + (oldClientNumber + 1));
				}
			}
		}
	}
}