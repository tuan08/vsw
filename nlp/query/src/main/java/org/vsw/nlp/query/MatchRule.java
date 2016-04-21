package org.vsw.nlp.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.vsw.nlp.query.matcher.CurrencyTagMatcher;
import org.vsw.nlp.query.matcher.DateTagMatcher;
import org.vsw.nlp.query.matcher.DigitTagMatcher;
import org.vsw.nlp.query.matcher.EmailTagMatcher;
import org.vsw.nlp.query.matcher.LexiconTagMatcher;
import org.vsw.nlp.query.matcher.NameTagMatcher;
import org.vsw.nlp.query.matcher.PhoneTagMatcher;
import org.vsw.nlp.query.matcher.PlaceTagMatcher;
import org.vsw.nlp.query.matcher.QuantityTagMatcher;
import org.vsw.nlp.query.matcher.SynsetTagMatcher;
import org.vsw.nlp.query.matcher.TagMatcher;
import org.vsw.nlp.query.matcher.TokenMatcher;
import org.vsw.nlp.query.matcher.WordTagMatcher;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.PhraseIterator;
import org.vsw.nlp.token.SentenceTokenizer;
import org.vsw.nlp.token.SingleSequenceIterator;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.TokenSequenceIterator;
import org.vsw.nlp.util.CharacterSet;
import org.vsw.nlp.util.ParamHolder;
import org.vsw.util.StringUtil;

public class MatchRule {
	final static public Pattern DISTANCE_PATTERN = Pattern.compile("\\.\\d*\\.") ;
  final static public int     ANY_DISTANCE     = 100000000 ;
	final static public byte    PHRASE = 1, SENTENCE = 2, LINE = 3, BLOCK = 4, DOCUMENT = 5 ;
	
	private byte         matchScope ;
	private int          beginRange =  0 ;
	private int          endRange   =  0 ;
	private String       fieldName ;
	private TagMatcher[] unitMatcher ;

	public MatchRule(String exp) throws Exception {
		exp = exp.trim() ;
		int idx = exp.indexOf(" ") ;
		String match = exp.substring(0, idx) ;
		exp = exp.substring(idx + 1).trim() ;
		
		if(match.startsWith("/p")) {
			this.matchScope = PHRASE ;
		} else if(match.startsWith("/s")) {
			this.matchScope = SENTENCE ;
		} else if(match.startsWith("/l")) {
			this.matchScope = LINE ;
		} else if(match.startsWith("/b")) {
			this.matchScope = BLOCK ;
		} else {
			this.matchScope = DOCUMENT ;
		}

		if(match.length() > 2 && match.endsWith("]")) {
			idx = match.indexOf("[") ;
			this.fieldName = match.substring(idx + 1, match.length() - 1) ;
		}

		List<TagMatcher> list = new ArrayList<TagMatcher>() ;

		List<String> tokens = StringUtil.split(exp, DISTANCE_PATTERN, true) ;
		String firstToken = tokens.get(0) ;
		if(DISTANCE_PATTERN.matcher(firstToken).matches()) {
			beginRange = getDistance(firstToken) ;
			tokens.remove(0) ;
		}
		String lastToken = tokens.get(tokens.size() - 1) ;
		if(DISTANCE_PATTERN.matcher(lastToken).matches()) {
			endRange = getDistance(lastToken) ;
			tokens.remove(tokens.size() -1) ;
		}
		idx = 0 ;
		while(idx < tokens.size()) {
			String token = tokens.get(idx).trim() ;
			String distance = null ;
			if(idx + 1 < tokens.size()) {
				idx++ ;
				distance = tokens.get(idx) ;
			}
			list.add(createTagMatcher(token, distance)) ;
			idx++ ;
		}
		this.unitMatcher = list.toArray(new TagMatcher[list.size()]) ;
	}

	final public MatchResult[] matches(QueryData doc, int maxReturn) throws Exception {
		List<MatchResult> holder = new ArrayList<MatchResult>() ;
		matches(holder, doc, maxReturn) ;
		if(holder.size() == 0) return null ;
		return holder.toArray(new MatchResult[holder.size()]) ;
	}
	
	final public void matches(List<MatchResult> holder, QueryData doc, int maxReturn) throws Exception {
		if(fieldName == null) {
			matchDoc(doc, holder, maxReturn) ;
		} else {
			QueryDataField field = doc.getDocumentField(fieldName) ;
			if(field != null) matchField(doc, field, holder, maxReturn) ;
		}
	}

	private void matchDoc(QueryData doc, List<MatchResult> holder, int maxReturn) throws Exception {
		Map<String, QueryDataField> fields = doc.getDocumentFields() ;
		Iterator<QueryDataField> i = fields.values().iterator() ;
		while(i.hasNext()) {
			QueryDataField field = i.next() ;
			matchField(doc, field, holder, maxReturn) ;
			if(holder.size() == maxReturn) return ;
		}
	}

	private void matchField(QueryData doc, QueryDataField field, List<MatchResult> holder, int maxReturn) throws Exception {
		TokenSequenceIterator tokensetIterator = null ;
		if(this.matchScope == PHRASE) {
			tokensetIterator = new PhraseIterator(field.getTokens()) ;
		} else if(this.matchScope == SENTENCE) {
			tokensetIterator = new SentenceTokenizer(field.getTokens()) ;
		} else if(this.matchScope == LINE) {
		} else if(this.matchScope == BLOCK) {
			tokensetIterator = null ;
		} else {
			tokensetIterator = new SingleSequenceIterator(field.getTokens()) ;
		}
		TokenCollection tokenSet = null ;
		while((tokenSet = tokensetIterator.next()) != null) {
			MatchResult mresult = match(tokenSet) ;
			if(mresult != null) {
				holder.add(mresult) ;
				if(holder.size() == maxReturn) return ;
			}
		}
	}

	private MatchResult match(TokenCollection tokenSet) {
		IToken[] tokens = tokenSet.getTokens() ;
		for(int i = 0; i < tokens.length; i++) {
			IToken token = tokens[i] ;
			if(unitMatcher[0].matches(token) != null) {
				int from = i ;
				if(unitMatcher.length ==  1) {
					return createMatchResult(tokenSet, i, i + 1);
				}
				int maxDistance = unitMatcher[0].getAllowNextMatchDistance() ;
				int distance = 0 ;
				int currentMatcher = 1 ;
				for(int j = i + 1; j < tokens.length; j++) {
					IToken tryToken = tokens[j] ;
					if(tryToken.getNormalizeForm().length() == 1) {
						char puncChar = tryToken.getNormalizeForm().charAt(0) ;
						if(CharacterSet.isNewLine(puncChar) || CharacterSet.isBlank(puncChar)) { 
							continue ;
						}
					}
					try{
						if(unitMatcher[currentMatcher].matches(tryToken) != null) {
							maxDistance = unitMatcher[currentMatcher].getAllowNextMatchDistance() ;
							distance = 0 ;
							currentMatcher++ ;
							if(currentMatcher == unitMatcher.length) {
								return createMatchResult(tokenSet, from, j + 1);
							}
						} else {
							distance++ ;
							if(distance >= maxDistance) break ;
						}
					} catch(Throwable ex) {
						StringBuilder b = new StringBuilder() ;
						StringBuilder b2 = new StringBuilder() ;
						b2.append("Match Rule:\n");
						b2.append("     ").append(b.toString()).append("\n");
						b2.append("Match:\n");
						b2.append("     ").append(tokenSet.getOriginalForm());
						//FileUtil.writeToFile("d://bug.out.txt", b2.toString().getBytes("UTF-8"), true) ;
						System.out.println(b2.toString());
						throw new RuntimeException(ex) ;
					}
				}
			}
		}
		return null ;
	}
	
	private MatchResult createMatchResult(TokenCollection ts, int from, int to) {
		TokenCollection newSequence = null ;
		IToken[] tokens = ts.getTokens() ;
		int start = from - this.beginRange ;
		int end   = to   + this.endRange ;
		if(start <= 0 && end >= tokens.length) {
			newSequence = ts ;
		} else {
		  if(start < 0) start = 0 ;
		  else from = from - start ;
		  if(end > tokens.length) end = tokens.length ;
		  newSequence = new TokenCollection(tokens, start, end) ;
		}
		return new MatchResult(newSequence, from, to);
	}
	
	static public TagMatcher createTagMatcher(String exp, String distanceString) throws Exception {
		int allowNextMatchDistance = getDistance(distanceString) ;
		ParamHolder pholder = new ParamHolder(exp) ;
		if("synset".equals(pholder.getName())) {
			return new SynsetTagMatcher().init(pholder, allowNextMatchDistance) ;
		} else if("place".equals(pholder.getName())) {
			return new PlaceTagMatcher().init(pholder, allowNextMatchDistance) ;
		} else if("phone".equals(pholder.getName())) {
			return new PhoneTagMatcher().init(pholder, allowNextMatchDistance) ;
		} else if("currency".equals(pholder.getName())) {
			return new CurrencyTagMatcher().init(pholder, allowNextMatchDistance) ;
		} else if("date".equals(pholder.getName())) {
			return new DateTagMatcher().init(pholder, allowNextMatchDistance) ;
		} else if("lexicon".equals(pholder.getName())) {
			return new LexiconTagMatcher().init(pholder, allowNextMatchDistance) ;
		} else if("email".equals(pholder.getName())) {
			return new EmailTagMatcher().init(pholder, allowNextMatchDistance) ;
		} else if("digit".equals(pholder.getName())) {
			return new DigitTagMatcher().init(pholder, allowNextMatchDistance) ;
		} else if("quantity".equals(pholder.getName())) {
			return new QuantityTagMatcher().init(pholder, allowNextMatchDistance) ;
		} else if("token".equals(pholder.getName())) {
			return new TokenMatcher().init(pholder, allowNextMatchDistance) ;
		} else if("word".equals(pholder.getName())) {
			return new WordTagMatcher().init(pholder, allowNextMatchDistance) ;
		} else if("name".equals(pholder.getName())) {
			return new NameTagMatcher().init(pholder, allowNextMatchDistance) ;
		}
		throw new Exception("Unknown matcher " + pholder.getName()) ;
	}
	
	static int getDistance(String dstring) {
		if(dstring == null || dstring.length() == 2) return ANY_DISTANCE ;
		dstring = dstring.substring(1, dstring.length() - 1) ;
		int allowNextMatchDistance = ANY_DISTANCE ;
		if(dstring != null && dstring.length() > 0) {
			allowNextMatchDistance = Integer.parseInt(dstring) ;
		}
		return allowNextMatchDistance ;
	}
}