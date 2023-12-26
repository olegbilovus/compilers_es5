package main.esercitazione5;

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

}
