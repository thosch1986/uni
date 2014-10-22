
//import java.io.*;
import java.net.*;

public class UDPServer {

	public static void main(String[] args) throws Exception {
		
		DatagramSocket serverSocket = new DatagramSocket(55761);
		
		byte[] receiveData = new byte[1024]; // Grš§e des empfangenen Datenpackets
		byte[] sendData = new byte[1024];	// Grš§e des zu sendenden Datenpackets
		
		while(true) {
			
			// Reserviere Speicher fŸr empfangenes Datagramm
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			
			// empfange Datagramm (mit IPAddresse & Port)
			serverSocket.receive(receivePacket);
			
			int rcvLen = receivePacket.getLength();
			String sentence = new String(receivePacket.getData(), 0, rcvLen);
			
			// Auslesen IP-Adresse des Clients:
			InetAddress IPAddress = receivePacket.getAddress();
			// Auslesen Portnummer des Clients:
			int port = receivePacket.getPort();
			
			// >>>>>>>>> hier passiert die eigentliche Aktion des Servers <<<<<<<<<<<<
			String capitalizedSentence = sentence.toUpperCase();
			
			// Inhalt in Datenpacket verpacken.
			sendData = capitalizedSentence.getBytes();
			
			// Erzeuge Datagramm zum Senden an Client
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			
			// schreibe Datagramm in Socket
			serverSocket.send(sendPacket);	
			
			// Ende der while-Schleife, zurŸckspringen und auf weiteres Datagramm warten!
		}
	}
}
