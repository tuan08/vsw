import java.io.File

import org.vsw.nlp.ml.crf.pos.POSTester ;

object postest {
  def main(args: Array[String]) {
    val tester = new POSTester() ;
    tester.test(args) ;
  }
}
