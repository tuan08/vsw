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
package org.vsw.nlp.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.vsw.util.StringUtil;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class ParamHolder {
	private String name ;
	private Map<String, String[]> fields ;
	
	public ParamHolder(String expression) {
		fields = new HashMap<String, String[]>() ;
		expression = expression.trim() ;
		int tagSeparator = expression.indexOf('{') ;
		if(tagSeparator > 0) {
			this.name = expression.substring(0, tagSeparator).trim() ;
			String tags = expression.substring(tagSeparator + 1, expression.length() - 1) ;
			List<String> tagTmpHolder = StringUtil.split(tags, '|') ;
			for(int i = 0 ; i < tagTmpHolder.size(); i++) {
				String pvstring = tagTmpHolder.get(i) ;
				int idx = pvstring.indexOf('=') ;
				String fname = pvstring.substring(0, idx).trim() ;
				String values = pvstring.substring(idx + 1).trim() ;
				if(values.startsWith("\"\"") && values.endsWith("\"\"")) {
					values = values.substring(2, values.length() - 2) ;
					fields.put(fname, new String[] {values}) ;
				} else {
					String[] fvalue = values.split(",") ;
					for(int j = 0; j < fvalue.length; j++) fvalue[j] = fvalue[j].trim() ;
					fields.put(fname, fvalue) ;
				}
			}
		} else {
		  this.name = expression ;
		}
	}
	
	public String getName() { return this.name ; }
	
	public String[] getFieldValue(String name) { return fields.get(name) ; }
	
	public String getFirstFieldValue(String name) { 
		String[] value = fields.get(name) ; 
		if(value == null) return null ;
		return value[0] ;
	}
	
	public void setFieldValue(String name, String value) { 
		fields.put(name, new String[] {value}) ;
	}
	
	
	static public class RecordHolderIterator {
		BufferedReader breader ;
		
		public RecordHolderIterator(String file) throws IOException {
			this(new FileInputStream(file)) ;
		}
		
		public RecordHolderIterator(InputStream is) throws IOException {
			InputStreamReader reader = new InputStreamReader(is, StringUtil.UTF8) ;
			this.breader = new BufferedReader(reader) ;
		}
		
		public ParamHolder next() throws IOException {
			if(breader == null) return null ;
			int code = -1 ;
			StringBuilder b = new StringBuilder() ;
			char preChar = '\0', c = '\0';
			while((code = breader.read()) >= 0) {
				preChar = c ;
				c = (char) code ;
				b.append(c) ;
				if(c == ';' && preChar == ';') {
					b.setLength(b.length() - 2) ;
					return new ParamHolder(b.toString())  ;
				}
			}
			breader.close() ;
			breader = null ;
			String string = b.toString().trim() ;
			if(string.length() == 0) return null ;
			return new ParamHolder(string)  ;
		}
	}
}