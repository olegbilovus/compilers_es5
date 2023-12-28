package main.esercitazione5.scope;

import java.util.HashMap;
import java.util.Map;
import main.esercitazione5.StringTable;
import main.esercitazione5.ast.nodes.Node;
import main.esercitazione5.scope.exceptions.AlreadyDeclaredScopeException;
import main.esercitazione5.scope.exceptions.UndeclaredScopeException;
import main.esercitazione5.visitors.DebugVisitor;

public class ScopeTable {

  private final HashMap<Integer, ScopeEntry> table = new HashMap<>();

  private final ScopeTable prev;


  public ScopeTable(ScopeTable prev) {
    this.prev = prev;
  }

  public Map<Integer, ScopeEntry> getTable() {
    return table;
  }

  public ScopeTable getPrev() {
    return prev;
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

  public void add(Integer id, ScopeEntry entry, StringTable st, Node v) throws
      AlreadyDeclaredScopeException {
    if (table.containsKey(id)) {
      throw new AlreadyDeclaredScopeException(st.get(id),
          v.accept(new DebugVisitor(st)));
    }
    table.put(id, entry);
  }

  @Override public String toString() {
    return "ScopeTable{" +
        "table=" + table +
        ", prev=" + prev +
        '}';
  }
}
