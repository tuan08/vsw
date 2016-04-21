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
package org.vsw.nlp.ml.crf.ent.tag;

import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.UnitAbbreviation;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class CurrencyTag extends QuantityTag {
	final static public String TYPE = "qt:currency" ;
	
	final static public UnitAbbreviation[] CURRENCY =
		UnitAbbreviation.merge(UnitAbbreviation.VND, UnitAbbreviation.USD) ;
	
	static String[] UNITS = UnitAbbreviation.getUnits(CURRENCY) ;
	
	public CurrencyTag(IToken token) {
		if(token instanceof TokenCollection) {
			init((TokenCollection) token) ;
		} else {
			init(token) ;
		}
	}

	private void init(TokenCollection tseq) {
		Object[] token = getTokenValue(tseq) ;
		setQuantity(selectQuantity(token)) ;
		setUnit(selectUnit(token, UNITS, "vnd")) ;
	}

	private void init(IToken sel) {
		Object[] token = getTokenValue(sel) ;
		setQuantity(selectQuantity(token)) ;
		setUnit(selectUnit(token, UNITS, "vnd")) ;
	}
	
	public String getOType() { return TYPE ; }
	
  public boolean isTypeOf(String type) { return TYPE.equals(type); }
}
