package org.vsw.rdf.jena;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.rdf.model.Model;

public class InMemRDFDataSet implements RDFDataSet {
	private Dataset dataset ;
	private Model model ;
	
	public InMemRDFDataSet() {
		this.dataset = DatasetFactory.create() ;
		this.model = dataset.getDefaultModel() ;
	}
	
	public Dataset getDataset() { return this.dataset ; }
	
  public Model getModel() { return this.model ; }

  public void close() {
  	this.dataset.close() ;
  }

}
