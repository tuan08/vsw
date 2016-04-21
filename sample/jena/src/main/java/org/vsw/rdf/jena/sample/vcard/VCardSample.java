package org.vsw.rdf.jena.sample.vcard;
import org.vsw.rdf.jena.InMemRDFDataSet;
import org.vsw.rdf.jena.RDFDataSet;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.VCARD;

public class VCardSample {

	public static void main (String args[]) {
		RDFDataSet mmanager = new InMemRDFDataSet() ;
		//ModelManager mmanager = new TDBModelManager("target/ExTDB1") ;
		Model model = mmanager.getModel() ;
		String personURI    = "http://somewhere/JohnSmith";
    String givenName    = "John";
    String familyName   = "Smith";
    String fullName     = givenName + " " + familyName;

    Resource johnSmith  = model.createResource(personURI) ;
    johnSmith.addProperty(VCARD.FN, fullName) ;
    johnSmith.addProperty(VCARD.N, 
              model.createResource()
                  .addProperty(VCARD.Given, givenName)
                  .addProperty(VCARD.Family, familyName));
    //DumpUtil.dumpStatements(System.out, model) ;
		model.write(System.out, "N3") ;
    mmanager.close();
	}
}
