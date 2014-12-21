package cs.tcd.ie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents file information
 * @author Kmla Sharma
 * 
 */
public class SendName extends PacketContent {
	
	String information;
	
	/**
	 * Constructor that takes in information about a file.
	 * @param information: may contain name or whether or not the server found the name (if so, state index of name)
	 *
	 */
	SendName(String information) 
	{
		type= CLIENTNAME;
		this.information = information;
	}

	/**
	 * Constructs an object out of a datagram packet.
	 * @param packet Packet that contains information about a file.
	 */
	protected SendName(ObjectInputStream oin) 
	{
		try 
		{
			type = CLIENTNAME;
			information= oin.readUTF();
		} 
		catch(Exception e) {e.printStackTrace();}
	}
	
	/**
	 * Writes the content into an ObjectOutputStream
	 *
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeUTF(information);
		}
		catch(Exception e) {e.printStackTrace();}
	}


	/**
	 * Returns the content of the packet as String.
	 * 
	 * @return Returns the content of the packet as String.
	 */
	public String toString() 
	{
		return information;
	}
	
}
