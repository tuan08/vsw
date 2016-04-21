/**
 * Copyright (C) 2011 Headvances Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.

 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This project aim to build a set of library/data to process 
 * the Vietnamese language and analyze the web data
 **/
package org.vsw.nlp.ml.crf.qtag;

import java.io.IOException;
import java.util.List;

import org.vsw.nlp.ml.crf.FeatureGenerator;
import org.vsw.nlp.ml.crf.FeatureHolder;
import org.vsw.nlp.ml.crf.per.PrefixDictionary;
import org.vsw.nlp.ml.crf.pos.tag.PosTag;
import org.vsw.nlp.token.IToken;
import org.vsw.nlp.token.tag.CharacterTag;
import org.vsw.nlp.token.tag.DigitTag;
import org.vsw.nlp.token.tag.EmailTag;
import org.vsw.nlp.token.tag.NumberTag;
import org.vsw.nlp.token.tag.Tag;
import org.vsw.nlp.token.tag.URLTag;
import org.vsw.util.StringUtil;

public class NGramFeatureGenerator implements FeatureGenerator {
  static String[] NORMALIZE_TYPES = { DigitTag.TYPE, NumberTag.TYPE,
      CharacterTag.TYPE, URLTag.TYPE, EmailTag.TYPE };

  private PrefixDictionary prefixdict;

  public NGramFeatureGenerator() throws IOException {
    prefixdict = new PrefixDictionary();
  }

  public void generate(IToken[] token, int pos, FeatureHolder holder) {
    add1gram(token, pos - 2, "1g:p2:", holder);
    add1gram(token, pos - 1, "1g:p1:", holder);
    add1gram(token, pos,     "1g:0:", holder);
    add1gram(token, pos + 1, "1g:1:", holder);
    add1gram(token, pos + 2, "1g:2:", holder);
    add2gram(token, pos - 2, "2g:p2p1:", holder);
    add2gram(token, pos - 1, "2g:p10:", holder);
    add2gram(token, pos - 0, "2g:01:", holder);
    add2gram(token, pos + 1, "2g:12:", holder);
    //add3gram(token, pos - 2, "trigram:-2:-1:0:", holder);    
  }

  void add1gram(IToken[] token, int pos, String label, FeatureHolder holder) {
    if (pos < 0 || pos >= token.length)
      return;
    List<Tag> tags = token[pos].getTagByType(NORMALIZE_TYPES);
    if (tags != null && tags.size() > 0) {
      for (int i = 0; i < tags.size(); i++) {
        holder.addFeature(label + tags.get(i).getOType());
      }
      return;
    }
    String word = StringUtil.joinStringArray(token[pos].getWord(), "+");
    
    if (prefixdict.containPrefix(word, false))
      holder.addFeature(label + "prefix");
        
    holder.addFeature(label + word);
    
    List<Tag> postags = token[pos].getTag() ;
    for(int i = 0; i< postags.size(); i++) {
      Tag tag = postags.get(i) ;
      if(tag instanceof PosTag) {
        holder.addFeature(label + ((PosTag) tag).getTagValue()) ;
      }
    }
  }

  void add2gram(IToken[] token, int pos, String label, FeatureHolder holder) {
    if (pos < 0 || pos >= token.length - 1)
      return;

    String word1 = StringUtil.joinStringArray(token[pos].getWord(), "+");
    String word2 = StringUtil.joinStringArray(token[pos + 1].getWord(), "+");

    if (prefixdict.containPrefix(word1 + " " + word2, false))
      holder.addFeature(label + "prefix");

    holder.addFeature(label + word1 + "_" + word2);
  }

  void add3gram(IToken[] token, int pos, String label, FeatureHolder holder) {
    if (pos < 0 || pos >= token.length - 2)
      return;

    String word1 = StringUtil.joinStringArray(token[pos].getWord(), "+");
    String word2 = StringUtil.joinStringArray(token[pos + 1].getWord(), "+");
    String word3 = StringUtil.joinStringArray(token[pos + 2].getWord(), "+");

    if (prefixdict.containPrefix(word1 + " " + word2 + " " + word3, false))
      holder.addFeature(label + "prefix");
    
    holder.addFeature(label + word1 + "_" + word2 + "_" + word3);
  }
}