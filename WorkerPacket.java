package cs.tcd.ie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Class for packet content that represents file information
 * 
 */
public class WorkerPacket extends PacketContent {
	
	String[] data, input;
	String search;

	WorkerPacket(String[] input, String search) {
		type= WORKERPACKET;
		this.input = input;
		this.search = search;
	}
	
	protected WorkerPacket(ObjectInputStream oin) {
		try {
			type= WORKERPACKET;
			int length = oin.readInt();
			input = new String[length];
			search = oin.readUTF();
			for(int i = 0; i < input.length; i++)
			{
				input[i] = oin.readUTF();
			}
		} 
		catch(Exception e) {e.printStackTrace();}
	}
	
	/**
	 * Writes the content into an ObjectOutputStream
	 *  
	 */
	protected void toObjectOutputStream(ObjectOutputStream oout) {
		try {
			oout.writeInt(input.length);
			oout.writeUTF(search);
			for(int i = 0; i < input.length; i++)
			{
				oout.writeUTF(input[i]);
			}
		}
		catch(Exception e) {e.printStackTrace();}
	}

	public String toString()
	{
		return search;
	}
	
	public void setInput(String[] input)
	{
		this.input = input;
	}
	
	public String[] getData() {
		return input;
	}
}
