package org.vsw.rdf.jena;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.tdb.TDBFactory;

public class TDBRDFDataSet implements RDFDataSet {
	private Dataset dataset ;
	private Model model ;
	
	public TDBRDFDataSet(String directory) {
		this.dataset = TDBFactory.createDataset(directory) ;
		this.model = dataset.getDefaultModel() ;
	}
	
	public Dataset getDataset() { return this.dataset ; }
	
  public Model getModel() { return this.model ; }

  public void close() {
  	dataset.close() ;
  }
}
