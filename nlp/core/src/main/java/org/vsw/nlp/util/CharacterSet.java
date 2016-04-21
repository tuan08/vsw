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
package org.vsw.nlp.util;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
final public class CharacterSet {
	final static public char[] BLANK = {' ', '\t', 160} ;
	final static public char[] NEW_LINE = {'\n', '\f', '\r', '\0', 13 } ;
	final static public char[] BRACKET = {
		'(', ')', '[',']', '{', '}', '<', '>'
	};
	final static public char[] PUNCTUATION = {
  	//apostrophe
    '\'', '’', 
    //Brackets
    '(', ')', '[', ']', '{', '}', '<', '>',
    //colon
    ':',
    //comma
    ',',
    //dashes
    '‒', '–', '—', '―',
    //exclamation mark  
    '!',
    //full stop (period)  
    '.',
    //guillemets  
    '«', '»',
    //hyphen  
    '-', '‐',
    //question mark   
    '?',
    //quotation marks   
    '"', '\'', '‘', '’', '“', '”',
    //semicolon   
    ';',
    //slash/stroke  
    '/', '\\',
    //solidus   
    '⁄',
    //percent
    '%'
	};
	
	final static public char[] QUOTTATION = { '"', '\'', '‘', '’', '“', '”', '«', '»' } ;
	final static public char[] END_SENTENCE = { '.', '!', '?', ';', '…' } ;
	
	static public boolean isDigit(char c) {
  	if(c >= '0' && c <= '9') return true ;
  	return false ;
  }
  
  static public boolean isBlank(char c) {
    if(c == ' ' || c == '\t' || c == 160) return true ;
    return false  ;
  }
  
  static public boolean isNewLine(char c) {
    if(c == '\n' || c == '\f' || c == '\r' || c == '\0' || c == 13) return true ;
    return false  ;
  }
  
  public final static boolean isPunctuation(char c) {
    //apostrophe
    if(c == '\'' || c == '’') return true ;
    //Brackets
    if(c == '(' || c == ')' || c == '[' || c == ']' || 
       c == '{' || c == '}' || c == '<' || c == '>') return true ;
    //colon
    if(c == ':') return true ;
    //comma
    if(c == ',') return true ;
    //dashes
    if( c == '‒' || c == '–' || c =='—' || c == '―') return true ;
    //exclamation mark  
    if(c == '!' ) return true ;
    //full stop (period)  
    if(c == '.') return true;
    //guillemets  
    if(c == '«' || c == '»' ) return true ;
    //hyphen  
    if( c == '-' || c == '‐' ) return true ;
    //question mark   
    if(c == '?') return true ;
    //quotation marks   
    if(c == '"' || c == '\'' || c == '‘' || c ==  '’' || c == '“' || c == '”') return true ;
    //semicolon   
    if(c == ';') return true ;
    //slash/stroke  
    if(c ==  '/' || c == '\\') return true ;
    //solidus   
    if(c == '⁄') return true ;
    //percent
    if(c == '%') return true ;
    return false ;
  }
  
  final static public boolean isEndSentence(char c) {
    if(c == '.' || c == '!' || c == '?' || c == ';') return true ;
    return false ;
  }
  
  final static public boolean isBracket(char c) {
    if(c == '(' || c == ')' || c == '[' || c == ']' || 
       c == '{' || c == '}' || c == '<' || c == '>') return true ;
    return false ;
  }
  
  final static public boolean isQuotationMark(char c) {
    if(c == '"' || c == '\''|| c == '‘' || c ==  '’' || c == '“' || c == '”') return true ;
    return false ;
  }
  
  final static public boolean isGuillemets(char c) {
    if(c == '«' || c == '»' ) return true ;
    return false ;
  }
  
  final static public boolean isIn(char c, char[] set) {
  	for(char sel : set) if(c == sel) return true ;
  	return false ;
  }
  
  final static public char[] merge(char[] ... set) {
  	int size = 0 ;
  	for(char[] sel : set) size += sel.length ;
  	char[] holder = new char[size] ;
  	int idx = 0 ;
  	for(char[] sel : set) {
  		for(char selchar : sel) {
  			holder[idx++] = selchar ;
  		}
  	}
  	return holder ;
  }
  
  final static public boolean isWLetter(String string) {
  	if(string == null) return false ;
  	for(int i = 0; i < string.length(); i++) {
  		if(!Character.isLetter(string.charAt(i))) return false; 
  	}
  	return true ;
  }
  
  final static public boolean isDigits(String string) {
  	if(string == null) return false ;
  	for(int i = 0; i < string.length(); i++) {
  		char c  = string.charAt(i) ;
  		if(c < '0' || c > '9') return false; 
  	}
  	return true ;
  }
}