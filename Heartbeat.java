package cs.tcd.ie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents acknowledgements
 * 
 */
public class Heartbeat extends PacketContent {
	
    boolean active;
    int currentindex;
    int sectionsprocessed;
    int NodeNumber;
    
	
	/**
	 * Constructor that takes in information about a file.
	 * @param filename Initial filename.
	 * @param size Size of filename.
	 */
	Heartbeat(boolean active, int currentindex, int sectionprocessed, int NodeNumber) {
		type= HEARTBEAT;
		this.active = active;
		this.currentindex = currentindex;
		this.sectionsprocessed = sectionprocessed;
		this.NodeNumber = NodeNumber;
	}

	
	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected Heartbeat(ObjectInputStream oin) {
		try {
			type= HEARTBEAT;
			active = oin.readBoolean();
			currentindex = oin.read();
			sectionsprocessed = oin.read();
			NodeNumber = oin.read();
		} 
		catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeBoolean(active);
			oout.write(currentindex);
			oout.write(sectionsprocessed);
			oout.write(NodeNumber);
		}
		catch(Exception e) {e.printStackTrace();}
	}


	
	/**
	 * Returns the content of the packet as String.
	 * 
	 * @return Returns the content of the packet as String.
	 */
	public String toString() {
		return "";
	}
	
	/**
	 * Returns the info contained in the packet.
	 * 
	 * @return Returns the info contained in the packet.
	 */
	public boolean active() 
	{
		return active;
	}
	public int index() 
	{
		return currentindex;
	}
	public int sections() 
	{
		return sectionsprocessed;
	}
	public int number()
	{
	return NodeNumber;	
	}
	
}