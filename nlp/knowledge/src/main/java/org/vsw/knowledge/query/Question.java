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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.vsw.nlp.index.DocumentQueryParser;
import org.vsw.nlp.ml.crf.pos.tag.PosTag;
import org.vsw.nlp.ml.crf.qtag.QuestionTag;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.LexiconTag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class Question {
	private IToken[] token ;
	private IToken[] questionType ;
	private IToken[] questionObject ;
	private String   expectReturnEntity = "per" ;   
	
	public Question(IToken[] token) {
	  this.token = token ;
	  
	  List<IToken> oholder = new ArrayList<IToken>() ;
	  List<IToken> qholder = new ArrayList<IToken>() ;
		for(int i = 0; i < token.length; i++) {
			QuestionTag qtag = token[i].getFirstTagType(QuestionTag.class) ;
			String qtagValue = qtag.getTagValue() ;
			if(qtagValue.startsWith("qtag:type")) {
				qholder.add(token[i]) ;
			} else if(qtagValue.startsWith("qtag:obj")) {
			  oholder.add(token[i]) ;
			}
		}
		questionType   = qholder.toArray(new IToken[qholder.size()]) ;
		questionObject = oholder.toArray(new IToken[oholder.size()]) ;
		mapQuestionType() ;
	}
	
	public String getExpectReturnEntity() { return this.expectReturnEntity ; }
	
	public IToken[] getTokens() { return this.token ; }
	
	public IToken[] getQuestionTypeTokens() { return this.questionType ; }
	
	public IToken[] getQuestionObjectTokens() { return this.questionObject ; }
	
	public String getCandidateQuery(HashSet<String> filterWords) {
		StringBuilder priorityTerm = new StringBuilder() ;
		StringBuilder term = new StringBuilder() ;
		for(int i = 0; i < questionObject.length;  i++) {
			PosTag posTag = questionObject[i].getFirstTagType(PosTag.class) ;
			String pos = posTag.getTagValue() ;
			String nform = questionObject[i].getNormalizeForm() ;
			if(filterWords.contains(nform)) continue ;
			String nterm = DocumentQueryParser.normalize(nform) ;
			if(pos.startsWith("pos:N") || pos.startsWith("pos:V")) {
				if(priorityTerm.length() > 0) priorityTerm.append(" ") ;
				priorityTerm.append(nterm) ;
			}
			if(term.length() > 0) term.append(" ");
			term.append(nterm) ;
		}
		
		StringBuilder b = new StringBuilder() ;
		b.append("content:(") ;
		if(priorityTerm.length() > 0) {
		  b.append("\"").append(priorityTerm).append("\"~5 ");
		}
		b.append("\"").append(term).append("\"~8");
		b.append(")") ;
		return b.toString() ;
	}
	
	private void mapQuestionType() {
		StringBuilder b = new StringBuilder() ;
		for(int i = 0; i < questionType.length; i++) {
			if(i > 0) b.append(" ") ;
			b.append(questionType[i].getNormalizeForm()) ;
		}
		String string = b.toString() ;
		if(string.indexOf("ai") >= 0) this.expectReturnEntity = "per" ;
		else if(string.indexOf("người nào") >= 0) this.expectReturnEntity = "per" ;
		else if(string.indexOf("năm nào") >= 0) this.expectReturnEntity = "time" ;
		else if(string.indexOf("khi nào") >= 0) this.expectReturnEntity = "time" ;
		else if(string.indexOf("ở đâu") >= 0) this.expectReturnEntity = "loc" ;
		else if(string.indexOf("chỗ nào") >= 0) this.expectReturnEntity = "loc" ;
	}
}
