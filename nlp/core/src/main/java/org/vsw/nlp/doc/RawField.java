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
package org.vsw.nlp.doc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class RawField  {
	private String name ;
	private Map<String, String> attributes ;
	private List<RawSection> sections ;
	
	public RawField() { }
	
	public RawField(String name) {
		setName(name);
	}
	
	public RawField(String name, String data) {
		setName(name);
		add(data) ;
	}

	public String getName() { return name ; }
	public void setName(String name) { this.name = name ; }

	public void add(String section) {
		add(new RawSection(section)) ;
	}
	public void add(RawSection section) {
		if(sections == null) sections = new ArrayList<RawSection>() ;
		sections.add(section) ;
	}
	public List<RawSection> getSections() { return this.sections ; }
	public void setSections(List<RawSection> sections) { this.sections = sections ; }
	
	public Map<String, String> getAttributes() { return this.attributes ; }
	public void setAttributes(Map<String, String> attrs) { this.attributes = attrs ; }
	
	public String toSectionText() {
		StringBuilder b = new StringBuilder() ;
		if(sections != null) {
			for(int i = 0; i < sections.size(); i++) {
				RawSection section = sections.get(i) ;
				if(i > 0) b.append("\n\n") ;
				b.append(section.getSection()) ;
			}
		}
		return b.toString() ;
	}
	
	public String toString() { return toSectionText() ; }
}
