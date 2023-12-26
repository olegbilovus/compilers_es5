package main.esercitazione5.ast.nodes.expr;

import main.esercitazione5.visitors.Visitor;

public class UminusOP extends Expr {

  public UminusOP(Expr expr) {
    super(expr);
  }


  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
