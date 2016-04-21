import java.io.File

import org.vsw.nlp.ml.crf.ws.WSTester ;

object wstest {
  def main(args: Array[String]) {
    val tester = new WSTester() ;
    tester.test(args) ;
  }
}
