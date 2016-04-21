package org.vsw.nlp.classification;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class VuClassifiedDocumentReader extends ClassifiedDocumentReader {
	static String[] CATEGORIES = {
		"0-Điện thoại di động",  "1-Máy tính xách tay (laptop)",  "2-Tivi",  "3-Máy ảnh"
	} ;
	
	private List<Descriptor> descriptors = new ArrayList<Descriptor>() ;
	private int currentPos ;
	
	public VuClassifiedDocumentReader(String dataDir) throws Exception {
		for(String selCat : CATEGORIES) {
			File catDir = new File(dataDir + "/" + selCat) ;
			String category = selCat.substring(2) ;
			File[] file = catDir.listFiles() ;
			for(int i = 0; i < file.length; i++) {
			  descriptors.add(new Descriptor(category, file[i].getAbsolutePath())) ;
			}
		}
	}
	
	public ClassifiedDocument read() throws Exception {
		if(currentPos >= descriptors.size()) return null ;
		Descriptor descriptor = descriptors.get(currentPos++) ;
  	ClassifiedDocument doc = read(descriptor.path) ;
  	doc.setCategory(descriptor.category) ;
  	doc.setPath(descriptor.path) ;
  	return doc ;
  }
	
	static public ClassifiedDocument read(String file) throws Exception {
  	ClassifiedDocument doc = new ClassifiedDocument() ;
  	BufferedReader reader = 
  		new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")) ;
  	String line = null ;
  	String title = null; 
  	StringBuilder body = new StringBuilder() ;
  	while((line = reader.readLine()) != null) {
  		line = line.trim() ;
  		if(title == null && line.length() > 0) {
  			title = line; 
  		} else if(line.length() > 0) {
  			body.append(line).append("\n") ;
  		}
  	}
  	doc.setTitle(title) ;
  	doc.add("body", body.toString()) ;
  	return doc ;
  }
	
	static public class Descriptor {
		final public String category ;
		final public String path ;
		
		public Descriptor(String category, String path) {
			this.category = category ;
			this.path = path ;
		}
	}
	
	static public void main(String[] args) throws Exception {
		ClassifiedDocumentReader reader = new VuClassifiedDocumentReader("d:/vusvm/svmdata/Data") ;
		ClassifiedDocument doc = null ;
		int count = 0 ;
		while((doc = reader.read()) != null) {
			System.out.println(doc.getTitle());
			count++ ;
		}
		System.out.println("Count: " + count);
	}
}