import java.io.File

import org.vsw.nlp.ml.crf.ent.EntityReport ;

object entreport {
  def main(args: Array[String]) {
    val report = new EntityReport() ;
    report.run(args) ;
  }
}
