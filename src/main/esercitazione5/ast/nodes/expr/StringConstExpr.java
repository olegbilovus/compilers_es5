package main.esercitazione5.ast.nodes.expr;

import main.esercitazione5.ast.ConstValue;
import main.esercitazione5.visitors.Visitor;

public class StringConstExpr extends Expr {

  private final ConstValue constValue;

  public StringConstExpr(ConstValue constValue) {
    super();
    this.constValue = constValue;
  }

  public ConstValue getConstValue() {
    return constValue;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
