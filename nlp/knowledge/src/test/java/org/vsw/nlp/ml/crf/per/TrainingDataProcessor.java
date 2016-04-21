package org.vsw.nlp.ml.crf.per;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class TrainingDataProcessor {

  public static void main(String[] args) throws FileNotFoundException, IOException {
    String content = getContents("src/test/resources/PERtagged/PersonTrainingFix3.wtag");
    setContents(new File("src/test/resources/PERtagged/output.wtag"), content);
  }

  public static String getContents(String filename) {
    File file = new File(filename);
    StringBuffer contents = new StringBuffer();
    BufferedReader reader = null;

    try {
      reader = new BufferedReader(new FileReader(file));
      String text = null;

      while ((text = reader.readLine()) != null) {
        if(text.split(" ").length > 6)
        contents.append(text).append(System.getProperty("line.separator"));
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return contents.toString();

  }

  static public void setContents(File aFile, String aContents)
      throws FileNotFoundException, IOException {

    Writer output = new BufferedWriter(new FileWriter(aFile));
    try {
      // FileWriter always assumes default encoding is OK!
      output.write(aContents);
    } finally {
      output.close();
    }
  }
}
