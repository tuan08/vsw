import java.io.File

import org.vsw.nlp.test.NLPTestSuite ;

import org.codehaus.jackson.map.ObjectMapper;
import org.vsw.util.CommandParser ;
import org.vsw.util.IOUtil;

object test {
  def main(args: Array[String]) {
    val command = new CommandParser("test:") ;
    command.addOption("test", true, "The input test file or directory, the file ext must end with .suite") ;
    if(!command.parse(args)) return ;
    command.printHelp() ;

    val test = command.getOption("test", null) ;
    val file = new File(test) ;
    if(file.isFile) {
      run(test) ;
    } else {
      for(sel <- findFiles(file)) {
        run(sel.getCanonicalPath()) ;
      }
    }
  }

  def findFiles(dir: File): List[File] = {
    var holder : List[File] = List() ;
    for(sel <- dir.listFiles) {
      if(sel.getName().endsWith(".suite")) {
        holder ::= sel ;
      }
    }
    return holder ;
  }

  def run(file: String) {
    println("run test file: " + file) ;
    val JSON = IOUtil.getFileContenntAsString(file, "UTF-8") ;
    val mapper = new ObjectMapper(); // can reuse, share globally
    val suite = mapper.readValue(JSON , classOf[NLPTestSuite]);
    suite.run() ;
  }

}
