
package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import tcdIO.Terminal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
/*
 * @author: Kmla Sharma, Shane Moloney and David Hegarty
 * Student ID: 13319349
 */
public class Server extends Node {
	static final int DEFAULT_SRC_PORT = 50000;
	static final int DEFAULT_DST_PORT = 50001;
	static final String DEFAULT_DST_NODE = "localhost";	
	public static final int NODEUPDATEPACKET = 1;
	public static final int RESULTPACKET= 10;
	public static final int WORKERPACKET= 20;
	public static final int HEARTBEAT= 30;
	public static final int REGISTER= 40;
	public static final int STOPWORK= 50;
	public static final int FILEINFO= 100;
	Terminal terminal;
	String currentSearch;// The name currrently beign searched for.
	boolean ItemFound;		//Boolean marking whether or not the searchitem was found
	String[][] FileContents; // Array of the array of strings to send to the nodes
	WorkerNode[] WorkerNodes;
	InetSocketAddress dstAddress =  new InetSocketAddress(DEFAULT_DST_NODE, DEFAULT_DST_PORT);
	boolean [] Running;//Array of booleans that are set to True and are set to FAlse
	//whenever a hearbeat returns saying a node is not running, or a heartbeat fails to be sent
	//Twice in a row.
	int[][] Stats; //2D array of ints representing information on the Nodes the Server is keeping track of.
	// Each element in the row represents a node and  the two columns represent the number of 
	//names searched through and the sections of the code processed e.g. Stats[5][0] stores the 
	// number of names searched through for Node 5, with Stat[5][1] being the sections.
	int NextSection;
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
			if(recieved.type == FILEINFO )
			{
				FileInfoContent contents = ((FileInfoContent)recieved);
				currentSearch = contents.information;
				this.notifyAll();
			}
			else if(recieved.type == HEARTBEAT)
			{
				Running[((Heartbeat)recieved).number()] = true;
				int index = ((Heartbeat)recieved).index();
				Stats[((Heartbeat)recieved).number()][0] = Stats[((Heartbeat)recieved).number()][0]  + index;
				Stats[((Heartbeat)recieved).number()][1] = ((Heartbeat)recieved).sectionsprocessed;
			}
			else if(recieved.type == REGISTER)
			{
				if(NextSection < 20)
				{
					int Node = ((Register)recieved).nodeNumber();
					SendWork(WorkerNodes[Node], FileContents[NextSection]);
					NextSection ++;
				}
			}
			else if(recieved.type == RESULTPACKET)
			{
				//Not Entirely sure what address should be sending each packet to
				DatagramPacket stop = new StopWork(false).toDatagramPacket();
				for(int i = 0; i < 10; i ++)
				{
					stop.setSocketAddress(packet.getSocketAddress());
					socket.send(stop);
				}
				packet.setSocketAddress(dstAddress); //Should send the packet to Client
				socket.send(packet);
			}
		}
		catch(Exception e) {e.printStackTrace();}

	}


	public synchronized void start() throws Exception 
	{
		organiseFile();
		terminal.println("Waiting for contact");
		this.wait();

	}

	public void organiseFile()
	{
		String fname= "names-short.txt";

		String line = "";
		long counter;
		File file;
		FileInputStream fin;
		BufferedReader in;
		//FileOutputStream fout;
		FileOutputStream fos = null;
		File filer;
		BufferedWriter out;

		try {
			file= new File(fname);
			//System.out.println("File length: " + file.length());
			fin= new FileInputStream(file);
			in= new BufferedReader(new InputStreamReader(fin));
			fos = new FileOutputStream("zero.txt");
			FileContents = new String[20][((int) (file.length())/20)];
			counter= 0;
			byte[] section;
			line= in.readLine() + "\n";
			while((line != null))
			{
				//				for(int i=0; i<filenames.length; i++)
				//				{
				int array = 0;
				int index = 0;
				while(counter < 100 && ((line != null)))
				{
					section = line.getBytes();
					System.out.println(counter + " : " + line);
					fos.write(section);
					counter++;
					line= in.readLine() + "\n";
					FileContents[array][index] =line;
					if(index >= FileContents.length)
					{
						index = 0;					//Move to next section when one is full
						array ++;
					}
					index ++;
				}
				//counter=0;
				//}

				//now send this file to worker node
				//resume loop
				//counter=0;
				WorkerNodes = new WorkerNode[10];
				Stats = new int[10][2];

				for(int i = 0; i < 10; i ++)
				{
					WorkerNodes[i] = new WorkerNode(DEFAULT_DST_NODE,  DEFAULT_DST_PORT,  DEFAULT_SRC_PORT, i);
				}
				//System.out.println(counter + ": " + line);
				//if (((counter++)%100)==0) { wait(1000);}

				//				
			}
			//System.out.println("hola");
			in.close();
			fin.close();
			fos.flush();

		}
		catch(Exception e) {e.printStackTrace();}

	}
	public void SendWork(WorkerNode Current, String[] Section) throws IOException
	{
		//Not sure what address the packets are suppose to be sent to for each node
		WorkerPacket Search = new WorkerPacket(Section,currentSearch);
		DatagramPacket packet = Search.toDatagramPacket();
		packet.setSocketAddress(dstAddress);
		socket.send(packet);
	}
	public void timer(int time)
	{
		//Implement a timer class
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

