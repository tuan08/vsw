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
package org.vsw.nlp.ml.crf.ent;

import java.util.HashMap;
import java.util.Map;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class EntitySetConfig {
	static Map<String, EntitySetConfig> CONFIGS = new HashMap<String, EntitySetConfig>() ;
	
	static {
		CONFIGS.put(
				"np", 
				new EntitySetConfig("np", new String[]{ "org:", "loc:", "per:", "evt:" }));
		CONFIGS.put(
				"dimension", 
				new EntitySetConfig("dimension", new String[]{ "qt:dist:", "qt:sprc:", "qt:vol:"}));
		CONFIGS.put(
				"time", 
				new EntitySetConfig("time", new String[]{ "qt:time:", "time:"}));
		CONFIGS.put(
				"weight", 
				new EntitySetConfig("weight", new String[]{ "qt:weight:"}));
		CONFIGS.put(
				"speed", 
				new EntitySetConfig("speed", new String[]{ "qt:speed:"}));
		CONFIGS.put(
				"other", 
				new EntitySetConfig("other", new String[]{ "qt:bin:", "phone:", "percent:"}));
		
		CONFIGS.put(
				"num", 
				new EntitySetConfig("num", new String[]{ "time:", "phone:", "percent:", "qt:" }));
		CONFIGS.put(
				"all", 
				new EntitySetConfig("np", new String[]{ "org:", "loc:", "per:", "evt:", "time:", "phone:", "percent:", "qt:" }));
		CONFIGS.put("qtag", new EntitySetConfig("qtag", new String[]{ "qtag:"}));
	}
	
	final public String set ;
	final public String[] tagPrefix ;
	
	public EntitySetConfig(String set, String[] tagPrefix) {
		this.set = set ;
		this.tagPrefix = tagPrefix ;
	}
	
	static public EntitySetConfig getConfig(String name) {
		return CONFIGS.get(name) ;
	}
}