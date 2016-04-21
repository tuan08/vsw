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
package org.vsw.nlp.ml.io;

import java.io.IOException;
import java.io.PrintStream;

import org.vsw.nlp.doc.io.DocumentWriter;
import org.vsw.nlp.ml.BoundaryTag;
import org.vsw.nlp.token.IToken;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class WTagDocumentWriter extends DocumentWriter {
	protected void write(PrintStream out, IToken token) throws IOException {
		out.append(token.getOriginalForm());
		out.append(":{") ;
		BoundaryTag btag = token.getFirstTagType(BoundaryTag.class) ;
		if(btag != null) {
			String[] feature = btag.getFeatures() ;
			for(int i = 0; i < feature.length; i++) {
				if(i > 0) out.append(", ") ;
				out.append(feature[i]) ;
			}
		}
		out.append("}") ;
	}
}