import java.io.File

import org.vsw.nlp.ml.crf.ws.WSReport ;

object wsreport {
  def main(args: Array[String]) {
    val report = new WSReport() ;
    report.run(args) ;
  }
}
