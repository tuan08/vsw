/**
 * Copyright (C) 2011 Headvances Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This project aim to build a set of library/data to process 
 * the Vietnamese language and analyze the web data
 **/
package org.vsw.nlp.ml.crf.qtag;

import org.vsw.nlp.ml.crf.CRFTester;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
import org.vsw.nlp.ml.crf.ent.EntitySetConfig;

public class QTAGTester extends CRFTester {
	public QTAGTester() throws Exception {
		this("models/qtag.crf") ;
	}
	
	public QTAGTester(String crfmodel) throws Exception {
		super(crfmodel) ;
	}

	protected TokenFeaturesGenerator createTokenFeaturesGenerator() throws Exception {
		String configName = command.getOption("config", "qtag") ;
		EntitySetConfig config = EntitySetConfig.getConfig(configName) ;
		return QTAGTrainer.newTokenFeaturesGenerator(null, config) ;
	}

	public static void main(String[] args) throws Exception {
	  args = new String[] { "-file", "src/test/java/resources/QTAGTagged/QTAGsample.txt" };
    QTAGTester tester = new QTAGTester();
    tester.test(args);
	}
}