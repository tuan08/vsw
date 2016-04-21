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
package org.vsw.nlp.ml.io;

import java.util.List;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.Field;
import org.vsw.nlp.doc.Section;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.ml.BoundaryTag;
import org.vsw.nlp.ml.WTagTokenizer;
import org.vsw.nlp.ml.crf.sent.CRFSentenceSpliterAnalyzer;
import org.vsw.nlp.ml.dict.DictionaryTaggingAnalyzer;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.CommonTokenAnalyzer;
import org.vsw.nlp.token.analyzer.LineAnalyzer;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WTagDocumentReader extends DocumentReader {
	static TokenAnalyzer[] TOKEN_ANALYZER =  { new CommonTokenAnalyzer() } ;
	
	public WTagDocumentReader()  { 
		super(TOKEN_ANALYZER, null, null) ;
	}

	public WTagDocumentReader(NLPResource resource) throws Exception {
		super(TOKEN_ANALYZER, new CRFSentenceSpliterAnalyzer(resource), new DictionaryTaggingAnalyzer(resource)) ;
	}
	
	public Document read(IToken[] token) throws TokenException {
		for(int i = 0; i < token.length; i++) {
			BoundaryTag btag = token[i].getFirstTagType(BoundaryTag.class) ;
			if(btag == null) btag = new BoundaryTag(StringUtil.EMPTY_ARRAY) ;
			token[i].clearTag() ;
			token[i].add(btag) ;
		}
		Document doc = new Document() ;
		Field field = new Field("content") ;
		for(TokenCollection line : LineAnalyzer.INSTANCE.analyze(token)) field.add(line) ;
		doc.add(field) ;
		process(doc) ;
		return doc ;
	}
	
	protected Document createDocument(String text) throws TokenException {
		Document doc = new Document() ;
		Field field = new Field("content") ;
		List<String> line = StringUtil.split(text, '\n') ;
		for(int i = 0; i < line.size(); i++) {
			Section section = new Section(new WTagTokenizer(line.get(i).trim()).allTokens()) ;
			field.add(section) ;
		}
		doc.add(field) ;
		return doc ;
	}
}