package org.vsw.extract.entity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.vsw.hadoop.util.HDFSUtil;
import org.vsw.hadoop.util.SequenceFileUtil;

public class EntitySuggestionCollector {
static DefaultCodec CODEC = new DefaultCodec() ;
	
	private String clazz ;
	private String storeFile ;
	private SequenceFileUtil<Text, EntitySuggestion> tmpStoreFile ;
	private SequenceFileUtil<Text, EntitySuggestion>.Writer writer ;
	
	public EntitySuggestionCollector(String clazz, String file) throws Exception {
    this.clazz = clazz ;
		this.storeFile = file ;
		FileSystem fs = FileSystem.get(HDFSUtil.getDaultConfiguration()) ;
    this.tmpStoreFile = 
    	new SequenceFileUtil<Text, EntitySuggestion>(fs, file + ".tmp", Text.class, EntitySuggestion.class) ;
    this.writer = tmpStoreFile.getWriter(true) ;
	}
	
	public void collect(Map<String, String[]> attrs) throws Exception {
		String prefix = attrs.remove(clazz + ":id:prefix")[0] ;
		String name = attrs.remove(clazz + ":id:name")[0] ;
		String[] pname = getPropertyNames(attrs) ;
		EntitySuggestion entityMapping = new EntitySuggestion() ;
		entityMapping.setCType(clazz) ;
		entityMapping.setPrefix(prefix) ;
		entityMapping.setName(name) ;
		
		for(int i = 0; i < pname.length; i++) {
			PropertySuggestion suggestion = new PropertySuggestion() ;
			String suggestionName = pname[i].substring(clazz.length() + 1) ;
			String suggestionValue = attrs.get(pname[i])[0] ;
			suggestion.setName(suggestionName) ;
			suggestion.addCandidate(suggestionValue) ;
			suggestion.addSample(attrs.get(pname[i] + ":sample")[0]) ;
			entityMapping.addSuggestion(suggestion) ;
		}
		this.writer.append(new Text(entityMapping.computeId()), entityMapping) ;
	}
	
	public String[] getPropertyNames(Map<String, String[]> attrs) {
		Iterator<String> i = attrs.keySet().iterator() ;
		HashSet<String> holder = new HashSet<String>();
		while(i.hasNext()) {
			String key = i.next() ;
			if(key.endsWith(":sample")) continue ;
			holder.add(key) ;
		}
		return holder.toArray(new String[holder.size()]) ;
	}
	
	public void process(EntitySuggestionProcessor processor) throws Exception {
		System.out.print("Merge the entities for class " + clazz + ":") ;
		writer.close() ;
		SequenceFile.Reader reader = tmpStoreFile.getReader() ;
		EntitySuggestion previous = null ;
		JSONWriter<EntitySuggestion> writer = 
			new JSONWriter<EntitySuggestion>(storeFile, EntitySuggestion.class) ;
	  int count = 0 ;
	  Text key = new Text() ;
		while(true) {
			EntitySuggestion current = new EntitySuggestion() ;
			if(reader.next(key, current)) {
				if(previous != null && previous.simmilar(current)) {
					previous.merge(current) ;
				} else {
					if(previous != null) {
						previous = processor.process(previous) ;
 						if(previous != null)writer.write(previous);
					}
					previous = current ;
				}
				if(count > 0 && count % 1000 == 0) System.out.print(".") ;
				count++ ;
			} else {
				break ;
			}
		}
		if(previous != null) {
			previous = processor.process(previous) ;
			if(previous != null)writer.write(previous);
		}
		reader.close() ;
		writer.close() ;
		this.tmpStoreFile.delete() ;
		System.out.println() ;
		System.out.println("Count: " + count) ;
	}
}