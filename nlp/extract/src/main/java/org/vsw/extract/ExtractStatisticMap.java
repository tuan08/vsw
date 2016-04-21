package org.vsw.extract;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.vsw.nlp.query.QueryContext;
import org.vsw.util.StatisticMap;

public class ExtractStatisticMap extends StatisticMap {
	public void incr(QueryContext context) {
		HashSet<String> tags = context.getTags() ;
		for(String sel : tags) {
			int idx = sel.indexOf('=') ;
			if(idx > 0) {
				sel = sel.substring(0, idx) ;
			}
			this.incr("Tag", sel, 1) ;
		}
		
		Iterator<Map.Entry<String, String[]>> i = 
			context.getAttributes().entrySet().iterator() ;
		while(i.hasNext()) {
			Map.Entry<String, String[]> entry = i.next() ;
			this.incr("Attribute", entry.getKey(), 1) ;
		}
	}
	
	public void incr(Document doc) {
		int length = doc.getDataLength() ;
		if(length == 0) {
			this.incr("Document", "Empty Text", 1) ;
		} else if(length < 100) {
			this.incr("Document", "< 100 character", 1) ;
		} else if(length < 500) {
			this.incr("Document", "< 500 character", 1) ;
		} else if(length < 1000) {
			this.incr("Document", "< 1000 character", 1) ;
		} else if(length < 5000) {
			this.incr("Document", "< 1000 character", 1) ;
		} else if(length < 5000) {
			this.incr("Document", "< 5000 character", 1) ;
		} else if(length < 10000) {
			this.incr("Document", "< 10000 character", 1) ;
		} else {
			this.incr("Document", "> 10000 character", 1) ;
		}
	}
	
	public void incr(Section[] sections) {
		for(Section section : sections) {
			String header = section.getHeader().toLowerCase() ;
			if(!"abstract".equals(header)) {
				if(header.length() > 50) {
					header = header.substring(0, 50).trim() ;
				}
				this.incr("Wiki Section Header", header, 1) ;
			}
		}
	}	
}