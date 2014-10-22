
import java.io.*;
import java.net.*;

public class UDPClient {

	public static void main(String[] args) throws Exception {
		
		// Erzeuge Input-Stream:
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		
		// Erzeuge Client-Socket:
		DatagramSocket clientSocket = new DatagramSocket();
		
		// †bersetzung Hostname in IP-Adresse mit DNS:
		InetAddress IPAddress = InetAddress.getByName("192.168.0.21");
		
		// Portnummer (manuell von TS):
		int port = 55761;
		
		// Erzeuge Datenpackete:
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		
		// Einlesen der Nachricht von Konsole:
		String sentence = inFromUser.readLine();
		sendData = sentence.getBytes();
		
		// Erzeuge Datagramm mit zu sendenden Daten, LŠnge, IP-Adresse und Port:
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		
		// Sende Datagramm an Server: 
		clientSocket.send(sendPacket);
		
		// Erzeuge Datagramm mit den empfangenen Daten:
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		
		// Empfange Datagramm vom Server Ÿber den Socket:
		clientSocket.receive(receivePacket);
		
		// Auslesen der Daten aus dem Datagramm:
		String modifiedSentence = new String(receivePacket.getData());
		
		// Ausgabe der modifizierten Daten auf dem Screen des Clients:
		System.out.println("FROM SERVER: " + modifiedSentence);
		
		// Schliessen des Sockets:
		clientSocket.close();
	}
}
