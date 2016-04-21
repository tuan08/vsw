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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.Field;
import org.vsw.nlp.doc.Section;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class DocumentWriter {
	public void write(String file , Document doc) throws Exception {
		write(new FileOutputStream(file), doc) ;
	}
	
	public void write(File file , Document doc) throws Exception {
		write(new FileOutputStream(file), doc) ;
	}
	
	public void write(OutputStream os , Document doc) throws Exception {
		PrintStream out = new PrintStream(os, true, "UTF-8");
		Iterator<Map.Entry<String, Field>> i = doc.getFields().entrySet().iterator() ;
		boolean firstField = true ;
		while(i.hasNext()) {
			if(!firstField) out.print("\n\n\n") ;
			Field field = i.next().getValue() ;
			write(out, field) ;
			firstField = false ;
		}
		out.close() ;
	}
	
	protected void write(PrintStream out, Field field) throws IOException {
		List<Section> sections = field.getSections() ;
		for(int i = 0; i < sections.size(); i++) {
			if(i > 0) out.print("\n\n") ;
			write(out, sections.get(i)) ;
		}
	}
	
	protected void write(PrintStream out, Section section) throws IOException {
		TokenCollection[] collection = section.getTokenCollection() ;
		for(int i = 0; i < collection.length; i++) {
			IToken[] token = collection[i].getTokens() ;
			for(int j = 0; j < token.length; j++) {
				if(j > 0) out.print(' ') ;
				write(out, token[j]) ;
			}
		}
	}
	
	protected void write(PrintStream out, IToken token) throws IOException {
		out.print(token.getOriginalForm()) ;
	}
}