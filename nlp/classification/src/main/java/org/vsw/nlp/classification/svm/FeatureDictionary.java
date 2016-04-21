package org.vsw.nlp.classification.svm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import gnu.trove.TObjectIntHashMap;

public class FeatureDictionary {
	static  int NULL_VALUE = 0;
	
	private int featureIdTracker ;
	private TObjectIntHashMap featureIdMap = new TObjectIntHashMap() ;

	private int categoryIdTracker ;
	private Map<String, Integer> categoryIdMap = new LinkedHashMap<String, Integer>() ;

	public int getCategoryId(String category) {
		Integer value = categoryIdMap.get(category) ;
		if(value == null) {
			synchronized(categoryIdMap) {
				value = categoryIdTracker++ ;
				categoryIdMap.put(category, value) ;
			}
		}
		return value ;
	}
	
	public int getFeatureId(String feature) {
		int value = featureIdMap.get(feature) ;
		if(value == NULL_VALUE) {
			synchronized(featureIdMap) {
				value = ++featureIdTracker ;
				featureIdMap.put(feature, value) ;
			}
		}
		return  value ;
	}	
}
