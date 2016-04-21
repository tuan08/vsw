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
package org.vsw.nlp.meaning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.vsw.util.IOUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
abstract public class InMemoryManager<T extends Model> implements Manager<T> {
	private String[]       resources  ;
	private String         storePath ;
	private Set<T>         modifieds = new HashSet<T>() ; 
	private Map<String, T> models = new LinkedHashMap<String, T>() ; 
	
	public InMemoryManager(String[] resources, String storePath) throws Exception {
		init(resources, storePath) ;
	}
	
	void init(String[] resources, String storePath) throws Exception {
		this.resources  = resources ;
		this.storePath = storePath ;
		if(storePath != null && new File(storePath).exists()) {
			load("file:" + storePath);
			System.out.println("Load Store File.................") ;
		} else {
		  for(String selRes : resources) load(selRes);
		}
	}
	
	public void reload() throws Exception {
		modifieds.clear() ;
		models.clear() ;
		init(resources, storePath) ;
	}
	
	void load(String resource) throws Exception {
		InputStream is = IOUtil.loadRes(resource) ;
		JsonParser parser = Util.getJsonParser(is) ;
		while(parser.nextToken() != null) {
			T model = parser.readValueAs(getManageType()) ;
			models.put(model.getId(), model) ;
		}
		is.close() ;
	}
	
	public T[] getModifieds() { 
		return modifieds.toArray(newInstances(modifieds.size())) ; 
	}
	
	public void save(T model) throws Exception {
		if(storePath == null) {
			throw new Exception("This manager does not support the store function!") ;
		}
		modifieds.add(model) ;
		models.put(model.getId(), model) ;
	}
	
	public void remove(T object) throws Exception {
		modifieds.add(object) ;
		models.remove(object.getId()) ;
	}
	
	public void commit() throws Exception {
		if(storePath == null) {
			throw new Exception("This manager does not support the store function!") ;
		}
		PrintStream out = new PrintStream(new FileOutputStream(storePath), true, "UTF-8") ;
		JsonGenerator generator = Manager.Util.getJsonGenerator(out) ;
		Iterator<T> i = iterator() ;
		while(i.hasNext()) {
			T model = i.next() ;
			generator.writeObject(model) ;
		}
		generator.close() ;
		out.close() ;
		modifieds.clear() ;
	}
	
	abstract protected Class<T> getManageType()  ;
	abstract protected T newInstance()  ;
	abstract protected T[] newInstances(int size)  ;
	
	public Iterator<T> iterator() throws Exception {
		return models.values().iterator() ;
	}
}