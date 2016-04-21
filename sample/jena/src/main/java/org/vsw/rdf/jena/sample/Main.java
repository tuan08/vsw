package org.vsw.rdf.jena.sample;
import java.io.FileInputStream;

import org.vsw.rdf.jena.InMemRDFDataSet;
import org.vsw.rdf.jena.RDFDataSet;
import org.vsw.rdf.jena.TDBRDFDataSet;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.Model;

public class Main {
	public static void main (String args[]) throws Exception {
		RDFDataSet  mmanager = new InMemRDFDataSet () ;
		//RDFDataSet mmanager = new TDBRDFDataSet("target/TDB") ;
		Model model = mmanager.getModel() ;
		model.read(new FileInputStream("src/main/resources/sample/books.n3"),  null, "N3") ;
		model.read(new FileInputStream("src/main/resources/sample/people.n3"), null, "N3") ;
		model.write(System.out, "N3") ;

		//See http://www.openjena.org/ARQ/app_api.html
		String sparqlQueryString = 
			"PREFIX  book: <http://vsw.org/book/> \n" +
			"PREFIX  dc:   <http://purl.org/dc/elements/1.1/> \n" +
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
