package org.vsw.nlp.query ;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
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
import org.vsw.util.StringUtil;

public class MatchRuleUnitTest {
	@Test
	public void test() {
		String EXP1 = ".3. synset{name = đại học} .0. name{otype=vnname, name} .3." ;
		String EXP2 = ".. synset{name = đại học} .0. name{otype=vnname, name}" ;
		String EXP3 = "synset{name = đại học} .0. name{otype=vnname, name} .3." ;
		print(StringUtil.split(EXP1, MatchRule.DISTANCE_PATTERN, true)) ;
		System.out.println("----------------------------------------");
		print(StringUtil.split(EXP2, MatchRule.DISTANCE_PATTERN, true)) ;
		System.out.println("----------------------------------------");
		print(StringUtil.split(EXP3, MatchRule.DISTANCE_PATTERN, true)) ;
	}
	
	private void print(List<String> holder) {
		for(String sel : holder) {
			System.out.println("==> " + sel);
		}
	}
	
	@Test
	public void testMatchRule() throws Exception {
		Dictionaries dict = new Dictionaries() ;
		QueryData document = new QueryData() ;
		TokenAnalyzer[] analyzer = {
	    PunctuationTokenAnalyzer.INSTANCE, new CommonTokenAnalyzer(), 
	    new LongestMatchingAnalyzer(dict), 
	    new DateTokenAnalyzer(), new TimeTokenAnalyzer(), 
	    new VNDTokenAnalyzer(), new USDTokenAnalyzer(),
	    new VNPhoneTokenAnalyzer(), new VNMobileTokenAnalyzer(),
	    new EmailTokenAnalyzer()
		}; 
		
		document.add("title", "liên hệ chúng tôi tuan.nguyen@gmail.com", analyzer) ;
		document.add("body", "liên hệ chúng tôi 0988922860", analyzer) ;
	
		assertRule(
		  "/p[title] synset{name=liên hệ, type=email} .. email{provider=*.com}", 
		  document, 
		  new String[]{ "" }) ;
		assertRule(
			"/p[body] synset{name=liên hệ} .1. phone{type=mobile}", 
			document, 
			null) ;
		assertRule(
			"/p[body] synset{name=liên hệ} .2. phone{type=mobile}", 
			document, 
			new String[]{ "" }) ;
		
		//assert document scope
		assertRule(
			"/p synset{name=liên hệ} .2. phone{type=mobile}", 
			document, 
			new String[]{ "" }) ;
	}
	
	private void assertRule(String rule, QueryData doc, String[] expect) throws Exception {
		MatchRule mrule = new MatchRule(rule) ;
		MatchResult[] mresults = mrule.matches(doc, 1) ;
		if(expect == null) {
			Assert.assertTrue(mresults == null) ;
		} else {
		  Assert.assertTrue(mresults != null && mresults.length == expect.length) ;
		}
	}	
}