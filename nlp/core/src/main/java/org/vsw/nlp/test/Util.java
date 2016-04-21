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
package org.vsw.nlp.test;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.vsw.nlp.dict.Dictionaries;
import org.vsw.nlp.ml.dict.LongestMatchingAnalyzer;
import org.vsw.nlp.token.analyzer.CommonTokenAnalyzer;
import org.vsw.nlp.token.analyzer.DateTokenAnalyzer;
import org.vsw.nlp.token.analyzer.EmailTokenAnalyzer;
import org.vsw.nlp.token.analyzer.PunctuationTokenAnalyzer;
import org.vsw.nlp.token.analyzer.TimeTokenAnalyzer;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;
import org.vsw.nlp.token.analyzer.USDTokenAnalyzer;
import org.vsw.nlp.token.analyzer.VNDTokenAnalyzer;
import org.vsw.nlp.token.analyzer.VNMobileTokenAnalyzer;
import org.vsw.nlp.token.analyzer.VNPhoneTokenAnalyzer;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class Util {
	static public TokenAnalyzer[] getTextAnalyzer(Dictionaries dictionaries) throws Exception {
		TokenAnalyzer[] analyzer = {
	    PunctuationTokenAnalyzer.INSTANCE, new CommonTokenAnalyzer(), 
	    new LongestMatchingAnalyzer(dictionaries), 
	    new DateTokenAnalyzer(), new TimeTokenAnalyzer(), 
	    new VNDTokenAnalyzer(), new USDTokenAnalyzer(),
	    new VNPhoneTokenAnalyzer(), new VNMobileTokenAnalyzer(),
	    new EmailTokenAnalyzer()
		};
		return analyzer ;
	}
	
	static public <T> T getInstance(String data, Class<T> type) throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		return mapper.readValue(data , type);
	}
}
