package org.vsw.extract.mediawiki;

import org.wikimodel.wem.IWikiPrinter;

public class TextHolder implements IWikiPrinter {
	final private StringBuilder data = new StringBuilder();

	public String getText() { return data.toString() ; }
	
	public void print(String str) { data.append(str) ; }
	public void println(String str) { data.append(str).append("\n") ; }
}
