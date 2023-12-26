package main.esercitazione5.ast.nodes.stat;

import java.util.List;
import main.esercitazione5.ast.nodes.IdNode;
import main.esercitazione5.ast.nodes.expr.Expr;
import main.esercitazione5.visitors.Visitor;

public class CallProcOP extends Stat {

  private final IdNode id;
  private final List<Expr> params;

  public CallProcOP(IdNode id) {
    this.id = id;
    this.params = null;
  }

  public CallProcOP(IdNode id, List<Expr> params) {
    this.id = id;
    this.params = params;
  }

  public IdNode getId() {
    return id;
  }

  public List<Expr> getParams() {
    return params;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
