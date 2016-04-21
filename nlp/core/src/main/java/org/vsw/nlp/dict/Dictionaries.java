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

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.vsw.nlp.meaning.Entity;
import org.vsw.nlp.meaning.InMemoryManager;
import org.vsw.nlp.meaning.Lexicon;
import org.vsw.nlp.meaning.Manager;
import org.vsw.nlp.meaning.Synset;
import org.vsw.nlp.token.tag.LexiconTag;
import org.vsw.nlp.token.tag.SynsetTag;
import org.vsw.nlp.util.StringPool;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class Dictionaries {
	private DictionaryLexicon lexiconDict ;
	private DictionaryEntity  entityDict  ;
	private DictionaryVNName  vnnameDict  ;
	
	public Dictionaries() throws Exception {
		Manager<Lexicon> lexiconManager = new InMemoryManager<Lexicon>(Manager.LEXICON_RESOURCES, null) {
      protected Class<Lexicon> getManageType() { return Lexicon.class; }
      protected Lexicon newInstance() { return new Lexicon(); }
      protected Lexicon[] newInstances(int size) { return new Lexicon[size]; }
		};
		Manager<Synset> synsetManager = new InMemoryManager<Synset>(Manager.SYNSET_RESOURCES, null) {
      protected Class<Synset> getManageType() { return Synset.class; }
      protected Synset newInstance() { return new Synset(); }
      protected Synset[] newInstances(int size) { return new Synset[size]; }
		};
		Manager<Entity> meaningManager = new InMemoryManager<Entity>(Manager.MEANING_RESOURCES, null) {
      protected Class<Entity> getManageType() { return Entity.class; }
      protected Entity newInstance() { return new Entity(); }
      protected Entity[] newInstances(int size) { return new Entity[size]; }
		};
		init(lexiconManager, synsetManager, meaningManager) ;
	}


	public Dictionaries(Manager<Lexicon> lexiconManager,
			                Manager<Synset>  synsetManager,
			                Manager<Entity> meaningManager) throws Exception {
		init(lexiconManager, synsetManager, meaningManager) ;
	}
	
	public void init(Manager<Lexicon> lexiconManager,
		       	       Manager<Synset>  synsetManager,
			             Manager<Entity>  entityManager) throws Exception {
		lexiconDict = new DictionaryLexicon(lexiconManager, synsetManager);
		entityDict  = new DictionaryEntity(entityManager) ;
		vnnameDict  = new DictionaryVNName() ;
		addLexicon(lexiconManager.iterator()) ;
		addSynset(synsetManager.iterator()) ;
		addMeaning(entityManager.iterator()) ;
		optimize() ;
	}
	
	public void reload() throws Exception {
		Manager<Lexicon> lexiconManager = lexiconDict.getLexiconManager() ;
		Manager<Synset>  synsetManager = lexiconDict.getSynsetManager() ;
		Manager<Entity>  entityManager   = entityDict.getEntityManager() ;
		init(lexiconManager, synsetManager, entityManager) ;
	}
	
	public DictionaryLexicon getDictionaryLexicon() { return this.lexiconDict ; }

	public DictionaryEntity getDictionaryEntity() { return this.entityDict ; }
	
	public DictionaryVNName getDictionaryVNName() { return this.vnnameDict ; }
	
	public void add(Entity meaning) {
		entityDict.add(meaning) ;
		if(meaning.getName() != null) {
			String name = meaning.getName().toLowerCase() ;
			Lexicon lexicon = new Lexicon(name, new String[] { "pos:Np" });
			lexicon.setManageable(false);
			lexiconDict.add(name, new LexiconTag(lexicon)) ;
		}
	}
	
	public void add(Lexicon lexicon) {
		lexiconDict.add(lexicon.getName(), new LexiconTag(lexicon)) ;
	}
	
	void addLexicon(Iterator<Lexicon> i) {
		while(i.hasNext()) add(i.next()) ; 
	}
	
	void addSynset(Iterator<Synset> i) {
		while(i.hasNext()) add(i.next()) ; 
	}
	
	void addMeaning(Iterator<Entity> i) {
		while(i.hasNext()) add(i.next()) ; 
	}
	
	public void optimize() {
		StringPool pool = new StringPool() ;
		this.lexiconDict.optimize(pool)  ;
	}
	
	public void add(Synset synset) {
		lexiconDict.add(new SynsetTag(synset)) ;
	}
	
	static public JsonParser getJsonParser(InputStream is) throws JsonParseException, IOException {
		MappingJsonFactory factory = new MappingJsonFactory();
		factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false); // all configuration before use
		JsonParser jp = factory.createJsonParser(is);
		return jp ;
	}
}