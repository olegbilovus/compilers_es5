package main.esercitazione5.ast.nodes.expr;

import main.esercitazione5.ast.nodes.IdNode;
import main.esercitazione5.visitors.Visitor;

public class IdNodeExpr extends Expr {

  private final IdNode id;
  private final Boolean ref;

  public IdNodeExpr(IdNode id) {
    super();
    this.id = id;
    ref = false;
  }

  public IdNodeExpr(IdNode id, Boolean ref) {
    super();
    this.id = id;
    this.ref = ref;
  }

  public IdNode getId() {
    return id;
  }

  public Boolean isRef() {
    return ref;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
