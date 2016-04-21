package org.vsw.extract.io;

import org.vsw.extract.Document;

public interface Reader {
	public Document next() throws Exception ;
	
	public void close() throws Exception ;
}
