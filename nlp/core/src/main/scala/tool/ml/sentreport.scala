import java.io.File

import org.vsw.nlp.ml.crf.sent.SentReport ;

object sentreport {
  def main(args: Array[String]) {
    val report = new SentReport() ;
    report.run(args) ;
  }
}
