//import java.io.*;
import java.net.*;

public class UDPServer {
	public static void main(String argv[]) throws Exception
	{
		int clientNumber = -1;
		int oldClientNumber = -1;
		
		DatagramSocket serverSocket = new DatagramSocket(9999);
		byte[] receiveData = new byte[1024];
		while(true){
			
			//Socket connectionSocket = welcomeSocket.accept();
			
			//BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));		
			
			for (int serverNumber = 0; serverNumber < 1000000; serverNumber++) {
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
			
				int rcvLen = receivePacket.getLength();
				oldClientNumber = clientNumber;
				clientNumber = Integer.parseInt(new String(receivePacket.getData(), 0, rcvLen));
				if (oldClientNumber + 1 != clientNumber) {
					System.out.println("recived: " + clientNumber + " expected: " + (oldClientNumber + 1));
				}
			}
		}
	}
}