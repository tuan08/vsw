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
package org.vsw.knowledge.query;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.ml.crf.qtag.QTAGTokenAnalyzer;
import org.vsw.nlp.token.IToken;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class QuestionParser {
	private DocumentReader docReader ;
	private QTAGTokenAnalyzer qtagAnalyzer ;
	
	public QuestionParser(NLPResource resource, DocumentReader docReader) throws Exception {
		this.docReader = docReader ;
		qtagAnalyzer = new QTAGTokenAnalyzer(resource) ;
	}
	
	public Question parse(String question) throws Exception {
		return new Question(tokenize(question)) ;
	}
	
	public IToken[] tokenize(String question) throws Exception {
		Document doc = docReader.read(question) ;
		doc.analyze(qtagAnalyzer) ;
		IToken[] token = doc.getTokens() ;
		return token ;
	}
}
