package main.esercitazione5.ast.nodes.stat;

import java.util.List;
import main.esercitazione5.ast.nodes.expr.Expr;
import main.esercitazione5.visitors.Visitor;

public class ReadOP extends Stat {

  private final List<Expr> exprList;


  public ReadOP(List<Expr> exprList) {
    this.exprList = exprList;
  }


  public List<Expr> getExprList() {
    return exprList;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
