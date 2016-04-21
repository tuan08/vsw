package org.vsw.extract.entity;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.Writable;
import org.vsw.hadoop.util.WritableUtil;

import uk.ac.shef.wit.simmetrics.similaritymetrics.CosineSimilarity;

public class PropertySuggestion implements Writable {
	private String      name ;
	private String      suggestion ;
	private Candidate[] candidate  ;
	private Sample[]    sample     ;
	
	public String getName() { return name; }
	public void   setName(String name) { this.name = name; }
	
	public String getSuggestion() { return suggestion; }
	public void   setSuggestion(String suggestion) { this.suggestion = suggestion; }
	
	public void addCandidate(String string) {
		Candidate newCandidate = new Candidate(string, 1) ;
		if(this.candidate == null) {
			this.candidate = new Candidate[] { newCandidate } ;
		} else {
			Candidate[] array = new Candidate[this.candidate.length + 1] ;
			System.arraycopy(candidate, 0, array, 0, candidate.length) ;
			array[candidate.length] = newCandidate ;
			candidate = array ;
		}
	}
	public Candidate[] getCandidate() { return candidate; }
	public void setCandidate(Candidate[] candidate) { this.candidate = candidate; }
	
	public void addSample(String string) {
		Sample newSample = new Sample(string, 1) ;
		if(this.sample == null) {
			this.sample = new Sample[] { newSample } ;
		} else {
			Sample[] array = new Sample[this.sample.length + 1] ;
			System.arraycopy(sample, 0, array, 0, sample.length) ;
			array[sample.length] = newSample ;
		  sample = array ;
		}
	}
	public Sample[] getSample() { return sample; }
	public void     setSample(Sample[] sample) { this.sample = sample ; }
	
	public void merge(PropertySuggestion other) throws Exception {
		if(!name.equals(other.name)) {
			throw new Exception("Cannot merge suggestion " + name + " with " + other.name) ;
		}
	  //merge sample
		List<Sample> sampleHolder = new ArrayList<Sample>() ;
		for(int i = 0; i < sample.length; i++) {
			sampleHolder.add(sample[i]) ;
		}
		for(int i = 0; i < other.sample.length; i++) {
			Sample asample = other.sample[i] ;
			String asampleString = asample.getSample().toLowerCase() ;
			boolean foundMatch = false ;
			for(int j = 0; j < this.sample.length; j++) {
				float compare = compare(asampleString, this.sample[j].getSample().toLowerCase()) ;
				if(compare > 0.65) {
					foundMatch = true ;
					this.sample[j].setOccurrence(this.sample[j].getOccurrence() + asample.getOccurrence()) ;
				}
			}
			if(foundMatch) {
				//probably same sample, ignore this suggestion
				return ;
			} else {
				sampleHolder.add(asample) ;
			}
		}
		this.sample = sampleHolder.toArray(new Sample[sampleHolder.size()]) ;
		//merge candidate 
		Map<String, Candidate> candidates = new HashMap<String, Candidate>() ;
		for(int i = 0; i < candidate.length; i++) {
			candidates.put(candidate[i].getCandidate(), candidate[i]) ;
		}
		for(Candidate sel : other.getCandidate()) {
			Candidate exist = candidates.get(sel.getCandidate()) ;
			if(exist != null) {
				exist.setOccurrence(exist.getOccurrence() + sel.getOccurrence()) ;
			} else {
				candidates.put(sel.getCandidate(), sel) ;
			}
		}
	}
	
	public void readFields(DataInput in) throws IOException {
		this.name = WritableUtil.readString(in) ;
		this.suggestion = WritableUtil.readString(in) ;
		this.candidate = WritableUtil.readArray(in, Candidate.FACTORY) ;
		this.sample = WritableUtil.readArray(in, Sample.FACTORY) ;
	}

  public void write(DataOutput out) throws IOException {
  	WritableUtil.writeString(out, name) ;
  	WritableUtil.writeString(out, suggestion) ;
  	WritableUtil.writeArray(out, candidate) ;
  	WritableUtil.writeArray(out, sample) ;
  }
	
	static private CosineSimilarity comparator = new CosineSimilarity();
  public static float compare(String s1, String s2) {
    if(s1 == null) s1 = "" ;
    if(s2 == null) s2 = "" ;
    float t = comparator.getSimilarity(s1, s2);
    return t;
  }
}