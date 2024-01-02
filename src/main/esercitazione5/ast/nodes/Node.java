package main.esercitazione5.ast.nodes;

import java.util.List;
import main.esercitazione5.ast.Type;
import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.visitors.Visitor;

public abstract class Node {

  private static int nodeCount = 0;

  private final int thisNodeCount;

  protected Node() {
    thisNodeCount = ++nodeCount;
  }

  public int getThisNodeCount() {
    return thisNodeCount;
  }

  private ScopeTable scopeTable;

  private List<Type> typeList;

  public ScopeTable getScopeTable() {
    return scopeTable;
  }

  public void setScopeTable(ScopeTable scopeTable) {
    this.scopeTable = scopeTable;
  }

  public List<Type> getTypeList() {
    return typeList;
  }

  public Type getNodeType() {
    return typeList == null ? null : typeList.get(0);
  }

  public void setTypeList(List<Type> typeList) {
    this.typeList = typeList;
  }

  public abstract <T> T accept(Visitor<T> v);

}
