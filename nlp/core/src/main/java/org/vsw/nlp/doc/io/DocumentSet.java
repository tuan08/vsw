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
package org.vsw.nlp.doc.io;

import java.io.FileInputStream;
import java.io.IOException;

import org.vsw.nlp.doc.Document;
import org.vsw.util.FileUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class DocumentSet {
	private String[] files ;
	private DocumentReader reader ;
	
	public DocumentSet(String dir, String pattern, DocumentReader reader) throws IOException {
		this.files = FileUtil.findFiles(dir, pattern) ;
		this.reader = reader ;
	}
	
	public int size() { return files.length; }
	
	public String getFile(int i) { return files[i] ; }
	
	public String[] getFiles() { return files ; }
	
	public DocumentReader getDocumentReader() { return this.reader ; }
	
	public Document getDocument(int idx) throws Exception {
		Document doc = reader.read(new FileInputStream(files[idx])) ;
		doc.setUrl(files[idx]) ;
		return doc ;
	}
}
