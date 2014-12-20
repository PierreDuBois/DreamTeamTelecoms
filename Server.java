package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import tcdIO.Terminal;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.SocketAddress;
/*
 * @author: Kmla Sharma, Shane Moloney and David Hegarty
 * Student ID: 13319349
 */
public class Server extends Node {
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final int HEART_TIMEOUT = 3000;
	static final String DEFAULT_DST_NODE = "localhost";
	public static final int DATABASE_SIZE = 10000;
	public static final int DIVISION = (DATABASE_SIZE / 20);
	Terminal terminal;
	boolean startWork = false;
	String currentSearch;// The name currrently beign searched for.
	boolean ItemFound;		//Boolean marking whether or not the searchitem was found
	String[][] FileContents; // Array of the array of strings to send to the nodes
	boolean[][] sent = new boolean[2][20];
	InetSocketAddress dstAddress =  new InetSocketAddress(DEFAULT_DST_NODE, DEFAULT_SRC_PORT);
	boolean [] Running = new boolean[5];//Array of booleans that are set to True and are set to FAlse
	//whenever a hearbeat returns saying a node is not running, or a heartbeat fails to be sent
	//Twice in a row.
	HeartbeatTracker[] heartbeats = new HeartbeatTracker[10];
	int[][] Stats; //2D array of ints representing information on the Nodes the Server is keeping track of.
	// Each element in the row represents a node and  the two columns represent the number of 
	//names searched through and the sections of the code processed e.g. Stats[5][0] stores the 
	// number of names searched through for Node 5, with Stat[5][1] being the sections.
	int NextSection;
	DatagramPacket address[] = new DatagramPacket[5];
	/*
	 * 
	 */
	Server(Terminal terminal, int port) 
	{
		try {
			this.terminal= terminal;
			socket= new DatagramSocket(port);
			listener.go();
		}
		catch(java.lang.Exception e) {e.printStackTrace();}
	}

	/**
	 * Assume that incoming packets contain a String and print the string.
	 * Should be able to tell difference between the Client sending a string to search for,
	 * and a worker node sending an update. 
	 */
	public synchronized void onReceipt(DatagramPacket packet) {
		try {
			PacketContent recieved = PacketContent.fromDatagramPacket(packet);
			if(recieved.type == PacketContent.FILEINFO )
			{
				FileInfoContent contents = ((FileInfoContent)recieved);
				currentSearch = contents.information;
				startWork = true;
			}
			else if(recieved.type == PacketContent.HEARTBEAT)
			{
				int node = ((Heartbeat)recieved).number();
				terminal.println("Heartbeat received from worker: " + node);
				Running[node] = true;
				int index = ((Heartbeat)recieved).index();
				Stats[node][0] = Stats[node][0]  + index;
				Stats[node][1] = ((Heartbeat)recieved).sectionsprocessed;
				heartbeats[node].clearTimer();
				heartbeats[node].resetTimer();
				heartbeats[node].timerTask();
			}
			else if(recieved.type == PacketContent.REGISTER)
			{
				int node = ((Register)recieved).nodeNumber();
				terminal.println("Worker " + node + " is ready to work!");
				isMissing(packet, node);
				if(NextSection < 20 && startWork)
				{
					getNextSection();
					sendWork(packet.getSocketAddress(), FileContents[NextSection]);
					heartbeats[node].clearTimer();
					heartbeats[node].resetTimer();
					heartbeats[node].timerTask();
					sent[1][heartbeats[node].getSection()] = true;
					heartbeats[node].setSection(NextSection);
				}
				Running[node] = true;
				address[node] = packet;
			}
			else if(recieved.type == PacketContent.RESULTPACKET)
			{
				int node = ((Register)recieved).nodeNumber();
				endWork(packet, node);
			}
		}
		catch(Exception e) {e.printStackTrace();}
	}
	
	public void isMissing(DatagramPacket packet, int nodeNumber)
	{
		int count = 0;
		for(int i = 0; i < sent.length; i++)
		{
			if(sent[0][i] && sent[1][i])
				count++;
		}
		if(count == sent.length)
			endWork(packet, nodeNumber);
	}
	
	public void endWork(DatagramPacket packet, int nodeNumber)
	{
		try {
			startWork = false;
			PacketContent recieved = PacketContent.fromDatagramPacket(packet);
			DatagramPacket stop = new StopWork(false).toDatagramPacket();
			for(int i = 0; i < 5; i ++)
			{
				if(address[i] != null)
				{
					stop.setSocketAddress(address[i].getSocketAddress());
					socket.send(stop);
				}
			}
			String result;
			if(recieved.getType() == PacketContent.RESULTPACKET)
				result = "Name was found at line " + (((ResultPacket)recieved).getLineNumber() + (heartbeats[((ResultPacket)recieved).getID()].getSection() * DIVISION)) + ".";
			else
				result = "Name not found.";
			DatagramPacket clientPacket = new FileInfoContent(result).toDatagramPacket();
			clientPacket.setSocketAddress(dstAddress); //Should send the packet to Client
			socket.send(clientPacket);
			NextSection = 20;
			this.notify();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getNextSection()
	{
		for(int i=0; i<Running.length; i++)
		{
			if(!Running[i])
			{
				sent[0][heartbeats[i].getSection()] = false;
				sent[1][heartbeats[i].getSection()] = false;
			}
		}
		for(int i=0; i<sent.length; i++)
		{
			if(!sent[0][i])
			{
				NextSection = i;
				sent[0][i] = true;
				return;
			}
		}
	}
		


	public synchronized void start() throws Exception 
	{
		for(int i = 0; i < 5; i++)
			heartbeats[i] = new HeartbeatTracker(this, i, HEART_TIMEOUT, 19);
		organiseFile();
		terminal.println("Waiting for contact");
		this.wait();
	}

	public void organiseFile()
	{
		String fname= "names-short.txt";

		String line = " ";
		long counter;
		File file;
		FileInputStream fin;
		BufferedReader in;

		try {
			file= new File(fname);
			fin= new FileInputStream(file);
			in= new BufferedReader(new InputStreamReader(fin));
			FileContents = new String[20][DIVISION];
			counter= 0;
			int array = 0;
			Stats = new int[5][2];
			while(line != null && array < 20)
			{
				int index = 0;
				counter=0;
				while(counter < DIVISION && line != null)
				{
					line= in.readLine();
					FileContents[array][index] =line;
					index++;
					counter++;
				}
				array++;
			}
			in.close();
			fin.close();
		}
		catch(Exception e) {e.printStackTrace();}

	}
	public void sendWork(SocketAddress current, String[] Section) throws IOException
	{
		WorkerPacket Search = new WorkerPacket(Section,currentSearch);
		DatagramPacket packet = Search.toDatagramPacket();
		packet.setSocketAddress(current);
		socket.send(packet);
	}

	public static void main(String[] args) 
	{
		try {					
			Terminal terminal= new Terminal("Server");
			(new Server(terminal, DEFAULT_DST_PORT)).start();
			terminal.println("Program completed");
		} catch(java.lang.Exception e) {e.printStackTrace();}
	}
}
