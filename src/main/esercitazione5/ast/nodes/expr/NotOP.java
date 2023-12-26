package main.esercitazione5.ast.nodes.expr;

import main.esercitazione5.visitors.Visitor;

public class NotOP extends Expr {

  public NotOP(Expr expr) {
    super(expr);
  }


  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
