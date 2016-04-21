/*******************************************************************************
 * Copyright (c) 2005,2007 Cognium Systems SA and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Contributors:
 *     Cognium Systems SA - initial API and implementation
 *******************************************************************************/
package org.vsw.extract.wikimodel.wem ;

import org.wikimodel.wem.IWikiParser;
import org.wikimodel.wem.WikiParserException;
import org.wikimodel.wem.common.CommonWikiParser;
import org.wikimodel.wem.mediawiki.MediaWikiParser ;
/**
 * @author MikhailKotelnikov
 */
public class CommonWikiParserTest extends AbstractWikiParserTest {

	/**
	 * @param name
	 */
	public CommonWikiParserTest(String name) {
		super(name);
	}

	@Override
	protected IWikiParser newWikiParser() {
		return new CommonWikiParser();
	}

	public void testComplexFormatting() throws WikiParserException {
		test("%rdf:type toto:Document\r\n"
				+ "\r\n"
				+ "%title Hello World\r\n"
				+ "\r\n"
				+ "%summary This is a short description\r\n"
				+ "should this come as a property-value of summary??\r\n"		//TODO check.
				+ "%locatedIn (((\r\n"
				+ "    %type [City]\r\n"		// onReference for the square brackets.
				+ "    %name [Paris]\r\n"		// onReference
				+ "    %address (((\r\n"
				+ "      %building 10\r\n"
				+ "      %street Cité Nollez\r\n"
								+ "      %anotherprop (((\r\n"
								+ "         %property1 value1\r\n"
								+ "         %property2 value2\r\n"
				+ "      ))) \r\n"
				+ "    ))) \r\n"
				+ ")))\r\n"
				+ "= Hello World =\r\n"
				+ "\r\n"
				+ "* item one\r\n"
				+ "  * sub-item a\r\n"
				+ "  * sub-item b\r\n"
				+ "    + ordered X \r\n"
				+ "    + ordered Y\r\n"
				+ "  * sub-item c\r\n"
				+ "* item two\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "The table below contains \r\n"
				+ "an %seeAlso(embedded document). \r\n");
	}
}