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

import java.text.DecimalFormat;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class CurrencyTag extends QuantityTag {
	final static public String TYPE = "currency" ;
	
	static DecimalFormat FORMATER = new DecimalFormat("##########.00 ") ;
	
	private double amount ;

	public CurrencyTag(double amount, String unit) {
		super(TYPE);
		this.amount = amount ;
		setUnit(unit) ;
	}
	
	public double getAmount() { return this.amount  ; }
	
	public String getTagValue() { return FORMATER.format(amount)  + getUnit() ; }
}