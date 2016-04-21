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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
/**
 * $Author: Tuan Nguyen$ 
 * $Revision$
 * $Date$
 * $LastChangedBy$
 * $LastChangedDate$
 * $URL$
 **/
public class CommandParser {
	private Options options ;
	private CommandLine cmd ;
	private String helpMesg ;
	
	public CommandParser(String helpMesg) {
		options = new Options() ;
		addOption("help", false, "Print this help message") ;
		addOption("apphome", true, "The app home directory") ;
		this.helpMesg = helpMesg ;
	}
	
	public void addOption(String name, boolean hasArg, String desc) {
		Option op = new Option(name, hasArg, desc) ;
		options.addOption(op);
	}

	public void addMandatoryOption(String name, boolean hasArg, String desc) {
		Option op = new Option(name, hasArg, desc) ;
		op.setRequired(true) ;
		options.addOption(op);
	}
	
	public boolean hasOption(String name) { return cmd.hasOption(name) ; }
	
	public String getOption(String name, String dvalue) {
		return cmd.getOptionValue(name, dvalue) ;
	}
	
	public String[] getOptionValues(String name, String[] dvalue) {
		String values = cmd.getOptionValue(name, null) ;
		if(values == null) return dvalue ;
		return StringUtil.toStringArray(values) ;
	}
	
	public int getOption(String name, int dvalue) {
		String value = cmd.getOptionValue(name, null) ;
		if(value == null) return dvalue ;
		return Integer.parseInt(value) ;
	}
	
	public boolean parse(String[] args)  {
		try {
		CommandLineParser parser = new PosixParser();
    this.cmd = parser.parse(options, args);
    if(cmd.hasOption("help")) {
    	printHelp() ;
    	return false ;
    }
		} catch(Throwable ex) {
			System.out.println("ERROR: " + ex.getMessage()) ;
			printHelp() ;
			return false ;
		}
    return true ;
	}

	public void printHelp()  {
    HelpFormatter formatter = new HelpFormatter();
  	formatter.printHelp(helpMesg, options );
	}
}