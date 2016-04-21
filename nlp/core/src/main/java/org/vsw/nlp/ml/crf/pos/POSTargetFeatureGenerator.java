package org.vsw.nlp.ml.crf.pos;

import org.vsw.nlp.ml.BoundaryTag;
import org.vsw.nlp.ml.crf.FeatureGenerator;
import org.vsw.nlp.ml.crf.FeatureHolder;
import org.vsw.nlp.token.IToken;

public class POSTargetFeatureGenerator implements FeatureGenerator{
  public void generate(IToken[] token, int pos, FeatureHolder holder) {
    BoundaryTag btag = (BoundaryTag)token[pos].getFirstTagType(BoundaryTag.TYPE) ;
    if(btag != null) {
      String[] feature = btag.getFeatures() ;
      if(feature != null && feature.length > 0) {
        for(String sel : feature) {
          if(sel.startsWith("pos:")) {
            holder.setTargetFeature(sel) ;
            return ;
          }
        }
      }
    }
    holder.setTargetFeature("O") ;
  }
}
