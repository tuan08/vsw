package org.vsw.nlp.classification.svm;

import java.io.PrintStream;

import org.vsw.nlp.token.IToken;

import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntIntProcedure;

public class FeatureVector {
	private String category ;
	private int categoryId ;
	private TIntIntHashMap features = new TIntIntHashMap() ;
	
	private FeatureDictionary fdict; 
	
	public FeatureVector(String category, FeatureDictionary fdict) {
		this.category = category ;
		this.categoryId = fdict.getCategoryId(category);
		this.fdict = fdict ;
	}
	
	public String getCategory() { return this.category ; }
	
	public int getCategoryId() { return this.categoryId ; }
	
	public TIntIntHashMap getFeatures() { return this.features ; }
	
	public void addFeature(String field, String word) {
		String feature = field + ":" + word ;
		int featureId = fdict.getFeatureId(feature) ;
		int freq = features.get(featureId) ;
		features.put(featureId, freq + 1) ;
	}
	
	public void addFeature(String field, String[] word) {
		for(int i = 0; i < word.length; i++) {
			addFeature(field, word[i]) ;
		}
	}
	
	public void addFeature(String field, IToken[] token) {
		for(int i = 0; i < token.length; i++) {
			addFeature(field, token[i].getOriginalForm()) ;
		}
	}
	
	public void print(final PrintStream p) throws Exception {
		p.print(categoryId) ;
		features.forEachEntry(new TIntIntProcedure() {
      public boolean execute(int key, int value) {
      	p.append(' '); p.print(key) ;p.print(':') ; p.print(value) ;
      	return true ;
      }
		}) ;
		p.println() ;
	}
}