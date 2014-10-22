import java.io.*;
import java.net.*;

public class TCPClient {
	public static void main(String argv[]) throws Exception
	{
		int clientNumber;
				
		Socket clientSocket = new Socket("192.168.0.21", 9999);
		
		
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		
		for (clientNumber = 0; clientNumber < 1000000; clientNumber++) {
			outToServer.writeBytes(Integer.toString(clientNumber) + '\n');
		
		
		
			
		}		
		clientSocket.close();
	}
}