package org.vsw.nlp.classification.svm.lib;
public class svm_problem implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 453256488992614907L;
	public int l;
	public double[] y;
	public svm_node[][] x;
}
