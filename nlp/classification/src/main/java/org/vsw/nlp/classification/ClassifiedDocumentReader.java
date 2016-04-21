package org.vsw.nlp.classification;

abstract public class ClassifiedDocumentReader {
	abstract public ClassifiedDocument read() throws Exception ;

	public void close() throws Exception {
		
	}
}
