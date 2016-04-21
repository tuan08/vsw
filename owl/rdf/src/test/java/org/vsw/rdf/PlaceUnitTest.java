package org.vsw.rdf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.junit.Before;
import org.junit.Test;
import org.vsw.nlp.topic.Place;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

public class PlaceUnitTest {
  @Before
  public void init() {
  }
  
	@Test
	public void run() throws Exception {
		ClassLoader cl = Thread.currentThread().getContextClassLoader() ;
		MappingJsonFactory factory = new MappingJsonFactory();
		factory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false); // all configuration before use
		JsonParser parser = factory.createJsonParser(cl.getResourceAsStream("data.place.json"));
		
		PrintStream os = new PrintStream(new FileOutputStream("target/places.n3"), true, "UTF-8") ;
		PrintWriter w = new PrintWriter(os) ;
		w.append("@prefix place:  <http://vsw.org/onthology/place/> .\n\n");
		int count = 0 ;
		while(count < 100 && parser.nextToken() != null) {
  		Place place =  parser.readValueAs(Place.class);
  		System.out.println("place: " + place.getHId()) ;
  		w.append("place:").append(place.getHId()).append('\n') ;
  		writeProperty(w, "  ", "place:name", place.getName()) ;
  		writeProperty(w, "  ", "place:variant", place.getVariant()) ;
  		writeProperty(w, "  ", "place:addressNumber", place.getAddressNumber()) ;
  		writeProperty(w, "  ", "place:street", place.getStreet()) ;
  		writeProperty(w, "  ", "place:quarter", place.getQuarter()) ;
  		writeProperty(w, "  ", "place:district", place.getDistrict()) ;
  		writeProperty(w, "  ", "place:city", place.getCity()) ;
  		writeProperty(w, "  ", "place:province", place.getProvince()) ;
  		writeProperty(w, "  ", "place:country", place.getCountry()) ;
  		writeProperty(w, "  ", "place:postalCode", place.getPostalCode()) ;
  		w.append("  .\n");
  		count++ ;
		}
		System.out.println("Load: " + count);
		w.flush() ;
		w.close() ;
		
		
		RDFDataSet mmanager = new InMemRDFDataSet() ;
		Model model = mmanager.getModel() ;
		model.read(new FileInputStream("target/places.n3"),  null, "N3") ;
	  
		//See http://www.openjena.org/ARQ/app_api.html
		String sparqlQueryString = 
			"PREFIX place: <http://vsw.org/onthology/place/>\n" +
			"SELECT ?name ?city \n" +
			"WHERE \n" + 
			"	  {\n" +
			"	    ?r place:name ?name .\n" +
			"	    OPTIONAL { ?r place:city ?city } " +
			"	  }" ;
    Query query = QueryFactory.create(sparqlQueryString) ;
    QueryExecution qexec = QueryExecutionFactory.create(query, mmanager.getDataset()) ;
    ResultSet results = qexec.execSelect() ;
    ResultSetFormatter.out(results) ;
    qexec.close() ;
    
    mmanager.close();
	}
	
	private void writeProperty(PrintWriter w, String indent, String pname, String[] array) {
		if(array == null) return ;
		w.append(indent).append(pname).append("    \"").append("\" ;\n") ;
	}
	
	private void writeProperty(PrintWriter w, String indent, String pname, String value) {
		if(value == null) return ;
		w.append(indent).append(pname).append("    \"").append(value).append("\" ;\n") ;
	}
}
