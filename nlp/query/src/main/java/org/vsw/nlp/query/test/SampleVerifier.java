package org.vsw.nlp.query.test;

import org.vsw.nlp.query.QueryContext;

public class SampleVerifier {
	private Verifier[] verifier ;
	
	public SampleVerifier(String[] expect) {
		verifier = new Verifier[expect.length] ;
		for(int i = 0; i < expect.length; i++) {
			if(expect[i].startsWith("tag:")) {
				verifier[i] = new TagVerifier(expect[i].substring("tag:".length() + 1).trim()) ;
			} else {
				if(expect[i].startsWith("attr:")) expect[i] = expect[i].substring("attr:".length() + 1) ;
				int idx = expect[i].indexOf('=') ;
				String name = expect[i].substring(0, idx).trim() ;
				String value = expect[i].substring(idx + 1).trim() ;
				verifier[i] = new AttrVerifier(name, value) ;
			}
		}
	}
	
	public void verify(QueryContext context) throws Exception {
		for(Verifier sel : verifier) sel.verify(context) ;
	}
	
	static interface Verifier {
		public void verify(QueryContext context) throws Exception ;
	}
	
	static class TagVerifier implements Verifier {
		private String expectTag ;
		
		public TagVerifier(String expectTag) {
			this.expectTag = expectTag ;
		}
		
		public void verify(QueryContext context) throws Exception {
			if(!context.getTags().contains(expectTag)) {
				throw new Exception("Expect Tag " + expectTag) ;
			}
		}
	}
	
	static class AttrVerifier implements Verifier {
		private String name, expectValue ;
		
		public AttrVerifier(String name, String value) {
			this.name = name ;
			this.expectValue = value ;
		}
		
		public void verify(QueryContext context) throws Exception {
			String[] value = context.getAttributes().get(name) ;
			if(value != null) {
				for(String sel : value) {
					if(expectValue.equals(sel)) return ;
				}
			}
			throw new Exception("Expect " + name + " = " + expectValue) ;
		}
	}
}
