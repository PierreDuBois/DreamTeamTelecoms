package cs.tcd.ie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Register extends PacketContent {
	
	boolean status;
	int nodeID;
	long timeTaken;
	/**
	 * 
	 * @param status: contains info on whether or not the worker node is ready for work. True = it is, false it isnt

	 */
	Register(boolean status, long timeTaken, int id) 
	{
		type= REGISTER;
		this.status = status;
		this.timeTaken = timeTaken;
		nodeID = id;
	}

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected Register(ObjectInputStream oin) {
		try 
		{
			type= REGISTER;
			status= oin.readBoolean();
			nodeID = oin.readInt();
			timeTaken = oin.readLong();
		} 
		catch(Exception e) {e.printStackTrace();}
	}
	
	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeBoolean(status);
			oout.writeInt(nodeID);
			oout.writeLong(timeTaken);
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
	 * returns whether or not the node is ready to work
	 *
	 */
	public boolean isReady()
	{
		return status;
	}
	
	public int nodeNumber()
	{
		return nodeID;
	}
	
	public long time()
	{
		return timeTaken;
	}
}
