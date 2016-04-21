package org.vsw.nlp.query;

import org.vsw.nlp.dict.Dictionaries;
import org.vsw.nlp.test.Util;
import org.vsw.nlp.token.analyzer.TokenAnalyzer;

abstract public class BaseQueryUnitTest {
	final static public String LIENHE_QUERY = 
		"{" +
		"  \"name\" : \"test query\" ," +
		"  \"priority\":  1 ," +
		"  \"description\": \"extract the place information\"," +
		"  \"matchmax\" : 3 ," +
		"  \"matchselector\" : \"first\" ," +
		"  \"prematch\": [" +
		"    \"msg: call prematch for query test query\"" +
		"  ]," +
		"  \"match\": [" +
		"    \"/p synset{name=liên hệ} .. digit .2. place{type=street}\"," +
		"    \"/p synset{name=liên hệ} .. place{type=street, district, quarter} .. place{type=district, quarter, city}\"" +
		"  ]," +
		"  \"extract\": [" +
		"    \"tag+attr: lienhe:diadiem:duong    f=  $place{type=street}\"," +
		"    \"tag+attr: lienhe:diadiem:phuong   f=  $place{type=quarter]}\"," +
		"    \"tag+attr: lienhe:diadiem:quan     f=  $place{type=district}\"," +
		"    \"tag+attr: lienhe:diadiem:thanhpho f=  $place{type=city}\"," +
		"    \"tag+attr: lienhe:diadiem:tinh     f=  $place{type=province}\"," +
		"    \"tag+attr: lienhe:diadiem:quocgia  f=  $place{type=country}\"" +
		"  ]," +
		"  \"postmatch\": [" +
		"    \"tag: tageval\", " +
		"    \"msg: call postmatch for query test query\", " +
		"    \"print: tag, attr\", " +
		"    \"if: $tagcount{lienhe:diadiem:*} > 3 ? $msg{tag count > 3} : $msg{tag count < 3}\"" +
		"  ]"  +
		"}" ;

	protected QueryData createDocument(String ... fields) throws Exception {
		QueryData document = new QueryData() ;
		TokenAnalyzer[] analyzer = Util.getTextAnalyzer(new Dictionaries()) ;
		for(String selField : fields) {
			int idx      = selField.indexOf(':') ;
		  String fname = selField.substring(0, idx).trim() ;
		  String data  = selField.substring(idx + 1).trim() ;
			document.add(fname, data, analyzer) ;
		}
		return document ;
	}
}