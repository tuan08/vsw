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

import java.io.PrintStream;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocsCollector;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.doc.RawDocument;
import org.vsw.nlp.index.DocumentIndexer;
import org.vsw.nlp.index.FSDocumentIndexer;
import org.vsw.util.CommandParser;
import org.vsw.util.ConsoleUtil;
import org.vsw.util.FileUtil;

public class KnowledgeEngine {
	private NLPResource       resource ;
	private KnowledgeIndexer  indexer  ;
	private KnowledgeSearcher searcher ;
	
	public KnowledgeEngine(NLPResource resource, String indexDir) throws Exception {
		this.resource = resource ;
		DocumentIndexer docIndexer = null ;
		if(indexDir == null) docIndexer = new DocumentIndexer() ;
		else docIndexer = new FSDocumentIndexer(indexDir, "knowledge", "shard0") ;
		this.indexer = new KnowledgeIndexer(resource, docIndexer) ; 
	}
	
	public NLPResource getNLPResource() { return this.resource ; }
	
	public KnowledgeIndexer getIndexer() { return this.indexer ; }

	public KnowledgeSearcher getKnowledgeSearcher() throws Exception {
		if(searcher == null) {
			searcher = new KnowledgeSearcher(resource, indexer) ;
		}
		return searcher ;
	}
	
	static public void main(String[] args) throws Exception {
		if(args == null || args.length == 0) {
			args = new String[] {
				"-dbdir", "d:/knowledge",
				"-index", "d:/knowledge/viwiki.json",
				"-query", "Trần Nhân Tông"
			};
		}
		CommandParser command = new CommandParser("knowledge:") ;
    command.addMandatoryOption("dbdir", true, "The knowledge database") ;
    command.addOption("index", true, "Add the index data to the dbdir") ;
    command.addOption("query", true, "the query question") ;
    if(!command.parse(args)) return ;
    command.printHelp() ;
   
    PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
    String dbdir = command.getOption("dbdir", null) ;
    KnowledgeEngine engine = new KnowledgeEngine(NLPResource.getInstance(), dbdir) ;
		String index = command.getOption("index", null) ;
		if(index != null) {
			String[] files = FileUtil.findFiles(index, ".*\\.json") ;
			for(String selFile : files) {
			  out.println("Add File: " + selFile);
				engine.getIndexer().addFile(selFile) ;
			}
			engine.getIndexer().commit() ;
		}
		
		String query = command.getOption("query", null) ;
		if(query != null) {
			KnowledgeSearcher searcher = engine.getKnowledgeSearcher() ;
			TopDocsCollector collector = searcher.query(query, 50) ;
			ScoreDoc[] sdoc = collector.topDocs().scoreDocs ;
			RawDocument[] rdoc = searcher.retrieve(sdoc) ;
			for(int i = 0; i < rdoc.length; i++) {
				out.println(rdoc[i].getField("title") + "(" + sdoc[i].doc + ")");
			}
		}
	}
}