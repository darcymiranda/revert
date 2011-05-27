package util;

public class Stopwatch {
	
	private long t = System.currentTimeMillis();
	
	public void reset(){ t = System.currentTimeMillis(); }
	
	public long elapsed(){ return System.currentTimeMillis() - t; }

}
