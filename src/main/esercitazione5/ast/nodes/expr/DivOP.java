package main.esercitazione5.ast.nodes.expr;

import main.esercitazione5.visitors.Visitor;

public class DivOP extends Expr {

  public DivOP(Expr exprLeft, Expr exprRight) {
    super(exprLeft, exprRight);
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
