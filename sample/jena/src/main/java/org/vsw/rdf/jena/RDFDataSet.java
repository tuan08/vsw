package org.vsw.rdf.jena;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.rdf.model.Model;

public interface RDFDataSet {
	public Dataset getDataset() ;
	public Model getModel() ;
	public void close() ;
}
