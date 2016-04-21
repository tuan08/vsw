package org.vsw.extract;

import org.vsw.extract.io.JSONReader;
import org.vsw.extract.io.Reader;
import org.vsw.extract.mediawiki.XMLMediaWikiDumpReader;
import org.vsw.nlp.query.QueryDataIterator;

public class SectionProcessor extends Processor {
	private String[] section ;
	
	public SectionProcessor(String file, String[] section) throws Exception {
		Reader reader  = null ;
    if(file.endsWith(".xml")) reader = new XMLMediaWikiDumpReader(file) ;
    if(file.endsWith(".json")) reader = new JSONReader(file) ;
    init(reader) ;
    this.section = section ;
	}
	
	public SectionProcessor(Reader reader) throws Exception {
		this(reader, null) ;
	}
	
  public SectionProcessor(Reader reader, String[] section) throws Exception {
	  init(reader);
	  this.section = section ;
  }

	protected QueryDataIterator createQueryDataIterator(Document doc) throws Exception {
	  return new SectionIterator(this.section, doc, analyzer);
  }
}
