import java.io.File

import org.vsw.util.CommandParser ;
import org.vsw.util.FileUtil ;
import org.vsw.util.ConsoleUtil ;
import org.vsw.nlp.query.QueryContext;
import org.vsw.nlp.query.QueryData ;
import org.vsw.nlp.query.QueryResourceLoader;

import org.vsw.extract.Document ;
import org.vsw.extract.SectionProcessor ;
import org.vsw.extract.ExtractStatisticMap ;
import org.vsw.extract.rdf.PeopleRDFWriter ;
import org.vsw.extract.rdf.PlaceRDFWriter ;


object rdfmap {
  def main(args: Array[String]) {
    val command = 
      new CommandParser("command: process") ;
    command.addMandatoryOption("file", true, "The input wikipedia xml/json dump file") ;
    command.addOption("section", true, "The section type to process") ;
    command.addMandatoryOption("query", true, "The query file/directory") ;
    command.addOption("outdir", true, "The output directory") ;
    command.addOption("limit", true, "Limit number of records to process") ;
    if(!command.parse(args)) return ;
    command.printHelp() ;
    val queryConfigDir = command.getOption("query", null) ;
    val outDir = command.getOption("outdir", "target") ;
    val dataDir = outDir + "/data" ;
    FileUtil.mkdirs(dataDir) ;
    val limit = Integer.parseInt(command.getOption("limit", "10000000")) ;

    val  placeRDFWriter = new PlaceRDFWriter(dataDir + "/place.n3") ;
    val  peopleRDFWriter = new PeopleRDFWriter(dataDir + "/people.n3") ;

    val out = ConsoleUtil.getUTF8SuportOutput() ;
    val stat = new ExtractStatisticMap() ;
    val processor = new SectionProcessor(command.getOption("file", null), command.getOptionValues("section", null)) {
      override def onLoad(loader: QueryResourceLoader) {
        loader.load("classpath:nlp/vietnamese.lexicon.json", "lexicon") ;
        loader.load("classpath:nlp/data.place.json", "place") ;
        loader.load("classpath:nlp/mobile.product.json", "product") ;

        val queryFiles = FileUtil.findFiles(queryConfigDir, ".*\\.json") ;
        for(sel <- queryFiles) {
          loader.load(sel) ;
          println("Load Query: " + sel) ;
        }
      }

      override def onPost(doc : Document, data: QueryData, context: QueryContext) {
      }

      override def onPost(doc: Document, context: QueryContext) {
        placeRDFWriter.write(doc, context) ;
        peopleRDFWriter.write(doc, context) ;
        stat.incr(context) ;
        context.reset() ;
      }
    };
    processor.setLimit(limit) ;
    processor.process() ;

    placeRDFWriter.close() ;
    peopleRDFWriter.close() ;
    stat.report(out);
  }
}
