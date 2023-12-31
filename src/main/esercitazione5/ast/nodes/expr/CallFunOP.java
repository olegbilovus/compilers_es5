package main.esercitazione5.ast.nodes.expr;

import java.util.List;
import main.esercitazione5.visitors.Visitor;

public class CallFunOP extends Expr {

  private final IdNode id;

  public CallFunOP(IdNode id, List<Expr> params) {
    super(params);
    this.id = id;
  }

  public CallFunOP(IdNode id) {
    super();
    this.id = id;
  }

  public IdNode getId() {
    return id;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
