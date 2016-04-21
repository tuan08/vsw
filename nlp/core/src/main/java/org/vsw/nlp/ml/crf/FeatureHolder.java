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
package org.vsw.nlp.ml.crf;

import java.util.ArrayList;
import java.util.List;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/

public class FeatureHolder {
	private List<String> features = new ArrayList<String>() ;
	private String       targetFeature ;
	
	public void reset() {
		features.clear() ;
		targetFeature = null ;
	}
	
	final public void addFeature(String feature) {
		features.add(feature) ;
	}
	
	public String[] getFeatures() {
		return features.toArray(new String[features.size()]) ;
	}
	
	public String getTargetFeature() { return this.targetFeature ; }
	public void setTargetFeature(String s) { this.targetFeature = s ; }
}
