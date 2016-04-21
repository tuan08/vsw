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
package org.vsw.nlp;

import org.junit.Assert;
import org.junit.Test;
import org.vsw.nlp.util.ParamHolder;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class TestParamHolder {
	@Test
	public void test() {
		ParamHolder rh = new ParamHolder("name") ;
		Assert.assertEquals("name", rh.getName()) ;
		
		rh = new ParamHolder("name {}") ;
		Assert.assertEquals("name", rh.getName()) ;
		
		rh = new ParamHolder("name{p1 = v1, v2 | p2 = v1, v2}") ;
		Assert.assertEquals("name", rh.getName()) ;
		Assert.assertArrayEquals(rh.getFieldValue("p1"), new String[]{"v1", "v2"}) ;
		Assert.assertArrayEquals(rh.getFieldValue("p2"), new String[]{"v1", "v2"}) ;
		
		rh = new ParamHolder("name{p1 = v1, v2 | p2 = \"\"v1, v2\"\"}") ;
		Assert.assertEquals("name", rh.getName()) ;
		Assert.assertArrayEquals(rh.getFieldValue("p1"), new String[]{"v1", "v2"}) ;
		Assert.assertArrayEquals(rh.getFieldValue("p2"), new String[]{"v1, v2"}) ;
	}
}
