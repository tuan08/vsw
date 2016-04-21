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
package org.vsw.nlp.ml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.dict.Dictionaries;
import org.vsw.nlp.doc.Document;
import org.vsw.nlp.doc.io.DocumentReader;
import org.vsw.nlp.doc.io.DocumentSet;
import org.vsw.nlp.ml.io.WTagDocumentReader;
import org.vsw.nlp.ml.io.WTagDocumentWriter;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenException;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.util.CharacterSet;
import org.vsw.util.FileUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
abstract public class TokenProcessor {
	private String file ;
	private boolean saveChange = false ;
	protected Dictionaries dictionaries ;
	protected PrintStream out;
	
	public TokenProcessor(String file, boolean saveChange) throws Exception  {
		this(System.out, file, saveChange) ;
	}
	
	public TokenProcessor(PrintStream out, String file, boolean saveChange) throws Exception  {
		this.out = out ;
		this.file = file ;
		this.saveChange = saveChange ;
		dictionaries = new Dictionaries() ;
	}
	
	public void process() throws Exception {
		DocumentReader reader = new WTagDocumentReader(NLPResource.getInstance()) ;
		DocumentSet set = new DocumentSet(file, ".*\\.(tagged|wtag)", reader) ;
		TokenAnalyzer inspector = new TokenAnalyzer() {
      public IToken[] analyze(IToken[] token) throws TokenException {
      	List<IToken> holder = new ArrayList<IToken>() ;
    		for(int i = 0 ; i < token.length; i++) {
    			IToken[] newToken = process(token, i) ;
    			for(IToken sel : newToken) holder.add(sel) ;
    		}
    		return holder.toArray(new IToken[holder.size()]) ;
      }
			
		};
		WTagDocumentWriter writer = new WTagDocumentWriter() ;
		for(int i = 0; i < set.size(); i++) {
			//out.println("Start Checking: " + set.getFile(i)) ;
			Document doc = set.getDocument(i) ;
			doc.analyze(inspector) ;
			if(saveChange) {
				FileUtil.remove(new File(doc.getUrl()), false) ;
				String fileName = doc.getUrl().replace(".tagged", ".wtag") ;
				writer.write(fileName, doc) ;
			}
			//out.println("Done.................................................") ;
		}
	}
	
	IToken[] inspect(IToken[] token) {
		List<IToken> holder = new ArrayList<IToken>() ;
		for(int i = 0 ; i < token.length; i++) {
			IToken[] newToken = process(token, i) ;
			for(IToken sel : newToken) holder.add(sel) ;
		}
		return holder.toArray(new IToken[holder.size()]) ;
	}
	
	abstract protected IToken[] process(IToken[] token, int pos) ;

	static public void main(String[] args) throws Exception {
		final String[] WORDS = {"ngày", "tháng", "năm"} ;
		final String[] TOKENS = {"khoa học lao động"} ;
		final PrintStream out = new PrintStream(new FileOutputStream("review.txt"), true, "UTF-8") ;
		final Set<String> set = new HashSet<String>() ;
		TokenProcessor inspector = new TokenProcessor(System.out, "d:/vswdata3/nlp/wtag", false) {
			protected IToken[] process(IToken[] token, int pos) {
      	String oform = token[pos].getOriginalForm();
      	String nform = token[pos].getNormalizeForm() ;
      	String[] word = token[pos].getWord() ;
      	if(word.length == 1) {
      	} else if(word.length > 3) {
      		set.add(nform) ;
      	} else if(isMixWLetter(word)) {
      		set.add(nform) ;
      	}
      	return new IToken[]{token[pos]};
      }
			
			private boolean isMixWLetter(String[] word) {
				int wletter = 0; 
				for(int i = 0; i < word.length; i++) {
					if(CharacterSet.isWLetter(word[i])) wletter++ ;
				}
				if(wletter < word.length) return true ;
				return false ;
			}
			
		};
		inspector.process() ;
		Iterator<String> i = set.iterator() ;
		while(i.hasNext())out.println(i.next());
		out.close() ;
	}
}