package org.headvances.hadoop.batchdb;

public interface Reporter {
	static enum BatchDB { WRITE, COMPACT }
	
	public void progress() ;
	public void increment(String name, int amount) ;
}
