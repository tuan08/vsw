import java.io.File

import org.vsw.nlp.ml.crf.ws.WSTrainer ;

object wstrain {
  def main(args: Array[String]) {
    val trainner = new WSTrainer() ;
    trainner.train(args) ;
  }
}
