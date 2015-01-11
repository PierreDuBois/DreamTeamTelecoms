package cs.tcd.ie;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import tcdIO.*;

/**
 *
 * Client class
 * 
 * 
 * An instance accepts user input 
 *
 */

/*
 * @author: Kmla Sharma
 */

public class Client extends Node {
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";	
	//terminal from tcdlib.jar
	Terminal terminal; //terminal is Joptionpane dialog box
	InetSocketAddress dstAddress;
	
	/**
	 * Constructor
	 * 	 
	 * Attempts to create socket at given port and create an InetSocketAddress for the destinations
	 * Socket is one endpoint of a 2 way communication link, TCP layer
	 */
	Client(Terminal terminal, String dstHost, int dstPort, int srcPort) {
		try {
			this.terminal= terminal;
			dstAddress= new InetSocketAddress(dstHost, dstPort);
			socket= new DatagramSocket(srcPort);
			listener.go();
			
		}
		catch(java.lang.Exception e) 
		{
			e.printStackTrace();
		}
	}

	
	
	public synchronized void onReceipt(DatagramPacket packet) 
	{
		PacketContent ack = PacketContent.fromDatagramPacket(packet);
		terminal.println(ack.toString());
		this.notify();
	}

	
	/**
	 * Sender Method
	 * 
	 */
	public synchronized void start() throws Exception 
	{
		DatagramPacket packet= null;
		String name; //read in from user input
		SendName packetToSend;
		name = terminal.readString("Name to find: ");
		packetToSend = new SendName(name); 
		packet = packetToSend.toDatagramPacket(); //convert fileinfo into a packet
		packet.setSocketAddress(dstAddress);
		socket.send(packet);
		terminal.println("Sent: " + name);
		this.wait();
	}
	/**
	 * Test method
	 * 
	 * Sends a packet to a given address
	 */
	public static void main(String[] args) {
		try {					
			Terminal terminal= new Terminal("Client");		
			(new Client(terminal, DEFAULT_DST_NODE, DEFAULT_DST_PORT, DEFAULT_SRC_PORT)).start();
			terminal.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
