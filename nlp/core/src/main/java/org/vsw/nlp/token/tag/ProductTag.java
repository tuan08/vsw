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

import org.vsw.nlp.topic.Product;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class ProductTag extends Tag {
	final static public String TYPE = "product" ;

	private Product product ;
	
	public ProductTag(Product product) {
		this.product = product ;
	}

	public Product getProduct() { return this.product ; }
	public void    setProduct(Product product) { this.product = product ; }
	
	public String getTagValue() { return product.getName() ; }

	public String getOType() { return TYPE ; }
	
  public boolean isTypeOf(String type) {
	  return TYPE.equals(type);
  }
}