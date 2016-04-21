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
package org.vsw.knowledge.ie.candidate;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.headvances.hadoop.util.HDFSUtil;
import org.headvances.hadoop.util.SequenceFileUtil;
import org.vsw.knowledge.KnowledgeDocumentReader;
import org.vsw.nlp.NLPResource;
import org.vsw.nlp.dict.DictionaryEntity;
import org.vsw.nlp.dict.DictionaryEntity.Entities;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.RawDocument;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.doc.io.RawDocumentReader;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.util.CommandParser;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class CandidateExtractor {
	private DocumentReader documentReader ;
	private NLPResource resource ;
	
	public CandidateExtractor() throws Exception {
		this.resource = NLPResource.getInstance() ;
		documentReader = 
			new KnowledgeDocumentReader(resource).
			addDictionaryTaggingAnalyzer(resource).
			addUnknownWordTokenSplitter(resource) ;
	}
	
	public void extract(String infile, String outfile) throws Exception {
		extractCandidate(infile, outfile + ".candidate") ;
		mergeCandidate(outfile + ".candidate", outfile) ;
	}
	
	private void extractCandidate(String infile, String outfile) throws Exception {
		DictionaryEntity entityDict = resource.getDictionaries().getDictionaryEntity() ;
		RawDocumentReader reader = new RawDocumentReader(infile) ;
		Configuration conf = HDFSUtil.getDaultConfiguration() ;
		SequenceFileUtil<Text, Candidate> sqfile = 
			new SequenceFileUtil<Text, Candidate>(conf, outfile, Text.class, Candidate.class) ;
		SequenceFileUtil<Text, Candidate>.Writer writer = sqfile.getWriter(true) ;
		RawDocument rdoc = null ;
		int candidateCount = 0, docCount = 0 ;
		while((rdoc = reader.read()) != null) {
			Document doc = rdoc.toDocument(documentReader) ;
			TokenCollection[] collections = doc.getTokenCollections() ;
			for(TokenCollection selCollection : collections) {
				IToken[] token = selCollection.getTokens() ;
				for(int i = 0; i < token.length; i++) {
					String nform = token[i].getNormalizeForm() ;
					if(nform.length() == 1) continue ;
					String oform = token[i].getOriginalForm() ;
					if(!Character.isUpperCase(oform.charAt(0))) continue ;
					Entities entities = entityDict.getEntities(token[i].getNormalizeForm()) ;
					if(entities != null) {
						Candidate candidate = 
							new Candidate("viwikipedia", token[i].getNormalizeForm(), selCollection.getOriginalForm()) ;
						writer.append(new Text(nform), candidate) ;
						candidateCount++ ;
					}
				}
			}
			docCount++ ;
			if(docCount % 100 == 0) {
				System.out.println("Document " + docCount + ", Candidate " + candidateCount);
			}
		}
		writer.close() ;
		reader.close() ;
	}
	
	private void mergeCandidate(String infile, String outfile) throws Exception {
		Configuration conf = HDFSUtil.getDaultConfiguration() ;
		SequenceFileUtil<Text, Candidate> sqfile = 
			new SequenceFileUtil<Text, Candidate>(conf, infile, Text.class, Candidate.class) ;
		SequenceFile.Reader reader = sqfile.getReader() ;
		CandidatesWriter writer = new CandidatesWriter(outfile);
		Candidates current = null ;
		Text pkey = new Text() ;
		int entityCount = 0 ;
		while(true) {
			Text key = new Text() ;
			Candidate candidate = new Candidate() ;
			if(reader.next(key, candidate)) {
				if(pkey == null || !pkey.equals(key)) {
					if(current != null) writer.write(current) ;
					entityCount++ ;
					current = new Candidates() ;
				}
				current.add(candidate) ;
				pkey = key ;
			} else {
				break ;
			}
			if(entityCount % 100 == 0) {
				System.out.println("Merge " + entityCount + " entities");
			}
		}
		if(current != null) writer.write(current) ;
		writer.close() ;
		reader.close() ;
	}
	
	static public void main(String[] args) throws Exception {
		if(args == null || args.length == 0) {
			args = new String[] {
				"-infile", "d:/knowledge/viwiki.json",
				"-outfile", "viwiki.ent.json"
			};
		}
		CommandParser command = new CommandParser("knowledge:") ;
    command.addMandatoryOption("infile",  true, "The input raw document file") ;
    command.addMandatoryOption("outfile", true, "The output entity candidate file") ;
    if(!command.parse(args)) return ;
    command.printHelp() ;
    String infile = command.getOption("infile", null) ;
    String outfile = command.getOption("outfile", null) ;
    CandidateExtractor extractor = new CandidateExtractor() ;
    extractor.extract(infile, outfile) ;
	}
}