package main.esercitazione5.ast.nodes;

import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.visitors.Visitor;

public abstract class Node {

  private ScopeTable scopeTable;

  public ScopeTable getScopeTable() {
    return scopeTable;
  }

  public void setScopeTable(ScopeTable scopeTable) {
    this.scopeTable = scopeTable;
  }

  public abstract <T> T accept(Visitor<T> v);

}
