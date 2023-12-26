package main.esercitazione5.ast.nodes.expr;

import main.esercitazione5.visitors.Visitor;

public class MulOP extends Expr {

  public MulOP(Expr exprLeft, Expr exprRight) {
    super(exprLeft, exprRight);
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
