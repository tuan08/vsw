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
package org.vsw.nlp.ml;

import org.vsw.nlp.token.tag.Tag;
import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class BoundaryTag extends Tag {
	final static public String TYPE = "boundary" ;
	protected String[] feature ;
	
	public BoundaryTag(String[] feature) {
		this.feature = feature ;
	}
	
	public BoundaryTag(String feature) {
		this.feature = new String[] {feature} ;
	}
	
	public String[] getFeatures() { return this.feature ; }
	
	public void addFeature(String afeature) {
		if(feature == null || feature.length == 0) {
			feature = new String[] { afeature } ;
		} else {
			feature = StringUtil.merge(feature, afeature) ;
		}
	}
	
	public void removeFeatureWithPrefix(String afeature) {
		this.feature = StringUtil.removeStringWithPrefix(feature, afeature) ;
	}
	
	public void clear() { feature = StringUtil.EMPTY_ARRAY ; }
	
	public String getOType() { return TYPE ; }
	
	public boolean isTypeOf(String type) { return getOType().equals(type)  ; }
	
	public String getInfo() {
		StringBuilder b = new StringBuilder() ;
		b.append(getOType()).append(": {") ;
		for(int i = 0; i < feature.length; i++) {
			b.append(feature[i]) ;
			if(i + 1 < feature.length) b.append(", ") ;
		}
		b.append("}") ;
		return b.toString() ;
	}
}
