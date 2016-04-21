import java.io.File

import org.vsw.nlp.classification.svm.SVMTrainer ;

object svmtrain {
  def main(args: Array[String]) {
    val trainner = new SVMTrainer() ;
    SVMTrainer.train(args) ;
  }
}
