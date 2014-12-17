package cs.tcd.ie;

import java.util.Timer;
import java.util.TimerTask;


public class HeartbeatTracker{
	int node;
	Timer timer = new Timer();
	Server server;
	long timeDelay;
	boolean[] array;
	int section;
	
	HeartbeatTracker(Server server, int node, long timeDelay, int section)
	{
		this.server = server;
		this.node = node;
		this.timeDelay = timeDelay;
		this.section = section;
	}
	
	public void clearTimer()
	{
		timer.cancel();
		timer.purge();
	}
	
	public void resetTimer()
	{
		clearTimer();
		timer = new Timer();
	}
	
	public void timerTask()
	{
		timer.schedule(new TimerTask(){
			public void run()
			{
				server.Running[node] = false;
			}
			}, timeDelay, timeDelay);
	}
	
	public int getSection()
	{
		return section;
	}
	
	public void setSection(int section)
	{
		this.section = section;
	}
}
