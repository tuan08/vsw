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
package org.vsw.nlp.index;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.lucene.search.TopDocsCollector;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.junit.Before;
import org.junit.Test;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.io.DefaultDocumentReader;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.meaning.Entity;
import org.vsw.util.FileUtil;

public class LuceneIndexUnitTest {
	@Before
  public void setup() throws Exception {
    FileUtil.removeIfExist("target/index") ;
  }
	
	@Test
	public void run() throws Exception {
		FileInputStream is = new FileInputStream("src/data/nlp/meaning/vn.place.json") ;
		JsonParser parser = getJsonParser(is) ;
		DocumentReader reader = new DefaultDocumentReader(NLPResource.getInstance()) ;
		String[] filterWords = { "thuộc" } ;
		VNTokenizer tokenizer = new VNTokenizer(reader, filterWords) ;
		DocumentIndexer indexer = new DocumentIndexer() ;
		int idx = 0 ;
		while(parser.nextToken() != null && idx < 10) {
  		Entity meaning = parser.readValueAs(Entity.class) ;
  		IndexDocument idoc = new IndexDocument() ;
  		String name  = meaning.getName() ;
  		if(name != null) name = name.toLowerCase() ;
  		idoc.add("name",  name) ;
  		idoc.add("topic", tokenizer.split(meaning.getTopic())) ;
  		idoc.add("udesc", tokenizer.split(meaning.getUDescription())) ;
  		indexer.index(idoc) ;
  		//if(meaning.getName() != null)System.out.println(meaning.getName());
  		//idx++ ;
		}
		indexer.commit() ;
		indexer.close() ;
		DocumentSearcher searcher = new DocumentSearcher(indexer.getDirectory()) ;
		TopDocsCollector collector = null;
		for(int i = 0; i < 1000; i++) {
			collector = searcher.query("udesc:cần.thơ") ;
		}
		int LOOP = 100000 ;
		long start = System.currentTimeMillis();
		for(int i = 0; i < LOOP; i++) {
			collector = searcher.query("+(udesc:(biện.hòa))", 5) ;
		}
		long execute = System.currentTimeMillis() - start ;
		System.out.println("Execute in: " + execute + "ms" );
		System.out.println("Hit: " + collector.getTotalHits() + ", return: " + collector.topDocs().scoreDocs.length);
	}
	
	static JsonParser getJsonParser(InputStream is) throws JsonParseException, IOException {
		MappingJsonFactory factory = new MappingJsonFactory();
		factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false); // all configuration before use
		JsonParser jp = factory.createJsonParser(is);
		return jp ;
	}
}
