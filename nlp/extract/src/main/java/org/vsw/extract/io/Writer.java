package org.vsw.extract.io;

import org.vsw.extract.Document;

public interface Writer {
	public void write(Document doc) throws Exception ;
	public void close() throws Exception ;
}
