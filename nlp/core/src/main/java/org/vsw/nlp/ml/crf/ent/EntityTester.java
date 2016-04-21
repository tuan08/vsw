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
package org.vsw.nlp.ml.crf.ent;

import org.vsw.nlp.NLPResource;
import org.vsw.nlp.ml.crf.CRFTester;
import org.vsw.nlp.ml.crf.TokenFeaturesGenerator;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class EntityTester extends CRFTester {
	public EntityTester() throws Exception {
		this("nlp/entity.np.crf") ;
	}
	
	public EntityTester(String crfmodel) throws Exception {
		super(crfmodel) ;
	}

	protected TokenFeaturesGenerator createTokenFeaturesGenerator() throws Exception {
		String configName = command.getOption("config", "np") ;
		EntitySetConfig config = EntitySetConfig.getConfig(configName) ;
		return EntityTrainer.newTokenFeaturesGenerator(NLPResource.getInstance(), config) ;
	}

	public static void main(String[] args) throws Exception {
		args = new String[] { "-file",  "src/test/resources/tagged/sample.tagged" };
		EntityTester tester = new EntityTester();
		tester.test(args);
	}
}