package org.vsw.nlp.classification.svm;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.vsw.nlp.classification.ClassifiedDocument;
import org.vsw.nlp.classification.ClassifiedDocumentReader;
import org.vsw.nlp.classification.VuClassifiedDocumentReader;
import org.vsw.nlp.ml.crf.ent.EntityTagger;
import org.vsw.nlp.ml.crf.ws.WordSegmenter;
import org.vsw.nlp.token.TokenException;

public class FeatureGenerator {
	private FeatureDictionary fdict ;
	private EntityTagger entityTagger ;
	
	
	public FeatureGenerator(EntityTagger entityTagger) {
		this.fdict = new FeatureDictionary() ;
		this.entityTagger = entityTagger ;
	}
	
	public FeatureVector generate(ClassifiedDocument doc) throws TokenException {
		FeatureVector fvector = new FeatureVector(doc.getCategory(), fdict) ;
		WordSegmenter wsegmenter = entityTagger.getWordSegmenter() ;
		fvector.addFeature("title", wsegmenter.segment(doc.getTitle())) ;
		List<ClassifiedDocument.Section> sections = doc.getSections() ;
		for(int i = 0; i < sections.size(); i++) {
			ClassifiedDocument.Section section = sections.get(i) ;
			fvector.addFeature(section.getName(), wsegmenter.segment(section.getContent())) ;
		}
		return fvector ;
	}
	
	public void generate(String fileName, ClassifiedDocumentReader reader) throws Exception {
		FileOutputStream fos = new FileOutputStream(fileName) ;
		PrintStream out = new PrintStream(fos, true, "UTF-8") ;
		generate(out, reader) ;
		out.close();
	}
	
	public void generate(PrintStream out, ClassifiedDocumentReader reader) throws Exception {
		ClassifiedDocument doc = null ;
		while((doc = reader.read()) != null) {
			FeatureVector fvector = generate(doc) ;
			fvector.print(out) ;
			System.out.println(doc.getCategory() + ": " + doc.getPath());
		}
	}
	
	static public void main(String[] args) throws Exception {
		VuClassifiedDocumentReader reader = new VuClassifiedDocumentReader("d:/vusvm/svmdata/Data") ;
		EntityTagger entityTagger = new EntityTagger(new WordSegmenter()) ;
		FeatureGenerator generator = new FeatureGenerator(entityTagger) ;
		generator.generate(System.out, reader) ;
	}
}
