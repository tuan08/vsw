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
package org.vsw.nlp.ml.crf.per;

import java.util.HashMap;
import java.util.Map;

import org.vsw.nlp.ml.crf.ent.EntitySetConfig;

public class PersonSetConfig extends EntitySetConfig{
	static Map<String, PersonSetConfig> CONFIGS = new HashMap<String, PersonSetConfig>() ;
	
	static {
		CONFIGS.put("np", new PersonSetConfig("np", new String[]{ "per:" }));		
	}
	
	public PersonSetConfig(String set, String[] tagPrefix) {
		super(set,tagPrefix);
	}
	
	static public PersonSetConfig getConfig(String name) {
		return CONFIGS.get(name) ;
	}
}
