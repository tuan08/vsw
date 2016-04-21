package org.vsw.extract.rdf;

import org.vsw.extract.Document;
import org.vsw.nlp.query.QueryContext;

public interface RDFWriter {
	public void write(Document doc, QueryContext context) throws Exception ;
	public void close() throws Exception ;
}
