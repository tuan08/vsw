package org.vsw.extract.entity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.vsw.hadoop.util.WritableUtil;

public class Candidate implements Writable {
	final static public WritableUtil.WritableFactory<Candidate> FACTORY = new WritableUtil.WritableFactory<Candidate>() {
    public Candidate newInstance() { return new Candidate() ; }
    public Candidate[] newArray(int size) { return new Candidate[size]; }
	};
	
	private String candidate ;
	private int  occurrence ;
	
	public Candidate() {}
	
	public Candidate(String candidate, int occr) {
		this.candidate = candidate ;
		this.occurrence = occr ;
	}
	
	public String getCandidate() { return candidate; }
	public void setCandidate(String candidate) { this.candidate = candidate; }
	
	public int getOccurrence() { return occurrence; }
	public void setOccurrence(int occurrence) { this.occurrence = occurrence; }

  public void readFields(DataInput in) throws IOException {
  	this.candidate = Text.readString(in) ;
  	this.occurrence = in.readInt() ;
  }

  public void write(DataOutput out) throws IOException {
  	Text.writeString(out, this.candidate) ;
  	out.writeInt(this.occurrence) ;
  }
}