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

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.io.DefaultDocumentReader;
import org.vsw.nlp.ml.crf.pos.POSTokenAnalyzer;
import org.vsw.nlp.ml.crf.ws.UnknownWordTokenSplitter;
import org.vsw.nlp.ml.dict.DictionaryTaggingAnalyzer;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class KnowledgeDocumentReader extends DefaultDocumentReader {
	private TokenAnalyzer[] postWordAnalyzers; 
	
	public KnowledgeDocumentReader(NLPResource resource) {
	  super(resource);
	}
	
	public KnowledgeDocumentReader addDictionaryTaggingAnalyzer(NLPResource resource) {
		addPostWordTokenAnalyzer(new DictionaryTaggingAnalyzer(resource)) ;
		return this ;
	}
	
	public KnowledgeDocumentReader addUnknownWordTokenSplitter(NLPResource resource) {
		addPostWordTokenAnalyzer(new UnknownWordTokenSplitter(resource)) ;
		return this ;
	}
	
	public KnowledgeDocumentReader addPOSTokenAnalyzer(NLPResource resource) {
		addPostWordTokenAnalyzer(new POSTokenAnalyzer(resource)) ;
		return this ;
	}
	
	public KnowledgeDocumentReader addPostWordTokenAnalyzer(TokenAnalyzer analyzer) {
		if(postWordAnalyzers == null) {
			postWordAnalyzers = new TokenAnalyzer[] {analyzer} ;
		} else {
			TokenAnalyzer[] tmp = new TokenAnalyzer[postWordAnalyzers.length + 1] ;
			System.arraycopy(postWordAnalyzers, 0, tmp, 0, postWordAnalyzers.length) ;
			tmp[postWordAnalyzers.length] = analyzer ;
			postWordAnalyzers = tmp ;
		}
		return this ;
	}
	
	public void onPostAnalyzeWord(Document doc) throws TokenException { 
		if(postWordAnalyzers == null) return ;
		doc.analyze(postWordAnalyzers) ;
	}
}
