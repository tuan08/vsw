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
package org.vsw.nlp.test;

import java.util.ArrayList;
import java.util.List;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.Tag;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class TokenVerifier {
	private String expectOrigForm ;
	private List<TagVerifier> expectTag = new ArrayList<TagVerifier>(3);

	public TokenVerifier(String exp) {
		if(exp.indexOf('{') > 0 && exp.indexOf('}') > 0) {
			int idx  = exp.indexOf('{') ;
			expectOrigForm = exp.substring(0, idx) ;
			String params = exp.substring(idx + 1, exp.length() - 1) ;
			String[] nameValue = params.split(",");
			for(int i = 0; i < nameValue.length; i++) {
				nameValue[i] = nameValue[i].trim() ;
				String name = nameValue[i], value = null ;
				int equalIdx = nameValue[i].indexOf('=') ;
				if(equalIdx > 0) {
					name = nameValue[i].substring(0, equalIdx).trim() ;
					value = nameValue[i].substring(equalIdx + 1).trim() ;
				}
				expectTag.add(new TagVerifier(name, value)) ;
			}
		} else {
			expectOrigForm = exp ;
		}
	}

	public void verify(IToken token) {
		if(!expectOrigForm.equals(token.getOriginalForm())) {
			throw new RuntimeException("Expect Token " + expectOrigForm + ", but " + token.getOriginalForm()) ;
		}

		List<Tag> tokenTag = token.getTag() ;
		for(int i = 0; i < expectTag.size(); i++) {
			TagVerifier tverifier = expectTag.get(i) ;
			boolean found = false ;
			for(int j = 0; j < tokenTag.size(); j++) {
				Tag tag = tokenTag.get(j) ;
				if(tverifier.matches(tag)) {
					found = true;
					break ;
				}
			}
			if(!found) {
				throw new RuntimeException("Expect tag " + tverifier + " token " + token.getOriginalForm()) ;
			}
		}
	}

	static class TagVerifier {
		private String otype ;
		private String expectValue ;

		public TagVerifier(String otype, String expectValue) {
			this.otype = otype ;
			this.expectValue = expectValue ;
		}

		public boolean matches(Tag tag) {
			if(!otype.equals(tag.getOType())) return false;
			if(expectValue == null) return true ;
			return expectValue.equals(tag.getTagValue()) ;
		}

		public String toString() {
			if(expectValue == null) return otype ;
			return otype + " = " + expectValue ;
		}
	}

}