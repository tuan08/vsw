import java.io.File

import org.vsw.nlp.ml.crf.sent.SentTester ;

object senttest {
  def main(args: Array[String]) {
    val tester = new SentTester() ;
    tester.test(args) ;
  }
}
