import java.io.File

import org.vsw.nlp.ml.crf.sent.SentIOB2 ;

object sentiob2 {
  def main(args: Array[String]) {
    val iob2 = new SentIOB2() ;
    iob2.run(args) ;
  }
}
