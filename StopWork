package cs.tcd.ie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class StopWork extends PacketContent {
	
	boolean doWork;
	/**@author: Kmla Sharma
	 * 
	 * @param doWork: should be false when the server wants to send to the worker nodes to stop searching as
	 * result has been found. doWork should never be true

	 */
	StopWork(boolean doWork) 
	{
		type= STOPWORK;
		this.doWork = doWork;
	}

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected StopWork(ObjectInputStream oin) {
		try 
		{
			type= STOPWORK;
			doWork= oin.readBoolean();
		} 
		catch(Exception e) {e.printStackTrace();}
	}
	
	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeBoolean(doWork);
		}
		catch(Exception e) {e.printStackTrace();}
	}


	/**
	 * Returns the content of the packet as String.
	 * This function must be here in order to extend packet content
	 * @return Returns the content of the packet as String.
	 */
	public String toString() 
	{
		return null;
	}
	
	/**
	 * returns false
	 *
	 */
	public boolean stopWork()
	{
		return doWork;
	}
}
