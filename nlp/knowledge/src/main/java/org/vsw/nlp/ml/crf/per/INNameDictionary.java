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

public class INNameDictionary {
	private HashSet<String> lnames = new HashSet<String>() ;
	private HashSet<String> fnames = new HashSet<String>() ;
	private HashSet<String> mnames = new HashSet<String>() ;

	public INNameDictionary() throws IOException {
		this("classpath:models/inpernames.txt");
	}
	
  public INNameDictionary(String res) throws IOException {
		InputStream is = IOUtil.loadRes(res) ;
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")) ;
		String line = null ;
		while((line = reader.readLine()) != null) {
			line = line.trim().toLowerCase() ;
			if(line.length() == 0) continue ;
			String[] word = line.split(" ") ;
			fnames.add(word[0]);
			if(word.length > 1) {
				lnames.add(word[word.length -1]) ;
			}
			for(int i = 1; i < word.length - 1; i++) {
				mnames.add(word[i]) ;
			}
		}
  }
  
  public boolean containLastName(String name, boolean normalize) { 
  	if(normalize) name = name.toLowerCase() ;
  	return lnames.contains(name) ; 
  }
  
  public boolean containFirstName(String name, boolean normalize) { 
  	if(normalize) name = name.toLowerCase() ;
  	return fnames.contains(name) ; 
  }
  
  public boolean containMiddleName(String name, boolean normalize) { 
  	if(normalize) name = name.toLowerCase() ;
  	return mnames.contains(name) ; 
  }
}
