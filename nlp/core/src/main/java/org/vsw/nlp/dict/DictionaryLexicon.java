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
package org.vsw.nlp.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.vsw.nlp.meaning.Lexicon;
import org.vsw.nlp.meaning.Manager;
import org.vsw.nlp.meaning.Synset;
import org.vsw.nlp.token.tag.LexiconTag;
import org.vsw.nlp.token.tag.SynsetTag;
import org.vsw.nlp.util.StringPool;
import org.vsw.util.StringMatcher;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class DictionaryLexicon {
	private WordTree root = new WordTree() ;
	private Map<String, Synset> synsets = new HashMap<String, Synset>() ;
	private Map<String, Entry>  entries = new HashMap<String, Entry>() ;
  private Manager<Lexicon> lexiconManager ;
  private Manager<Synset> synsetManager ;
   
  public DictionaryLexicon() { }
  
	public DictionaryLexicon(Manager<Lexicon> manager, Manager<Synset> synsetManager) {
		this.lexiconManager = manager ;
		this.synsetManager = synsetManager ;
	}
	
	public Manager<Lexicon> getLexiconManager() { return lexiconManager ; }
	
	public Manager<Synset> getSynsetManager() { return synsetManager ; }
	
	public  WordTree getWordTree() { return this.root ; }
	
	public Entry getEntry(String name) { return entries.get(name) ; }
	
	public Entry[] find(String name) { 
		List<Entry> holder = new ArrayList<Entry>() ;
		StringMatcher matcher = new StringMatcher(name) ;
		Iterator<Entry> i = entries.values().iterator() ;
		while(i.hasNext()) {
			Entry entry = i.next() ;
			if(matcher.matches(entry.getName())) holder.add(entry) ;
		}
		return holder.toArray(new Entry[holder.size()]) ; 
	}
	
	public void add(SynsetTag tag) {
		String[] array = StringUtil.merge(tag.getSynset().getVariant(), StringUtil.EMPTY_ARRAY) ;
		for(String sel : array) {
			String nName = sel.toLowerCase() ;
			String[] word = nName.split(" ") ;
			WordTree tree = root.find(word, 0, word.length) ;
			if(tree == null) {
				Lexicon lexicon = new Lexicon(nName, new String[] {"pos:X"});
				lexicon.setManageable(false) ;
				root.add(sel, word, 0, new LexiconTag(lexicon)) ;
				root.add(sel, word, 0, tag) ;
				//System.out.println(nName + " in synset " + tag.getSynset().getName() + " does not exist in the lexicon dict") ;
			} else {
				root.add(sel, word, 0, tag) ;
			}
		}
		synsets.put(tag.getSynset().getName(), tag.getSynset()) ;
	}
	
	public void add(String name, LexiconTag tag) {
		String nName = name.toLowerCase() ;
		String[] word = nName.split(" ") ;
		WordTree tree = root.find(word, 0, word.length) ;
		if(tree != null) {
			Entry entry = tree.getEntry() ;
			LexiconTag exist = entry.getFirstTagType(LexiconTag.class) ;
			if(exist != null) {
				exist.getLexicon().merge(tag.getLexicon()) ;
			} else entry.add(tag) ;
		} else {
			root.add(name, word, 0, tag) ;
		}
	}

	public void optimize(StringPool pool) {
		root.optimize(pool) ;
		this.entries.clear() ;
		root.collect(this.entries) ;
	}
}