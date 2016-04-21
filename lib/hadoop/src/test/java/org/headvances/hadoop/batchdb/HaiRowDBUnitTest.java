package org.headvances.hadoop.batchdb;

import org.headvances.hadoop.util.HDFSUtil;
import org.junit.Test;
import org.vsw.util.FileUtil;

public class HaiRowDBUnitTest {

  @Test
  public void test() throws Exception {
    String dblocation = "target/db" ;
    FileUtil.removeIfExist(dblocation) ;
    ColumnDefinition[] columnDefinition = {
      new ColumnDefinition("column1"), new ColumnDefinition("column2")
    } ;
    DatabaseConfiguration dbconfiguration = 
      new DatabaseConfiguration(columnDefinition, new RowIdPartitioner.RowIdHashPartioner(3, ":")) ;
    dbconfiguration.setHadoopConfiguration(HDFSUtil.getDaultConfiguration()) ;
    RowDB rowDB = new RowDB(dblocation, "rowdb", dbconfiguration) ;
  }
}
