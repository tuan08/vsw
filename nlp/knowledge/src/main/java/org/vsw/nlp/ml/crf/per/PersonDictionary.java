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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import org.vsw.util.IOUtil;

public class PersonDictionary {
	private HashSet<String> perdict = new HashSet<String>() ;
	
	public PersonDictionary() throws IOException {
		this("classpath:models/inpernames.txt");
	}
	
  public PersonDictionary(String res) throws IOException {
		InputStream is = IOUtil.loadRes(res) ;
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")) ;
		String line = null ;
		while((line = reader.readLine()) != null) {
			line = line.trim().toLowerCase() ;
			if(line.length() == 0) continue ;
			perdict.add(line);
		}
  }
  
  public boolean containPerson(String name, boolean normalize) { 
  	if(normalize) name = name.toLowerCase() ;
  	return perdict.contains(name) ; 
  } 
}
