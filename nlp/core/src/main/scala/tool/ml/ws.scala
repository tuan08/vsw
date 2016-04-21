import java.io.File

import org.vsw.nlp.ml.crf.ws.WordSegmenter ;

object ws {
  def main(args: Array[String]) {
    WordSegmenter.segment(args) ;
  }
}
