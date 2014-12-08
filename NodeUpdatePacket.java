package cs.tcd.ie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents acknowledgements
 * 
 */
public class NodeUpdatePacket extends PacketContent {
	
	boolean found;
	int NodeNumber;
	int index;
	
	/**
	 * Constructor that takes in information about a file.
	 * @param filename Initial filename.
	 * @param size Size of filename.
	 */
	NodeUpdatePacket(boolean found, int number, int index) {
		type= ACKPACKET;
		this.found = found;
		this.NodeNumber = number;
		this.index = index;
	}

	
	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected NodeUpdatePacket(ObjectInputStream oin) {
		try {
			type= ACKPACKET;
			found = oin.readBoolean();
		    NodeNumber= oin.read();
		    this.index = oin.read();
		} 
		catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeBoolean(found);
			oout.write(NodeNumber);
			oout.write(index);
		}
		catch(Exception e) {e.printStackTrace();}
	}


	
	/**
	 * Returns the content of the packet as String.
	 * 
	 * @return Returns the content of the packet as String.
	 */
	public String toString() {
		return "Search Item is found = " + found + ", current index =" + index + ", for Worker Node " + NodeNumber;
	}
	
	/**
	 * Returns the info contained in the packet.
	 * 
	 * @return Returns the info contained in the packet.
	 */
	public boolean wasFound() 
	{
		return found;
	}
	public int getIndex() 
	{
		return index;
	}
	public int getNumber() 
	{
		return NodeNumber;
	}
}