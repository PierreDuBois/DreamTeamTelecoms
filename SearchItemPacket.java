package cs.tcd.ie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents acknowledgements
 * 
 */
public class SearchItemPacket extends PacketContent {
	
	String SearchItem;
	/**
	 * Constructor that takes in information about a file.
	 * @param filename Initial filename.
	 * @param size Size of filename.
	 */
	SearchItemPacket(String item) {
		type= ACKPACKET;
		this.SearchItem = item;
	}

	
	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected SearchItemPacket(ObjectInputStream oin) {
		try {
			type= ACKPACKET;
			SearchItem= oin.readUTF();
		} 
		catch(Exception e) {e.printStackTrace();}
	}

	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeUTF(SearchItem);
		}
		catch(Exception e) {e.printStackTrace();}
	}


	
	/**
	 * Returns the content of the packet as String.
	 * 
	 * @return Returns the content of the packet as String.
	 */
	public String toString() {
		return SearchItem;
	}
	
	/**
	 * Returns the info contained in the packet.
	 * 
	 * @return Returns the info contained in the packet.
	 */
	public String getSearchItem() 
	{
		return SearchItem;
	}
	
}