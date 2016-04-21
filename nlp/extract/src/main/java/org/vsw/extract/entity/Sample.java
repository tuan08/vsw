package org.vsw.extract.entity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.vsw.hadoop.util.WritableUtil;

public class Sample implements Writable {
	final static public WritableUtil.WritableFactory<Sample> FACTORY = new WritableUtil.WritableFactory<Sample>() {
    public Sample newInstance() { return new Sample() ; }
    public Sample[] newArray(int size) { return new Sample[size]; }
	};
	
	private String sample ;
	private int  occurrence ;
	
	public Sample() { }
	
	public Sample(String sample, int occr) {
		this.sample = sample; 
		this.occurrence = occr ;
	}
	
	public String getSample() { return sample; }
	public void setSample(String sample) { this.sample = sample; }
	
	public int getOccurrence() { return occurrence; }
	public void  setOccurrence(int occurrence) { this.occurrence = occurrence; }

  public void readFields(DataInput in) throws IOException {
  	this.sample = Text.readString(in) ;
  	this.occurrence = in.readInt() ;
  }

  public void write(DataOutput out) throws IOException {
  	Text.writeString(out, this.sample) ;
  	out.writeInt(occurrence) ;
  }
}