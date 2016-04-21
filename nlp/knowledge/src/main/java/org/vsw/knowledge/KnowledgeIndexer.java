/**
 * Copyright (C) 2011 Headvances Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This project aim to build a set of library/data to process 
 * the Vietnamese language and analyze the web data
 **/
package org.vsw.knowledge;

import java.io.IOException;
import java.util.Iterator;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.RawDocument;
import org.vsw.nlp.doc.RawField;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.doc.io.RawDocumentReader;
import org.vsw.nlp.doc.io.RawDocumentSerializer;
import org.vsw.nlp.index.DocumentIndexer;
import org.vsw.nlp.index.IndexDocument;
import org.vsw.nlp.index.VNTokenizer;
import org.vsw.util.GZipUtil;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class KnowledgeIndexer  {
	private DocumentReader documentReader ;
	private DocumentIndexer docIndexer ;
	private VNTokenizer vntokenizer ;
	private RawDocumentSerializer serializer ;
	
	public KnowledgeIndexer(NLPResource resource, DocumentIndexer docIndexer) {
		this.docIndexer = docIndexer ;
		documentReader = 
			new KnowledgeDocumentReader(resource).
			addDictionaryTaggingAnalyzer(resource).
			addUnknownWordTokenSplitter(resource) ;
		this.vntokenizer = new VNTokenizer(documentReader, StringUtil.EMPTY_ARRAY) ;
		serializer = new RawDocumentSerializer() ;
	}

	public DocumentReader getDocumentReader() { return this.documentReader ;}
	
	public VNTokenizer getVNTokenizer() { return vntokenizer ; }
	
	public DocumentIndexer getDocumentIndexer() { return this.docIndexer ; }
	
	public RawDocumentSerializer getRawDocumentSerializer() { return this.serializer ; }
	
	public void add(RawDocument doc) throws Exception {
		IndexDocument idoc = new IndexDocument() ;
		Iterator<RawField> fitr = doc.values().iterator() ;
		while(fitr.hasNext()) {
			RawField rfield = fitr.next() ;
			String[] term = vntokenizer.split(rfield.toSectionText()) ;
			idoc.add(rfield.getName(), term) ;
		}
		byte[] data = GZipUtil.compress(serializer.serialize(doc)) ;
		idoc.add("data:byte", data) ;
		docIndexer.index(idoc) ;
	}
	
	public void addFile(String file) throws Exception {
		RawDocumentReader reader = new RawDocumentReader(file) ;
		RawDocument rdoc = null ;
		while((rdoc = reader.read()) != null) {
			add(rdoc) ;
		}
		commit() ;
		reader.close() ;
	}
	
	public void commit() throws IOException {
		docIndexer.commit() ;
	}
}
