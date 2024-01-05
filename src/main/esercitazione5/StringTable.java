package main.esercitazione5;

import java.util.HashMap;
import java.util.Map.Entry;

public class StringTable {

  /* Using two tables because later we will need to access the string table by the name and
   * the index. Only one table would make the access to the index O(n).
   * The trade-off is using more memory but the access will be O(1).
   */
  private final HashMap<String, Integer> table = new HashMap<>();
  private final HashMap<Integer, String> tableIndex = new HashMap<>();
  private int idCount = 0;

  public Integer get(String key) {
    return table.get(key);
  }

  public String get(Integer key) {
    return tableIndex.get(key);
  }

  public void put(String key) {
    table.computeIfAbsent(key, k -> ++this.idCount);
    tableIndex.computeIfAbsent(this.idCount, k -> key);
  }

  public void replace(Integer value, String key) {
    tableIndex.replace(value, key);
    table.remove(key, value);
    table.put(key, value);
  }

  @Override
  public String toString() {
    int maxLenSymbol = 0;
    int maxLenIndex = 0;

    for (Entry<Integer, String> e : tableIndex.entrySet()) {
      if (e.getValue().length() > maxLenSymbol) {
        maxLenSymbol = e.getValue().length();
      }
      int indexLen = e.getKey().toString().length();
      if (indexLen > maxLenIndex) {
        maxLenIndex = indexLen;
      }
    }
    String strFormat = "| %-" + maxLenIndex + "d | %-" + (maxLenSymbol + 2) + "s |%n";
    StringBuilder toReturn = new StringBuilder();
    for (Entry<Integer, String> e : tableIndex.entrySet()) {
      toReturn.append(String.format(strFormat, e.getKey(), "\"" + e.getValue() + "\""));
    }

    return toReturn.toString();
  }
}
