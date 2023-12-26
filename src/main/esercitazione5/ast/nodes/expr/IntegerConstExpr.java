package main.esercitazione5.ast.nodes.expr;

import main.esercitazione5.ast.ConstValue;
import main.esercitazione5.visitors.Visitor;

public class IntegerConstExpr extends Expr {

  private final ConstValue constValue;

  public IntegerConstExpr(ConstValue constValue) {
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
