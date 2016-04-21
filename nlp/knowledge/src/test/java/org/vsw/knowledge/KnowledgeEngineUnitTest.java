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

import java.util.List;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocsCollector;
import org.junit.Test;
import org.vsw.knowledge.matcher.TokenCollectionMatchResult;
import org.vsw.knowledge.query.Question;
import org.vsw.knowledge.query.QuestionParser;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.RawDocument;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class KnowledgeEngineUnitTest {
	@Test
	public void run() throws Exception {
		KnowledgeEngine engine = new KnowledgeEngine(NLPResource.getInstance(), null) ;
		engine.getIndexer().addFile("src/test/resources/knowledge.json") ;
		
		KnowledgeSearcher searcher = engine.getKnowledgeSearcher() ;
		QuestionParser qparser = searcher.getQuestionParser() ;
		Question question = qparser.parse("Ai là người đầu tiên lên Mặt Trăng") ;
		TopDocsCollector collector = searcher.query(question, 10) ;
		ScoreDoc[] sdoc = collector.topDocs().scoreDocs ;
		RawDocument[] rdoc = searcher.retrieve(sdoc) ;
		for(int i = 0; i < rdoc.length; i++) {
			System.out.println(i + "." + rdoc[i].getField("title"));
		}
		
		List<TokenCollectionMatchResult> results = searcher.query(question, rdoc) ;
		for(int i = 0; i < results.size(); i++) {
			TokenCollectionMatchResult result = results.get(i) ;
			List<TokenCollectionMatchResult.MatchEntity> matchEntities = result.getMatchEntity("per") ;
			System.out.println("Candidate: " + result.getTokenCollection().getOriginalForm());
			System.out.println("    Match: " + result.toString());
			System.out.println("    Match: " + result.getMatchFrom() + " - " + result.getMatchTo());
			if(matchEntities != null) {
				for(int k = 0; k < matchEntities.size(); k++) {
					TokenCollectionMatchResult.MatchEntity entity = matchEntities.get(k) ;
					System.out.println("    Match Entity: " + entity.getToken().getOriginalForm() + ", " + entity.getDistance());
				}
			}
		}
	}
}