package main.esercitazione5.ast.nodes;

import main.esercitazione5.ast.nodes.expr.Expr;
import main.esercitazione5.visitors.Visitor;

public class IdNode extends Expr {

  private final Integer id;

  public IdNode(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
