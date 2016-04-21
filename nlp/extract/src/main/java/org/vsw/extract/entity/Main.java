package org.vsw.extract.entity;

import java.io.PrintStream;

import org.vsw.extract.Document;
import org.vsw.extract.ExtractStatisticMap;
import org.vsw.extract.ParagraphProcessor;
import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData;
import org.vsw.nlp.query.QueryResourceLoader;
import org.vsw.util.CommandParser;
import org.vsw.util.ConsoleUtil;
import org.vsw.util.FileUtil;

public class Main {
  static public void run(String[] args) throws Exception {
  	CommandParser command =
  		new CommandParser("command:") ;
  	command.addMandatoryOption("file", true, "The input wikipedia xml/json dump file") ;
  	command.addOption("section", true, "The section type to process") ;
  	command.addMandatoryOption("query", true, "The query file/directory") ;
  	command.addOption("outdir", true, "The output directory") ;
  	command.addOption("limit", true, "Limit number of records to process") ;
  	if(!command.parse(args)) return ;
  	command.printHelp() ;
  	
  	String outdir = command.getOption("outdir", "target/entities") ;
  	FileUtil.mkdirs(outdir) ;
  	int limit = command.getOption("limit", 100000000) ;
  	
  	final String queryCFPath = command.getOption("query", null) ;
  	final ExtractStatisticMap stat = new ExtractStatisticMap() ;
  	final EntitySuggestionCollectors collectors = new EntitySuggestionCollectors(outdir) ;
  	PrintStream out = ConsoleUtil.getUTF8SuportOutput() ;
  	ParagraphProcessor processor = 
  		new ParagraphProcessor(command.getOption("file", null), command.getOptionValues("section", null)) {
       protected void onLoad(QueryResourceLoader loader) throws Exception {
         //loader.load("classpath:nlp/vietnamese.lexicon.json", "lexicon") ;
         //loader.load("classpath:nlp/data.place.json", "place") ;
         //loader.load("classpath:nlp/mobile.product.json", "product") ;

         String[] queryFiles = FileUtil.findFiles(queryCFPath, ".*\\.json") ;
         for(String sel : queryFiles) {
           loader.load(sel) ;
           System.out.println("Load Query: " + sel) ;
         }
       }

       protected void onPost(Document doc, QueryData data, QueryContext context) throws Exception {
      	 collectors.collect(context) ;
      	 stat.incr(context) ;
         context.reset() ;
       }

       protected void onPost(Document doc, QueryContext context) {
       }
     };
     processor.setLimit(limit) ;
     processor.process() ;
     collectors.process(new NewEntitySuggestionProcessor()) ;
     stat.report(out) ;
  }
  
  static public void main(String[] args) throws Exception {
  	if(args == null || args.length == 0) {
  		args = new String[] {
  	    "-file",  "d:/viwiki.xml",
  	    "-query", "src/data/wikipedia/query/entity",
  	    "-limit", "10000"
  		};
  	}
  	run(args) ;
  }
}