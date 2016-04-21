package org.vsw.extract.mediawiki;

import org.junit.Test;

public class InfoBoxUnitTest {
	@Test
	public void test() throws Exception {
		String WIKI = 
			"test {{this is a test}} \n" +
			"{{open wikiformat \n" +
			"| row1 =  row1 {{nested wikiformat}} \n" +
			"| row2 =  row2 \n" +
			"| row3 =  row3 \n" +
			"}} \n" +
			"" ;
		InfoBox infoBox = InfoBox.parse(WIKI) ;
		if(infoBox != null) {
			System.out.println(infoBox) ;
		}
	}
}