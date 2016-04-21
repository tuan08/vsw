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

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class StatisticMap  {
	static DecimalFormat PFORMATER = new DecimalFormat("####00.00%"); 
	
	private Map<String, TreeMap<String, Number>> map = 
		new LinkedHashMap<String, TreeMap<String, Number>>() ;
	private long startTime = System.currentTimeMillis() ;
	
	public void increment(String category, String name, long amount) {
		incr(category, name, amount) ;
	}

	public void incr(String category, String name, long amount) {
		incr(category, name, null, amount) ;
	}
	
	public void incr(String category, String name, String relateTo, long amount) {
		if(name == null || name.length() == 0) name = "other" ;
		TreeMap<String, Number> cat = map.get(category) ;
		if(cat == null) {
			cat = new TreeMap<String, Number>() ;
			map.put(category, cat) ;
		}
		Number value = cat.get(name) ;
		if(value == null) cat.put(name, new Number(relateTo, amount)) ;
		else value.incr(amount) ;
	}
	
	public void incr(String category, String[] name, long amount) {
		if(name == null || name.length == 0) {
			incr(category, (String) null, amount) ;
			return ;
		}
		for(int i = 0; i < name.length; i++) {
			incr(category, name[i], null, amount) ;
		}
	}
	
	public void incr(String category, Collection<String> name, long amount) {
		if(name == null || name.size() == 0) {
			incr(category, (String) null, amount) ;
			return ;
		}
	
		for(String sel : name) {
			incr(category, sel, null, amount) ;
		}
	}
	
	public void incr(String category, String[] name, String relateTo,  long amount) {
		if(name == null || name.length == 0) {
			incr(category, (String) null, relateTo, amount) ;
			return ;
		}
		for(int i = 0; i < name.length; i++) {
			incr(category, name[i], relateTo, amount) ;
		}
	}
	
	public void report(String file, String order) throws Exception { 
		PrintStream os = new PrintStream(new FileOutputStream(file), true, "UTF-8") ;
		report(os, order) ; 
		os.close() ;
	}
	
	public void report(PrintStream os) { report(os, null) ; }
	
	public void report(PrintStream os, String order) {
		Iterator<Map.Entry<String, TreeMap<String, Number>>> i = map.entrySet().iterator() ;
		while(i.hasNext()) {
			Map.Entry<String, TreeMap<String, Number>> entry = i.next() ;
			os.println("*************************************************************************");
			os.println(entry.getKey()) ;
			os.println("*************************************************************************");
			for(Map.Entry<String, Number> subentry : sort(entry.getValue(), order)) {
				Number relateTo = null ;
				Number value = subentry.getValue() ;
				if(value.relateTo != null) {
					relateTo = entry.getValue().get(value.relateTo) ;
				}
				print(os, subentry.getKey(),  45, subentry.getValue(), relateTo) ;
			}
			os.println();
		}
		os.println("Execute in " + (System.currentTimeMillis() - startTime) + "ms");
	}

	public Map<String, TreeMap<String, Number>> getStatistics() { return map ; }

	private void print(PrintStream os , String name, int nameWidth, Number value, Number relateValue) {
		os.print(name) ;
		for(int i = name.length(); i < nameWidth; i++) {
			os.print(' ') ;
		}
		String valueString = Long.toString(value.getValue()) ;
		for(int i = valueString.length(); i < 15; i++) {
			os.print(' ') ;
		}
		os.print(valueString) ;
		os.print("\t") ;
		if(relateValue != null) {
			os.print(PFORMATER.format(value.number/(double)relateValue.getValue())) ;
		}
		os.print('\n') ;
	}

	private List<Map.Entry<String, Number>> sort(TreeMap<String, Number> map, String order) {
		List<Map.Entry<String, Number>> holder = new ArrayList<Map.Entry<String, Number>>() ;
		holder.addAll(map.entrySet()) ;
		if("desc".equals(order)) {
			Collections.sort(holder, new EntryComparator(-1)) ;
		} else if("asc".equals(order)) {
			Collections.sort(holder, new EntryComparator(1)) ;
		}
		return holder ;
	}
	
	static class EntryComparator implements Comparator<Map.Entry<String, Number>> {
		private int direction ;
		
		EntryComparator(int direction) { this.direction = direction ; }
		
		public int compare(Entry<String, Number> e0, Entry<String, Number> e1) {
			long val = e0.getValue().number - e1.getValue().number;
			int ret = 0 ;
			if(val > 1) ret = 1 ;
			else if(val < 0) ret = -1 ;
			return ret * direction ;
		}
	};
	
	static public class Number {
		private long number ;
		private String relateTo ;

		public Number(String relateTo, long number)  { 
			this.relateTo = relateTo ;
			this.number = number ; 
		}

		public long getValue() { return this.number ; }

		public void incr(long value) { this.number += value ; }
	}

	static public void main(String[] args) {
		StatisticMap map = new StatisticMap() ;
		map.increment("category1", "name1", 1) ;
		map.increment("category1", "name2", 1) ;

		map.incr("category2", "all", 3) ;
		map.incr("category2", "name1", "all", 1) ;
		map.incr("category2", "name2", "all", 1) ;
		map.incr("category2", "name2", "all", 1) ;

		map.report(System.out) ;
	}
}
