package main.esercitazione5.ast.nodes.expr;

import main.esercitazione5.visitors.Visitor;

public class IdNode extends Expr {

  private final Integer id;
  private final Boolean ref;

  public IdNode(Integer id) {
    super();
    this.id = id;
    ref = false;
  }

  public IdNode(Integer id, Boolean ref) {
    super();
    this.id = id;
    this.ref = ref;
  }

  public Integer getId() {
    return id;
  }

  public Boolean isRef() {
    return ref;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
