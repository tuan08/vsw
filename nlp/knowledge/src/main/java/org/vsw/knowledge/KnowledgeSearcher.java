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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocsCollector;
import org.vsw.knowledge.matcher.TokenCollectionMatchResult;
import org.vsw.knowledge.matcher.TokenCollectionMatcher;
import org.vsw.knowledge.query.Question;
import org.vsw.knowledge.query.QuestionParser;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.RawDocument;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.doc.io.RawDocumentSerializer;
import org.vsw.nlp.index.DocumentSearcher;
import org.vsw.nlp.ml.crf.ent.NPEntityTokenAnalyzer;
import org.vsw.nlp.ml.crf.ent.NumEntityTokenAnalyzer;
import org.vsw.nlp.ml.crf.pos.POSTokenAnalyzer;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.util.GZipUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class KnowledgeSearcher {
	private DocumentSearcher      searcher ;
	private RawDocumentSerializer serializer ;
	private QuestionParser        questionParser ;
	private DocumentReader        rawDocumentReader ;
	private TokenAnalyzer[]       entityAnalyzer;
	
	public KnowledgeSearcher(NLPResource resource, KnowledgeIndexer indexer) throws Exception {
		searcher = new DocumentSearcher(indexer.getDocumentIndexer().getDirectory()) ;
		DocumentReader reader = 
			new KnowledgeDocumentReader(resource).
			addDictionaryTaggingAnalyzer(resource).
			addUnknownWordTokenSplitter(resource).
			addPOSTokenAnalyzer(resource) ;
		rawDocumentReader = 
			new KnowledgeDocumentReader(resource).
			addDictionaryTaggingAnalyzer(resource).
			addUnknownWordTokenSplitter(resource);
		entityAnalyzer = new TokenAnalyzer[] {
			new POSTokenAnalyzer(resource),
			new NPEntityTokenAnalyzer(resource),
			new NumEntityTokenAnalyzer(resource)
		};
		this.serializer     = indexer.getRawDocumentSerializer() ;
		this.questionParser = new QuestionParser(resource, reader) ;
	}

	public QuestionParser getQuestionParser() { return this.questionParser ; }
	
	public TopDocsCollector query(String question,  int limit) throws Exception {
		Question q = questionParser.parse(question) ;
		String query = q.getCandidateQuery(new HashSet<String>()) ;
		System.out.println("Query: " + query);
		return searcher.query(query, limit) ;
  }
	
	public TopDocsCollector query(Question question,  int limit) throws Exception {
		String query = question.getCandidateQuery(new HashSet<String>()) ;
		System.out.println("Query: " + query);
		return searcher.query(query, limit) ;
  }
	
	public RawDocument[] retrieve(ScoreDoc[] hdoc) throws Exception {
		RawDocument[] rdoc = new RawDocument[hdoc.length] ;
		for(int i = 0; i < hdoc.length; i++) {
			byte[] data = searcher.getDocumentFieldValueAsBytes(hdoc[i].doc, "data:byte") ;
			data = GZipUtil.decompress(data) ;
			rdoc[i] = serializer.deserialize(data) ;
		}
		return rdoc ;
	}
	
	public List<TokenCollectionMatchResult> query(Question question, RawDocument[] rdoc) throws Exception {
		TokenCollectionMatcher cmatcher = new TokenCollectionMatcher(question.getQuestionObjectTokens()) ;
		List<TokenCollectionMatchResult> holder = null ;
		for(int i = 0; i < rdoc.length; i++) {
			Document doc = rdoc[i].toDocument(rawDocumentReader) ;
			TokenCollection[] collections = doc.getTokenCollections() ;
			for(TokenCollection sel : collections) {
				if(cmatcher.isCandidate(sel)) {
					TokenCollectionMatchResult result = cmatcher.matches(sel) ;
					result.getTokenCollection().analyze(entityAnalyzer) ;
					if(holder == null) holder = new ArrayList<TokenCollectionMatchResult>() ;
					holder.add(result) ;
				}
			}
		}
		if(holder != null) {
		  Collections.sort(holder, TokenCollectionMatchResult.SCORE_COMPARATOR) ;
		}
		return holder ;
	}
}	