package org.vsw.rdf;

import java.io.FileInputStream;

import org.junit.Test;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

public class RDFDataSetUnitTest {
	@Test
	public void test() throws Exception {
		//ModelManager mmanager = new InMemModelManager() ;
		RDFDataSet mmanager = new TDBRDFDataSet("target/TDB") ;
		Model model = mmanager.getModel() ;
		model.read(new FileInputStream("src/test/resources/books.n3"),  null, "N3") ;
		model.read(new FileInputStream("src/test/resources/people.n3"), null, "N3") ;
		model.write(System.out, "N3") ;

	  //See http://www.openjena.org/ARQ/app_api.html
		String sparqlQueryString = 
			"PREFIX book:   <http://vsw.org/book/> \n" +
			"PREFIX dc:      <http://purl.org/dc/elements/1.1/> \n" +
			"	SELECT ?book ?title \n" +
			"	WHERE \n" + 
			"	  { ?book dc:title ?title }" ;
    Query query = QueryFactory.create(sparqlQueryString) ;
    QueryExecution qexec = QueryExecutionFactory.create(query, mmanager.getDataset()) ;
    ResultSet results = qexec.execSelect() ;
    ResultSetFormatter.out(results) ;
    qexec.close() ;
    
    mmanager.close();
	}	
}