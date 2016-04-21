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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class DateUtil {
  final static public SimpleDateFormat COMPACT_DATE = new SimpleDateFormat("dd/MM/yyyy")  ;
  final static public SimpleDateFormat COMPACT_DATE_TIME = new SimpleDateFormat("dd/MM/yyyy@HH:mm:ss")  ;
  final static public SimpleDateFormat COMPACT_DATE_ID = new SimpleDateFormat("yyyyMMdd")  ;
  final static public SimpleDateFormat COMPACT_DATE_TIME_ID = new SimpleDateFormat("yyyyMMddHHmmss") ;
  
  static public String asCompactDate(long time) {
    return COMPACT_DATE.format(new Date(time)) ;
  }
  
  static public String asCompactDate(Date time) {
    return COMPACT_DATE.format(time) ;
  }
  
  static public String asCompactDateId(Date time) {
    return COMPACT_DATE_ID.format(time) ;
  }
  
  static public String asCompactDate(Calendar cal) {
    return COMPACT_DATE.format(cal.getTime()) ;
  }
  
  static public String asCompactDateTime(long time) {
    return COMPACT_DATE_TIME.format(new Date(time)) ;
  }
  
  static public String asCompactDateTIME(Date time) {
  	return COMPACT_DATE_TIME.format(time) ;
  }
  
  static public Date parseCompactDate(String exp) throws ParseException {
    return COMPACT_DATE.parse(exp) ;
  }
  
  static public Date parseCompactDateTime(String exp) throws ParseException {
    return COMPACT_DATE_TIME.parse(exp) ;
  }
  
  static public Date parseCompactDateTimeId(String exp) throws ParseException {
    return COMPACT_DATE_TIME_ID.parse(exp) ;
  }
  
  static public String asCompactDateTimeId(long time) {
    return COMPACT_DATE_TIME_ID.format(new Date(time)) ;
  }
  
  static public String asCompactDateTimeId(Date date) {
    return COMPACT_DATE_TIME_ID.format(date) ;
  }
  
  static public String currentTimePath(String basePath) {
    String backupTime = basePath + "/" + COMPACT_DATE_TIME_ID.format(new Date());
    return backupTime ;
  }
  
  static public String currentDatePath(String basePath) {
    String backupTime = basePath + "/" + COMPACT_DATE_ID.format(new Date());
    return backupTime ;
  }
}
