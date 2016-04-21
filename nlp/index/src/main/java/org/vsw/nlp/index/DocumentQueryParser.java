package org.vsw.nlp.index;

import java.util.Calendar;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.vsw.util.DateUtil;

/**
 * Author : Tuan Nguyen
 *          tuan.nguyen@headvances.com
 * Jun 3, 2010  
 */
public class DocumentQueryParser extends QueryParser {
  public DocumentQueryParser(Analyzer a) {
    super(Version.LUCENE_CURRENT, "text", a);
  }
  
  public DocumentQueryParser(Analyzer a, String defaultField) {
    super(Version.LUCENE_CURRENT, defaultField, a);
  }
  
  protected Query getRangeQuery(String field, String part1, String part2, boolean inclusive) throws ParseException {
    if(field.endsWith(":date")) {
      try {
        long from = 0, to = 0 ;
        if(part1.indexOf('@') > 0) {
          from = DateUtil.parseCompactDateTime(part1).getTime();
        } else {
          from = DateUtil.parseCompactDate(part1).getTime() ;
          Calendar cal = Calendar.getInstance() ;
          cal.setTimeInMillis(from) ;
          cal.set(Calendar.HOUR_OF_DAY, 0) ;
          cal.set(Calendar.MINUTE, 0) ;
          cal.set(Calendar.SECOND, 0) ;
          from = cal.getTimeInMillis() ;
        }
        
        if(part2.indexOf('@') > 0) {
          to = DateUtil.parseCompactDateTime(part2).getTime() ;
        } else {
          to = DateUtil.parseCompactDate(part2).getTime() ;
          Calendar cal = Calendar.getInstance() ;
          cal.setTimeInMillis(to) ;
          cal.set(Calendar.HOUR_OF_DAY, 23) ;
          cal.set(Calendar.MINUTE, 59) ;
          cal.set(Calendar.SECOND, 59) ;
          to = cal.getTimeInMillis() ;
        }
        return NumericRangeQuery.newLongRange(field, from, to, inclusive, inclusive) ;
      } catch(Exception ex) {
        throw new ParseException("Cannot parse field = " + field + ", part1 = " + part1 + ", part2 = " + part2) ;
      }
    } else if(field.endsWith(":int")) {
    	return NumericRangeQuery.newIntRange(field, Integer.parseInt(part1), Integer.parseInt(part2), inclusive, inclusive) ;
    } else if(field.endsWith(":long")) {
    	return NumericRangeQuery.newLongRange(field, Long.parseLong(part1), Long.parseLong(part2), inclusive, inclusive) ;
    } else if(field.endsWith(":float")) {
    	return NumericRangeQuery.newFloatRange(field, Float.parseFloat(part1), Float.parseFloat(part2), inclusive, inclusive) ;
    } else if(field.endsWith(":dbl")) {
    	return NumericRangeQuery.newDoubleRange(field, Double.parseDouble(part1), Double.parseDouble(part2), inclusive, inclusive) ;
    }
    return super.getRangeQuery(field, part1, part2, inclusive) ;
  }

  final static public String normalize(String token) {
    StringBuilder b = new StringBuilder() ;
    char[] buf = token.toCharArray() ;
    for(int j = 0; j < buf.length; j++) {
      if(buf[j] == ' ' || buf[j] == ':' || buf[j] == '=' || buf[j] == '-') b.append('.') ;
      else b.append(buf[j]) ;
    }
    return b.toString() ;
  }
  
  final static public String[] normalize(String[] token) {
    if(token.length == 0) return token ;
    StringBuilder b = new StringBuilder() ;
    for(int i = 0; i < token.length; i++) {
      b.setLength(0) ;
      char[] buf = token[i].toCharArray() ;
      for(int j = 0; j < buf.length; j++) {
        if(buf[j] == ' ' || buf[j] == ':' || buf[j] == '=') b.append('.') ;
        else b.append(buf[j]) ;
      }
      token[i] = b.toString();
    }
    return token ;
  }
}