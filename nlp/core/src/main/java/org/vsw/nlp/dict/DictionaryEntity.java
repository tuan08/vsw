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
import java.util.List;
import java.util.Map;

import org.vsw.nlp.meaning.Entity;
import org.vsw.nlp.meaning.Manager;
import org.vsw.nlp.token.tag.MeaningTag;
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
public class DictionaryEntity {
	private Map<String, Entities> entities = new HashMap<String, Entities>() ;
	private List<MeaningTag> entityTags = new ArrayList<MeaningTag>() ;
	private Manager<Entity> entityManager ;
	
	public DictionaryEntity(Manager<Entity> meaningManager) {
		this.entityManager = meaningManager ;
	}
	
	public Manager<Entity> getEntityManager() { return this.entityManager ; }
	
	public void add(Entity entity) {
		MeaningTag mtag = new MeaningTag(entity) ;
		entityTags.add(mtag) ;
		if(entity.getName() != null) {
			String name = entity.getName().toLowerCase() ;
			Entities holder = entities.get(name) ;
			if(holder == null) {
				entities.put(name, new Entities(mtag)) ;
			} else {
				holder.add(mtag) ;
			}
		}
	}
	
	public Entity[] find(String exp) { 
		List<Entity> holder = new ArrayList<Entity>() ;
		StringMatcher matcher = new StringMatcher(exp) ;
		for(int i = 0; i < entityTags.size(); i++) {
			MeaningTag tag = entityTags.get(i) ;
			Entity entity = tag.getEntity() ;
			String name = entity.getName() ;
			if(name != null) {
				if(matcher.matches(name.toLowerCase())) {
				  holder.add(entity) ;
				  continue ;
				}
			}
			String[] variant = entity.getVariant() ;
			if(variant != null) {
				for(String sel : variant) {
					if(matcher.matches(sel.toLowerCase())) {
						holder.add(entity) ;
						break ;
					}
				}
				continue ;
			}
			String topic  = entity.getTopic() ;
			if(topic != null && matcher.matches(topic.toLowerCase())) {
				holder.add(entity) ;
			}
		}
		return holder.toArray(new Entity[holder.size()]) ; 
	}
	
	public Entities getEntities(String name) { return entities.get(name) ; }
	
	static public class Entities {
		private MeaningTag[] tags ;
		private String[]     otypes ;
		
		Entities(MeaningTag tag) {
			tags = new MeaningTag[] {tag} ;
			otypes = new String[] { tag.getOType() } ;
		}
		
		public void add(MeaningTag tag) {
			MeaningTag[] newArray = new MeaningTag[tags.length + 1] ;
			System.arraycopy(tags, 0, newArray, 0, tags.length) ;
			newArray[tags.length] = tag ;
			tags = newArray ;
			otypes = StringUtil.merge(otypes, tag.getOType()) ;
		}
		
		public MeaningTag[] getMeaningTags() { return this.tags ; }

		public String[] getOTypes() { return this.otypes ; }
	}
}
