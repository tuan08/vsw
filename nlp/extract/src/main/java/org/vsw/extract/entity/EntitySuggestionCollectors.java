package org.vsw.extract.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.util.VietnameseUtil;
import org.vsw.util.FileUtil;

public class EntitySuggestionCollectors {
	private String outdir ;
	private Map<String, EntitySuggestionCollector> collectors = new HashMap<String, EntitySuggestionCollector>() ;
	
	public EntitySuggestionCollectors(String outdir) {
		this.outdir = outdir ;
	}
	
	public void collect(QueryContext context) throws Exception {
		collect("person", context.getAttributes("person:")) ;
		collect("place", context.getAttributes("place:")) ;
		collect("organization", context.getAttributes("organization:")) ;
	}
	
	private void collect(String clazz, Map<String, String[]> attrs) throws Exception {
		if(attrs.size() == 0) return ;
		String type = attrs.get(clazz + ":id:prefix")[0] ;
		String file =  clazz + "/" + normalizeFileId(type) + ".json" ;
		EntitySuggestionCollector collector = collectors.get(file) ;
		if(collector == null) {
			FileUtil.mkdirs(outdir + "/" + clazz) ;
			collector = new EntitySuggestionCollector(clazz, outdir + "/" + file) ;
			collectors.put(file, collector) ;
		}
		collector.collect(attrs) ;
	}
	
	public void process(EntitySuggestionProcessor processor) throws Exception {
		Iterator<EntitySuggestionCollector> i = collectors.values().iterator() ;
		while(i.hasNext()) {
			EntitySuggestionCollector collector = i.next() ;
			collector.process(processor) ;
		}
	}
	
	private String normalizeFileId(String string) {
		StringBuilder b = new StringBuilder() ;
		char[] buf = string.toCharArray() ;
		for(int i = 0; i < buf.length; i++) {
			char c = buf[i] ;
			if(c == ' ') continue ;
			c = VietnameseUtil.removeVietnameseAccent(c) ;
			b.append(c) ;
		}
		return b.toString() ;
	}
}