import java.io.File

import org.vsw.nlp.ml.crf.sent.SentTrainer ;

object senttrain {
  def main(args: Array[String]) {
    val trainer = new SentTrainer() ;
    trainer.train(args) ;
  }
}
