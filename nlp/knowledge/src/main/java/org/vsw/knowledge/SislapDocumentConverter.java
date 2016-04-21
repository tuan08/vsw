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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.vsw.nlp.doc.RawDocument;
import org.vsw.nlp.doc.RawField;
import org.vsw.nlp.doc.io.RawDocumentWriter;
import org.vsw.util.FileUtil;
import org.vsw.util.HtmlUtil;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class SislapDocumentConverter {
	static public void main(String[] args) throws Exception {
		String[] files = FileUtil.findFiles("d:/wikidata2", ".*wiki.*") ;
		RawDocumentWriter writer = new RawDocumentWriter("viwiki.json") ;
		int idx = 0;
		for(String file : files) {
			WikiReader reader = new WikiReader(file) ;
			RawDocument doc = null ;
			while((doc = reader.next()) != null) {
				writer.write(doc) ;
				System.out.println(idx + ". " + doc.getField("title").toSectionText());
				idx++ ;
			}
			reader.close() ;
		}
		writer.close() ;
		System.out.println("Convert " + idx + " wiki");
	}
	
	static public class WikiReader {
		private String file ;
		private BufferedReader reader ; 
		
		WikiReader(String file) throws Exception {
			this.file = file ;
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file))) ;
		}
		
		public RawDocument next() throws Exception {
			String header = reader.readLine() ;
			if(header == null) return null;
			header = header.trim() ;
			if(header.length() == 0) return null;
			if(!header.startsWith("<doc id")) {
				throw new Exception("Expect start with <doc id=, check file " + file) ;
			}
			RawDocument doc  = new RawDocument() ;
			String  titleStr = HtmlUtil.removeTag(reader.readLine()) ;
			RawField title   = new RawField("title", titleStr) ;
			RawField content = new RawField("content") ;
			String line      = null ;
			while((line = reader.readLine()) != null) {
				line = line.trim() ;
				if(line.indexOf("</doc>") >= 0) {
					break ;
				}
				line = HtmlUtil.removeTag(line) ;
				line = line.replace("a>", "") ;
				if(line.length() == 0) continue ;
				content.add(line) ;
			}
			doc.add(title) ;
			doc.add(content) ;
			return doc ;
		}
		
		public void close() throws Exception {
			reader.close() ;
		}
	}
}