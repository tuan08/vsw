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
package org.vsw.nlp.ml.crf.per;

import java.util.List;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.dict.Dictionaries;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.Field;
import org.vsw.nlp.doc.Section;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.ml.crf.sent.CRFSentenceSpliterAnalyzer;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.CommonTokenAnalyzer;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.util.StringUtil;

public class PersonDocumentReader extends DocumentReader {
  static TokenAnalyzer[] TOKEN_ANALYZER =  { new CommonTokenAnalyzer()} ;
	public PersonDocumentReader() throws Exception { 
		this(NLPResource.getInstance()) ; 
	}
	
	public PersonDocumentReader(NLPResource resource) throws Exception { 
		super(TOKEN_ANALYZER, new CRFSentenceSpliterAnalyzer(resource), null) ;
	}

	protected Document createDocument(String text) throws TokenException {
		Document doc = new Document() ;
		Field field = new Field("content") ;
		List<String> line = StringUtil.split(text, '\n') ;
		for(int i = 0; i < line.size(); i++) {
			Section section = new Section(line.get(i).trim()) ;
			field.add(section) ;
		}
		doc.add(field) ;
		return doc ;
	}
}