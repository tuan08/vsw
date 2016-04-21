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

import java.io.IOException;

import org.vsw.nlp.ml.crf.FeatureGenerator;
import org.vsw.nlp.ml.crf.FeatureHolder;
import org.vsw.nlp.token.IToken;

public class NameDictFeatureGenerator implements FeatureGenerator {
	private VNNameDictionary vdict ;
	private INNameDictionary idict ;
  private VnSyllParser vnparser;

	public NameDictFeatureGenerator() throws IOException {
	  vdict = new VNNameDictionary() ;
	  idict = new INNameDictionary();
    vnparser = new VnSyllParser();
	}
	
	public void generate(IToken[] token, int pos, FeatureHolder holder) {
		char fletter = token[pos].getOriginalForm().charAt(0) ;
		if((fletter >= 'A' && fletter <= 'Z') || Character.isUpperCase(fletter)) {
			boolean vlast = false , vfirst = false , vmiddle = false ;
			boolean ilast = false , ifirst = false , imiddle = false ;
			String[] word = token[pos].getWord() ;
			for(String sel : word) {
			  if(!vfirst && vdict.containFirstName(sel, false )) vfirst = true ;
			  if(!vlast && vdict.containLastName(sel, false )) vlast = true ;
			  if(!vmiddle && vdict.containMiddleName(sel, false )) vmiddle = true ;
			  
			  if(!ifirst && idict.containFirstName(sel, false )) ifirst = true ;
        if(!ilast && idict.containLastName(sel, false )) ilast = true ;
        if(!imiddle && idict.containMiddleName(sel, false )) imiddle = true ;        
			}
			
			int limit = 0 ;
			if(vlast) {
				holder.addFeature("lst:vn") ;
				limit++ ;
			}
			if(vfirst && word.length > limit)  {
				holder.addFeature("fst:vn") ;
				limit++ ;
			}
			if(ilast && word.length > limit) {
        holder.addFeature("lst:in") ;
        limit++ ;
      }
      if(ifirst && word.length > limit)  {
        holder.addFeature("fst:in") ;
        limit++ ;
      }
			if(vmiddle && word.length > limit) {
			  holder.addFeature("mid:vn") ;
			  limit++ ;
			}			  
			if(imiddle && word.length > limit) holder.addFeature("mid:in") ;
			
			vnparser.parseVnSyllable(token[pos].getNormalizeForm());
	    if (vnparser.isValidVnSyllable()) {
	      holder.addFeature("vsyl");
	    } else {
	      holder.addFeature("isyl");
	    }
		}
	}
}