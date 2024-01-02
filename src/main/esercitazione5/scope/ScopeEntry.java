package main.esercitazione5.scope;

import java.util.List;
import main.esercitazione5.ast.Type;

public class ScopeEntry {

  private final ScopeKind kind;

  // for FUN or PROC args or VAR decl
  private final List<ScopeType> listType1;

  // for return types of FUN
  private final List<Type> listType2;

  private final int nodeCount;

  private boolean global = false;


  public ScopeEntry(ScopeKind kind, List<ScopeType> listType1, List<Type> listType2,
      int nodeCount) {
    this.kind = kind;
    this.listType1 = listType1;
    this.listType2 = listType2;
    this.nodeCount = nodeCount;
  }

  public ScopeEntry(ScopeKind kind, ScopeType entry, int nodeCount) {
    this.kind = kind;
    this.listType1 = List.of(entry);
    listType2 = null;
    this.nodeCount = nodeCount;
  }

  public ScopeKind getKind() {
    return kind;
  }

  public List<ScopeType> getListType1() {
    return listType1;
  }

  public List<Type> getListType2() {
    return listType2;
  }

  // used for VAR only
  public ScopeType getType() {
    return listType1 != null ? listType1.get(0) : null;
  }

  public int getNodeCount() {
    return nodeCount;
  }

  public boolean isGlobal() {
    return global;
  }

  public void setGlobal(boolean global) {
    this.global = global;
  }

  @Override public String toString() {
    return "ScopeEntry{" + "kind=" + kind + ", listType1=" + listType1 + ", listType2=" + listType2
        + '}';
  }
}
