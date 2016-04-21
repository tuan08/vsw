package org.vsw.nlp.index;

import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
/**
 * @author tuan
 * field id:    id
 * field date:  created:date
 * field vntxt: content:vntxt
 * field long:  number:long
 * field dbl:   price:dbl
 */
public class IndexDocument  {
	private Document document = new Document() ;

	public Document getIndexDocument() { return this.document ; }
	
	public void add(String fname, String value) {
		if(value == null) return  ;
    Field lfield = new Field(fname, new WordTokenStream(value)) ;
    document.add(lfield) ;
	}
	
	public void add(String fname, String[] value) {
		if(value == null || value.length == 0) return  ;
    Field lfield = new Field(fname, new WordTokenStream(value)) ;
    document.add(lfield) ;
	}
	
	public void add(String fname, int value) {
    NumericField lfield = new NumericField(fname, Field.Store.YES, true) ;
    lfield.setIntValue(value) ;
    lfield.setOmitNorms(true) ;
    document.add(lfield) ;
	}
	
	public void add(String fname, long value) {
		Field.Store fieldStore = Field.Store.YES  ;
    NumericField lfield = new NumericField(fname, fieldStore, true) ;
    lfield.setLongValue(value) ;
    lfield.setOmitNorms(true) ;
    document.add(lfield) ;
	}
	
	public void add(String fname, Date value) {
		Field.Store fieldStore = Field.Store.YES  ;
    NumericField lfield = new NumericField(fname, fieldStore, true) ;
    lfield.setLongValue(value.getTime()) ;
    lfield.setOmitNorms(true) ;
    document.add(lfield) ;
	}
	
	public void add(String fname, float value) {
		Field.Store fieldStore = Field.Store.YES  ;
    NumericField lfield = new NumericField(fname, fieldStore, true) ;
    lfield.setFloatValue(value) ;
    lfield.setOmitNorms(true) ;
    document.add(lfield) ;
	}
	
	public void add(String fname, double value) {
		Field.Store fieldStore = Field.Store.YES  ;
    NumericField lfield = new NumericField(fname, fieldStore, true) ;
    lfield.setDoubleValue(value) ;
    lfield.setOmitNorms(true) ;
    document.add(lfield) ;
	}
	
	public void add(String fname, byte[] data) {
		Field lfield = new Field(fname, data, Field.Store.YES) ;
		document.add(lfield) ;
	}
}