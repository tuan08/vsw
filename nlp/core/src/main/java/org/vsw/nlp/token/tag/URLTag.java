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
package org.vsw.nlp.token.tag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class URLTag extends WordTag {
	final static public String TYPE = "url" ;
	final static public String[] KNOWN_DOMAINS = {
		".com", ".net", ".edu", ".org", ".mil", ".us", ".uk", ".fr", ".vn", ".ch", ".au"
	} ;
	final static public String[] KNOWN_PROTOCOL = {
		"http://", "https://", "fpt://", "file://"
	} ;
	
	private String url ;
	private String provider ;

	public URLTag(String url) {
		super(TYPE);
		this.url = url ;
	}
	
	public String getURL() { return this.url ; }

	public String getProvider() { return provider ; }
	
	public String getTagValue() { return url ; }
	
	static public boolean isEndWithKnownDomain(String string) {
		for(int i = 0; i < KNOWN_DOMAINS.length; i ++) {
			if(string.endsWith(KNOWN_DOMAINS[i])) return true ;
		}
		return false ;
	}
	
	static public boolean isStartWithKnownProtocol(String string) {
		for(int i = 0; i < KNOWN_PROTOCOL.length; i ++) {
			if(string.startsWith(KNOWN_PROTOCOL[i])) return true ;
		}
		return false ;
	}
}