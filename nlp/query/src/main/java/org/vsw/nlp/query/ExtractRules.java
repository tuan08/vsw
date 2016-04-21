package org.vsw.nlp.query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.vsw.nlp.query.matcher.TagMatcher;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.TokenCollection;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.util.StringUtil;

final public class ExtractRules {
	private MapTokenToField[] tokenField ;
	private MapValueToField[] constField ;
	private MapBlockToField[] blockField ;

	public ExtractRules(String[] exps) throws Exception {
		List<MapTokenToField> tokenFieldHolder = new ArrayList<MapTokenToField>() ;
		List<MapValueToField> constFieldHolder = new ArrayList<MapValueToField>() ;
		List<MapBlockToField> blockFieldHolder = new ArrayList<MapBlockToField>() ;
		for(int i = 0; i < exps.length; i++) {
			exps[i] = exps[i].trim() ;
			String[] value = null ;
			byte operation = ExtractField.SET ;
			if(exps[i].indexOf("+=") > 0) {
				value = exps[i].split("\\+=") ;
				operation = ExtractField.ADD ;
			} else if(exps[i].indexOf("f=") > 0) {
				value = exps[i].split("f=") ;
				operation = ExtractField.FINAL ;
			} else {
				value = exps[i].split("=") ;
			}

			String mapToField = value[0].trim() ;
			String extractValueExp = value[1].trim() ;
			if(extractValueExp.startsWith("$")) {
				extractValueExp = extractValueExp.substring(1) ;
				if(extractValueExp.startsWith("match")) {
					blockFieldHolder.add(new MapBlockToField(mapToField, extractValueExp, operation)) ;
				} else {
					tokenFieldHolder.add(new MapTokenToField(mapToField, extractValueExp, operation)) ;
				}
			} else {
				constFieldHolder.add(new MapValueToField(mapToField, extractValueExp, operation)) ;
			}
		}
		this.tokenField = tokenFieldHolder.toArray(new MapTokenToField[tokenFieldHolder.size()]) ;
		this.constField = constFieldHolder.toArray(new MapValueToField[constFieldHolder.size()]) ;
		this.blockField = blockFieldHolder.toArray(new MapBlockToField[blockFieldHolder.size()]) ;
	}

	public void extract(QueryContext context, MatchResult[] mresult) throws IOException {
		if(mresult == null) return ;
		for(int k = 0; k < constField.length; k++) {
			constField[k].map(context) ;
		}

		for(MatchResult sel : mresult) {
			TokenCollection seq = sel.getMatchTokenSequence() ;
			IToken[] tokens = seq.getTokens() ;
			int startField = 0 ;
			for(int i = 0; i < tokens.length; i++) {
				for(int k = startField; k < tokenField.length; k++) {
					if(tokenField[k].map(context, tokens[i])) {
						startField = k + 1 ;
						break ;
					}
				}
			}
		}

		for(MatchResult sel : mresult) {
			for(int k = 0; k < blockField.length; k++) {
				if(blockField[k].map(context, sel)) {
					break ;
				}
			}
		}
	}

	abstract static public class ExtractField {
		final static byte ADD         = 1 ;
		final static byte SET         = 2 ;
		final static byte FINAL       = 3 ;
		
		protected byte   operation ;
		protected String fieldName ;
		private boolean  setattr =  true ;
		private boolean  settag = false ;
		
		public ExtractField(String fieldName, byte operation) {
			this.operation = operation ;
			if(fieldName.startsWith("tag:")) {
	    	fieldName = fieldName.substring("tag:".length()).trim() ;
	    	settag = true ; setattr = false ;
			} else if(fieldName.startsWith("tag+attr:")) {
	    	this.fieldName = fieldName.substring("tag+attr:".length()).trim() ;
	    	settag = true ; setattr = true ;
			} else {
				this.fieldName = fieldName ;
	    	settag = false ; setattr = true ;
	    }
		}

		protected void set(QueryContext context, String fieldName, String value) throws IOException  {
			if(operation == ADD) {
				String[] oldValue = context.getQueryAttribute(fieldName) ;
				if(oldValue != null) {
					context.set(fieldName, StringUtil.join(oldValue, new String[] { value.trim() }), setattr, settag) ;
				} else {
					context.set(fieldName, new String[] {value.trim()}, setattr, settag) ;
				}
			} else if(operation == FINAL) {
				if(context.getQueryAttribute(fieldName) == null) {
					context.set(fieldName, new String[] {value.trim()}, setattr, settag) ;
				}
			} else {
				context.set(fieldName, new String[] {value.trim()}, setattr, settag) ;
			}
		}
	}

	static public class MapValueToField extends ExtractField {
		private String value ;

		public MapValueToField(String fieldName, String value, byte operation) throws Exception {
			super(fieldName, operation);
			this.value = value ;
		}

		public boolean map(QueryContext context) throws IOException {
			set(context, fieldName, value) ;
			return true ;
		}
	}

	static public class MapTokenToField extends ExtractField {
		static byte SELECT_TAG   = 1 ;
		static byte SELECT_TOKEN = 2 ;
		
		private TagMatcher tagMatcher ;
		private byte selectValue = SELECT_TAG ;

		public MapTokenToField(String fieldName, String exp, byte operation) throws Exception{
			super(fieldName, operation);
			exp = exp.trim() ;
			if(exp.startsWith("$")) exp = exp.substring(1) ;
			if(exp.endsWith(".token")) {
				selectValue = SELECT_TOKEN ;
				exp = exp.substring(0, exp.length() - ".token".length()) ;
			}
			tagMatcher = MatchRule.createTagMatcher(exp, null) ;
		}

		public boolean map(QueryContext context, IToken token) throws IOException {
			Tag tag = tagMatcher.matches(token) ;
			if(tag == null) return false ;
			String setValue = tag.getTagValue() ;
			if(selectValue == SELECT_TOKEN) setValue = token.getNormalizeForm() ;
			if(setValue == null) return false ;
			set(context, fieldName, setValue) ;
			return false ;
		}
	}

	static public class MapBlockToField extends ExtractField {
		final static byte MATCH = 0 ;
		final static byte SMART = 1 ;
		final static byte BLOCK = 2 ;
		
		private String matchExp ;

		public MapBlockToField(String fieldName, String exp, byte operation) throws Exception {
			super(fieldName, operation) ;
			if(exp.startsWith("match{") && exp.endsWith("}")) {
				matchExp = exp.substring("match{".length(), exp.length() - 1) ;
			}
		}

		//match{from..to}, match{begin..end}, match{to..end} 
		public boolean map(QueryContext context, MatchResult mresult) throws IOException {
			TokenCollection tsequence = mresult.getMatchTokenSequence() ;
			String value = null ;
			if("begin..from".equals(matchExp)) {
				value = tsequence.getOriginalForm(0, mresult.getFrom());
			} else if("begin..to".equals(matchExp)) {
				value = tsequence.getOriginalForm(0, mresult.getTo());
		  } else if("from..to".equals(matchExp)) {
				value = tsequence.getOriginalForm(mresult.getFrom(), mresult.getTo());
		  } else if("from..end".equals(matchExp)) {
				value = tsequence.getOriginalForm(mresult.getFrom(), tsequence.getTokens().length);
		  } else if("to..end".equals(matchExp)) {
				value = tsequence.getOriginalForm(mresult.getTo(), tsequence.getTokens().length);
			} else {
				value = tsequence.getOriginalForm() ;
			}
			set(context, fieldName, value) ;
			return true ;
		}
	}
}