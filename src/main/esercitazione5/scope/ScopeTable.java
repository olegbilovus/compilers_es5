package main.esercitazione5.scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import main.esercitazione5.StringTable;
import main.esercitazione5.scope.exceptions.AlreadyDeclaredScopeException;
import main.esercitazione5.scope.exceptions.UndeclaredScopeException;

public class ScopeTable {

  private final HashMap<Integer, ScopeEntry> table = new HashMap<>();

  private final ScopeTable prev;

  private final List<ScopeTable> nexts = new ArrayList<>();

  public ScopeTable(ScopeTable prev) {
    this.prev = prev;
  }

  public HashMap<Integer, ScopeEntry> getTable() {
    return table;
  }

  public ScopeTable getPrev() {
    return prev;
  }

  public List<ScopeTable> getNexts() {
    return nexts;
  }

  public ScopeEntry lookup(Integer id, StringTable st) throws UndeclaredScopeException {
    for (ScopeTable cur = this; cur != null; cur = cur.getPrev()) {
      ScopeEntry entry;
      if ((entry = cur.getTable().get(id)) != null) {
        return entry;
      }
    }
    throw new UndeclaredScopeException(st.get(id));
  }

  public void add(Integer id, ScopeEntry entry, StringTable st) throws
      AlreadyDeclaredScopeException {
    if (table.containsKey(id)) {
      throw new AlreadyDeclaredScopeException(st.get(id));
    }
    table.put(id, entry);
  }
}
