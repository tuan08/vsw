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

import java.util.ArrayList;
import java.util.List;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class StringMatcher {
  final static byte EXACT_MATCH = 0 ;
  final static byte EXPRESSION_MATCH = 1 ;
  
  private byte matchType  = -1;
  private String expression ;
  private Matcher[] matcher ;
  
  public StringMatcher(String expression) {
    this.expression = expression ;
    if(expression.indexOf("*") >= 0) {
      matchType = EXPRESSION_MATCH ;
      List<Matcher> list = new ArrayList<Matcher>() ;
      char[] buf = expression.toCharArray() ;
      int idx = 0 ;
      int startToken = 0;
      while(idx < buf.length) {
        if(buf[idx] == '*') {
          if(idx - startToken > 0) {
            list.add(new TokenMatcher(new String(buf, startToken, idx - startToken))) ;
          }
          list.add(new AnyMatcher()) ;
          startToken = idx + 1;
        }
        idx++ ;
      }
      if(idx - startToken > 0) {
        list.add(new TokenMatcher(new String(buf, startToken, idx - startToken))) ;
      }
      matcher = new Matcher[list.size()] ;
      matcher = list.toArray(matcher) ;
    } else {
      matchType = EXACT_MATCH ;
    }
  }
  
  public boolean matches(String string) {
    if(matchType == EXACT_MATCH) return exactMatch(string) ;
    if(matchType == EXPRESSION_MATCH) return expressionMatch(string) ;
    throw new RuntimeException("Unknown match type for " + this.expression) ;
  }
  
  public String toString() { return this.expression ; }
  
  private boolean exactMatch(String string) {
    return expression.equals(string) ;
  }
  
  private boolean expressionMatch(String string) {
    int from = 0 ;
    for(int i = 0; i < matcher.length; i++) {
      from = matcher[i].matches(string, from, i) ;
      if(from < 0) return false ;
    }
    int stringLength = string.length() ;
    if(matcher[matcher.length - 1] instanceof AnyMatcher) {
      from = stringLength ;
    }
    return from  == stringLength ;
  }
  
  static interface Matcher {
    public int matches(String string, int from, int matcherIndex) ;
  }
  
  static class AnyMatcher implements Matcher {
    final public int matches(String string, int from, int matcherIndex) {
      return from ;
    }
  }
  
  static class TokenMatcher implements Matcher {
    private String token ;
    public TokenMatcher(String token) { this.token = token ; }
    
    final public int matches(String string, int from, int matcherIndex) {
      int idx = string.indexOf(token, from) ;
      if(idx < 0) return -1 ;
      if(matcherIndex == 0 && idx > 0) return -1 ;
      return idx + token.length() ;
    }
  }
}