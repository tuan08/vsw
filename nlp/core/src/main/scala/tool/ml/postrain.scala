import java.io.File

import org.vsw.nlp.ml.crf.pos.POSTrainer ;

object postrain {
  def main(args: Array[String]) {
    val trainner = new POSTrainer() ;
    trainner.train(args) ;
  }
}
