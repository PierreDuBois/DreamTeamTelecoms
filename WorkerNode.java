package cs.tcd.ie;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.net.InetSocketAddress;
import javax.swing.JOptionPane;


public class WorkerNode extends Node {
	static final int DST_PORT = 50001;
	static final int SRC_PORT = 60000;
	static final String DEFAULT_DST_NODE = "localhost";
	
	int portNum;
	String[] list;
	String search;
	int entriesProcessed, sectionsProcessed, workerID;
	long startTime;
	boolean running = true;
	Timer heartbeat, registration;
	InetSocketAddress dstAddress;
	/*
	 * @author: Shane Moloney
	 */
	WorkerNode(String dstHost, int dstPort, int srcPort, int id) {
		try {
			dstAddress = new InetSocketAddress(dstHost, dstPort);
			socket = new DatagramSocket(srcPort);
			listener.go();
			workerID = id;
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receives WorkerPacket and sets local variables appropriately.
	 */
	public synchronized void onReceipt(DatagramPacket packet) 
	{
		try {
			PacketContent content = PacketContent.fromDatagramPacket(packet);
			if(content.getType() == PacketContent.WORKERPACKET)
			{
				list = ((WorkerPacket)content).getData();
				search = ((WorkerPacket)content).toString();
				// heartbeat.cancel();
				// heartbeat.purge();
				registration.cancel();
				registration.purge();
			}
			else if(content.getType() == PacketContent.STOPWORK)
			{
				registration.cancel();
				registration.purge();
				heartbeat.cancel();
				heartbeat.purge();
				running = ((StopWork)content).stopWork();
			}
			//System.out.println(content.type);
			this.notify();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Sends the registration packet and begins heartbeat timer.
	 */
	public void register()
	{
		heartbeat = new Timer();
		heartbeat.schedule(new TimerTask()
		{
		@Override
		public void run()
		{
			DatagramPacket heartbeat = new Heartbeat(true, entriesProcessed, sectionsProcessed, workerID).toDatagramPacket();
			heartbeat.setSocketAddress(dstAddress);
			try {
				socket.send(heartbeat);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}, 2500, 2500);

		registration = new Timer();
		registration.schedule(new TimerTask()
		{
		@Override
		public void run()
		{
			DatagramPacket r = new Register(true, System.nanoTime() - startTime, workerID).toDatagramPacket();
			r.setSocketAddress(dstAddress);
			try {
				socket.send(r);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		}, 0, 1000);
	}

	public synchronized void start() throws Exception 
	{
		register();
		sectionsProcessed = -1;
		entriesProcessed = -1;
		list = new String[1];
		list[0] = "0";
		while(running)
		{
			startTime = System.nanoTime();
			entriesProcessed = 0;
			for(int i = 0; i < list.length; i++)
			{
				if(list[i].equals(search))
				{
					DatagramPacket result = new ResultPacket(i, list.length, workerID).toDatagramPacket();
					result.setSocketAddress(dstAddress);
					socket.send(result);
					System.out.println("hi");
					break;
				}
				entriesProcessed++;
			}
			sectionsProcessed++;
			heartbeat.cancel();
			heartbeat.purge();
			registration.cancel();
			registration.purge();
			register();
			this.wait();
		}
		heartbeat.cancel();
		heartbeat.purge();
		registration.cancel();
		registration.purge();
	}
	
	
	public static void main(String[] args) {
		try {
			String id = JOptionPane.showInputDialog(null, "Please enter worker ID:");
			int intID = Integer.parseInt(id);
			WorkerNode Worker = new WorkerNode(DEFAULT_DST_NODE, DST_PORT,  SRC_PORT+intID, intID);
			Worker.start();
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}
	}
}
