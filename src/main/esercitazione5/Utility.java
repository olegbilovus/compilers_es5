package main.esercitazione5;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Utility {

  private Utility() {
  }

  public static boolean isListEmpty(List<?> list) {
    return list == null || list.isEmpty();
  }

  public static void deleteLastCommaSpace(StringBuilder sb) {
    int sbLen = sb.length();
    sb.delete(sbLen - 2, sbLen);
  }

  public static StringReader readFile(String dir, String file) throws IOException {
    String filePath =
        dir + File.separator + "test_files" + File.separator + file;
    return new StringReader(Files.readString(Paths.get(filePath)));
  }

}
