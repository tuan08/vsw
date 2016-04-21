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
package org.vsw.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.URL;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class IOUtil {
	static public InputStream loadRes(String res) throws IOException {
		InputStream is = null ;
		if(res.startsWith("file:")) {
			is = new FileInputStream(res.substring("file:".length())) ;
		} else if(res.startsWith("classpath:")) {
			res = res.substring("classpath:".length()) ;
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(res) ;
		} else {
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(res) ;
		}
		return is ;
	}
	
  static public String getFileContenntAsString(File file, String encoding) throws Exception {
    FileInputStream is = new FileInputStream(file) ;
    return new String(getStreamContentAsBytes(is), encoding) ;
  }

  static public String getFileContenntAsString(File file) throws Exception {
    FileInputStream is = new FileInputStream(file) ;
    return new String(getStreamContentAsBytes(is)) ;
  }

  static public String getFileContenntAsString(String fileName, String encoding) throws Exception {
    FileInputStream is = new FileInputStream(fileName) ;
    return new String(getStreamContentAsBytes(is), encoding) ;
  }

  static public String getFileContenntAsString(String fileName) throws Exception {
    FileInputStream is = new FileInputStream(fileName) ;
    String data = new String(getStreamContentAsBytes(is)) ;
    is.close() ;
    return data ;
  }

  static public byte[] getFileContentAsBytes(String fileName) throws Exception {
    FileInputStream is = new FileInputStream(fileName) ;
    byte[] data = getStreamContentAsBytes(is) ;
    is.close() ;
    return data ;
  }

  static public String getStreamContentAsString(InputStream is, String encoding) throws IOException {
    return new String(getStreamContentAsBytes(is), encoding) ;
  }
  
  static public String getStreamContentAsString(Reader in) throws IOException {
  	char[] data  = new char[4912];      
  	int available = -1;
  	StringBuilder b = new StringBuilder() ;
  	while((available = in.read(data)) > -1){
  		b.append(data, 0, available);
  	}
  	return b.toString() ;
  }

  static public byte[] getStreamContentAsBytes(InputStream is) throws IOException {
    BufferedInputStream buffer = new BufferedInputStream(is);    
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] data  = new byte[4912];      
    int available = -1;
    while((available = buffer.read(data)) > -1){
      output.write(data, 0, available);
    }   
    is.close() ;
    return output.toByteArray();
  }
  
  static public byte[] getStreamContentAsBytes(InputStream is, int maxRead) throws IOException {
    BufferedInputStream buffer = new BufferedInputStream(is);    
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    byte[] data  = new byte[4912];      
    int available = -1, read = 0 ;
    while((available = buffer.read(data)) > -1 && read < maxRead){
      if(maxRead - read < available) available = maxRead - read ;
      output.write(data, 0, available);
      read += available ;
    }   
    return output.toByteArray();
  }

  static public String getResourceAsString(String resource, String encoding) throws Exception {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    URL url = cl.getResource(resource);
    InputStream is = url.openStream();
    String data = getStreamContentAsString(is, encoding) ;
    is.close() ;
    return data ;
  }

  static public byte[] getResourceAsBytes(String resource) throws Exception {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    URL url = cl.getResource(resource);
    InputStream is = url.openStream();
    byte[] data = getStreamContentAsBytes(is) ;
    is.close() ;
    return data ;
  }

  static public  byte[] serialize(Object obj) throws Exception {
    ByteArrayOutputStream bytes = new  ByteArrayOutputStream() ;
    ObjectOutputStream out = new ObjectOutputStream(bytes);
    out.writeObject(obj);
    out.close();
    byte[] ret = bytes.toByteArray() ;
    return ret ;
  }

  static public Object deserialize(byte[] bytes) throws Exception {
    if (bytes == null) return null ;
    ByteArrayInputStream is = new  ByteArrayInputStream(bytes) ;
    ObjectInputStream in = new ObjectInputStream(is);
    Object obj =  in.readObject() ;
    in.close();
    return obj ;
  }
}