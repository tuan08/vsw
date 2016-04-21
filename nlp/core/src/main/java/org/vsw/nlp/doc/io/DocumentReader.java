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

import java.io.InputStream;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.ml.crf.sent.CRFSentenceSpliterAnalyzer;
import org.vsw.nlp.ml.crf.ws.WSTokenAnalyzer;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.CommonTokenAnalyzer;
import org.vsw.nlp.token.analyzer.SentenceSplitterAnalyzer;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.token.analyzer.TokenCollectionAnalyzer;
import org.vsw.nlp.token.analyzer.TokenSplitterAnalyzer;
import org.vsw.util.IOUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
abstract public class DocumentReader {
	protected TokenAnalyzer[] tokenAnalyzer  ;
	protected TokenCollectionAnalyzer sentSpliter = SentenceSplitterAnalyzer.INSTANCE ;
	protected TokenAnalyzer           wordAnalyzer ;
	
	public DocumentReader(NLPResource resource) { 
		this.tokenAnalyzer = new TokenAnalyzer[] {
			new CommonTokenAnalyzer(),
			new TokenSplitterAnalyzer(resource)
		};
		this.sentSpliter =  new CRFSentenceSpliterAnalyzer(resource) ;
		this.wordAnalyzer = new WSTokenAnalyzer(resource) ;
	}

	public DocumentReader(TokenAnalyzer[] tokenAnalyzer, TokenCollectionAnalyzer sentenceAnalyzer, 
			                  TokenAnalyzer wsAnalyzer) { 
		this.tokenAnalyzer = tokenAnalyzer ;
		this.sentSpliter   = sentenceAnalyzer ;
		this.wordAnalyzer  = wsAnalyzer ;
	}
	
	public DocumentReader(TokenCollectionAnalyzer sentenceAnalyzer, TokenAnalyzer wsAnalyzer) { 
		this.sentSpliter = sentenceAnalyzer ;
		this.wordAnalyzer = wsAnalyzer ;
	}
	
	public DocumentReader setTokenAnalyzer(TokenAnalyzer[] tokenAnalyzer) {
		this.tokenAnalyzer = tokenAnalyzer ;
		return this ;
	}
	
	public DocumentReader setSentenceSpliter(TokenCollectionAnalyzer analyzer) {
		this.sentSpliter = analyzer; 
		return this ;
	}
	
	public DocumentReader setWordAnalyzer(TokenAnalyzer wanalyzer) {
		this.wordAnalyzer = wanalyzer ;
		return this;
	}
	
	public Document read(InputStream is) throws Exception {
		return read(is, null) ;
	}
	
	public Document read(InputStream is, String encoding) throws Exception {
		if(encoding == null) encoding = "UTF-8" ;
		String text = IOUtil.getStreamContentAsString(is, encoding) ;
		return read(text) ;
	}
	
	public Document read(String text) throws TokenException {
		Document doc = createDocument(text) ;
		process(doc) ;
		return doc ;
	}
	
	public void process(Document doc) throws TokenException {
		if(tokenAnalyzer != null) doc.analyze(tokenAnalyzer) ;
		onPostAnalyzeToken(doc) ;
		if(sentSpliter != null) doc.analyze(sentSpliter) ;
		onPostAnalyzeSentence(doc) ;
		if(wordAnalyzer != null) doc.analyze(wordAnalyzer) ;
		onPostAnalyzeWord(doc) ;
	}
	
	protected void onPostAnalyzeToken(Document doc) throws TokenException { }
	
	protected void onPostAnalyzeSentence(Document doc) throws TokenException { }
	
	protected void onPostAnalyzeWord(Document doc) throws TokenException { }
	
	abstract protected Document createDocument(String text) throws TokenException ;
}