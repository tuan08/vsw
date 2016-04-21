import java.io.File

import org.vsw.nlp.ml.crf.ent.EntityTrainer ;

object enttrain {
  def main(args: Array[String]) {
    val trainner = new EntityTrainer() ;
    trainner.train(args) ;
  }
}
