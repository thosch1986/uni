//import java.io.*;
import java.net.*;

public class UDPClient {
	public static void main(String argv[]) throws Exception
	{
		int clientNumber;
				
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("192.168.0.21");
		
		//DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		byte[] sendData = new byte[1024];
		for (clientNumber = 0; clientNumber < 1000000; clientNumber++) {
			//outToServer.writeBytes(Integer.toString(clientNumber) + '\n');
			sendData = Integer.toString(clientNumber).getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9999);
			clientSocket.send(sendPacket);
			Thread.sleep(1);
		}
		clientSocket.close();
	}
}
