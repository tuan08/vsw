import java.io.File

import org.vsw.nlp.ml.crf.ent.EntityTester ;

object enttest {
  def main(args: Array[String]) {
    val tester = new EntityTester() ;
    tester.test(args) ;
  }
}
