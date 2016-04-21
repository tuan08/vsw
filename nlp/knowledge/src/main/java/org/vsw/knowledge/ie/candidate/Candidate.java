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
package org.vsw.knowledge.ie.candidate;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class Candidate implements Writable {
	private String source ;
	private String entity ;
	private String text   ;
	
	public Candidate() {} ;
	
	public Candidate(String source, String entity, String text) {
		this.source = source ;
		this.entity = entity ;
		this.text = text ;
	}
	                                                         
	public String getSource() { return source; }
	public void   setSource(String source) { this.source = source; }

	public String getEntity() { return entity; }
	public void   setEntity(String entity) { this.entity = entity; }

	public String getText() { return text; }
	public void   setText(String text) { this.text = text; }

  public void readFields(DataInput in) throws IOException {
  	this.source = Text.readString(in) ;
  	this.entity = Text.readString(in) ;
  	this.text = Text.readString(in) ;
  }

  public void write(DataOutput out) throws IOException {
  	Text.writeString(out, source) ;
  	Text.writeString(out, entity) ;
  	Text.writeString(out, text) ;
  }
}
