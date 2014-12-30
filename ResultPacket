package cs.tcd.ie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents file information
 * @author Kmla Sharma
 * 
 */
public class ResultPacket extends PacketContent {
	
	int result, size, id;
	/**
	 * Constructor that takes in information about a file.
	 * @param result: contains line number of name found. if not found, result should contain -1
	 * @param timeTaken: contains time taken by worker node to complete task
	 * @param size: contains the amount of names in the distributed file sent to each worker node
	 */
	ResultPacket(int result, int size, int id) 
	{
		type= RESULTPACKET;
		this.result = result;
		this.size = size;
		this.id = id;
	}

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected ResultPacket(ObjectInputStream oin) {
		try 
		{
			type= RESULTPACKET;
			result= oin.readInt();
			size = oin.readInt();
			id = oin.readInt();
		} 
		catch(Exception e) {e.printStackTrace();}
	}
	
	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeInt(result);
			oout.writeInt(size);
			oout.writeInt(id);
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
	 * returns the line number (-1 = name not found)
	 *
	 */
	public int getLineNumber()
	{
		return result;
	}
	

	public int getID()
	{
		return id;
	}
	
	/**
	 * how many items were given to worker node to through. (each worker node is equal)
	 *
	 */
	public int getItems()
	{
		return size;
	}
	
}
