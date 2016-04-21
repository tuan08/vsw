package org.vsw.hadoop.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

public class WritableUtil {
	static public String readString(DataInput in) throws IOException {
		boolean notNull = in.readBoolean() ;
		if(notNull) return Text.readString(in) ;
		return null ;
	}
	
	static public void writeString(DataOutput out, String string) throws IOException {
		if(string == null) {
			out.writeBoolean(false) ;
			return ;
		} else {
			out.writeBoolean(true) ;
			Text.writeString(out, string) ;
		}
	}
	
	static public String[] readArray(DataInput in, String[] dvalue) throws IOException {
		boolean notNull = in.readBoolean() ;
		if(notNull) {
			int size = in.readInt() ;
			String[] array = new String[size] ;
			for(int i = 0; i < array.length; i++) {
				array[i] = Text.readString(in) ;
			}
			return array;
		}
		return dvalue  ;
	}
	
	static public void writeArray(DataOutput out, String[] string) throws IOException {
		if(string == null) {
			out.writeBoolean(false) ;
			return ;
		} else {
			out.writeBoolean(true) ;
			out.writeInt(string.length) ;
			for(int i = 0; i < string.length; i++) {
			  Text.writeString(out, string[i]) ;
			}
		}
	}
	
	static public void writeArray(DataOutput out, Writable[] array) throws IOException {
		if(array == null) {
			out.writeBoolean(false) ;
		} else {
			out.writeBoolean(true) ;
			out.writeInt(array.length) ;
			for(int i = 0; i < array.length; i++) {
				array[i].write(out) ;
			}
		}
	}
	
  static public <T extends Writable> T[] readArray(DataInput in, WritableFactory<T> factory) throws IOException {
  	boolean notNull = in.readBoolean() ;
  	if(notNull) {
  		int size = in.readInt() ;
  		T[] array = factory.newArray(size) ;
  		for(int i = 0; i < size; i++) {
  			array[i] = factory.newInstance() ;
  			array[i].readFields(in) ;
  		}
  		return array ;
  	}
  	return null ;
  }
	
	static public interface WritableFactory<T extends Writable> {
		public T newInstance() ;
		public T[] newArray(int size) ;
	}
}
