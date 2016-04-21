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
package org.vsw.nlp;

import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import org.vsw.nlp.dict.Dictionaries;
import org.vsw.util.IOUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class NLPResource {
	static NLPResource instance = null ;
	static int instanceCount = 0 ;
	
	private Dictionaries dictionaries ;
	private Map<String, Object> models = new HashMap<String, Object>() ;

	public NLPResource() throws Exception {
		if(instanceCount > 0) {
			new Exception("WARNING: create more than one NLPResource instance").printStackTrace() ;
		}
		this.dictionaries = new Dictionaries() ;
		instanceCount++ ;
	}
	
	public NLPResource(Dictionaries dictionaries) {
		if(instanceCount > 0) {
			new Exception("WARNING: create more than one NLPResource instance").printStackTrace() ;
		}
		this.dictionaries = dictionaries ;
		instanceCount++ ;
	}
	
	public Dictionaries getDictionaries() { return this.dictionaries ; }
	public void setDictionaries(Dictionaries dictionaries) {
		this.dictionaries = dictionaries ;
	}
	
	public <T> T getObject(String path) {
		try {
			String key = path ;
			int colon = key.indexOf(':') ;
			if(colon > 0) key =  key.substring(colon + 1) ;
			Object model = models.get(key) ;
			if(model == null) {
				ObjectInputStream s = new ObjectInputStream(IOUtil.loadRes(path));
				model =  s.readObject();
				s.close();
				models.put(key, model) ;
			}
			return (T) model ;
		} catch(Exception ex) {
			throw new RuntimeException(ex) ;
		}
	}

	static public NLPResource getInstance() {
		if(instance == null) {
			try {
				instance = new NLPResource() ;
			} catch(Exception ex) {
				throw new RuntimeException(ex) ;
			}
		}
		return instance ;
	}
}
