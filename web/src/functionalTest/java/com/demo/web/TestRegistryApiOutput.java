import java.io.*;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestRegistryApiOutput {
 
   private static final File testFolder = new File("../test");

   @Test
   public void testRegistryOutput() throws IOException {
      final File bench = new File(testFolder,"bench/registry.bench");
      final File output = new File(testFolder,"output/entries.txt");
      Assert.assertEquals(FileUtils.readLines(bench), FileUtils.readLines(output));
   }

}  
