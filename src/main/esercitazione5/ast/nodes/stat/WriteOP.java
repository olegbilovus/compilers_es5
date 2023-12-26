package main.esercitazione5.ast.nodes.stat;

import java.util.List;
import main.esercitazione5.ast.nodes.expr.Expr;
import main.esercitazione5.visitors.Visitor;

public class WriteOP extends Stat {

  private final List<Expr> exprList;
  private final Boolean newline;


  public WriteOP(List<Expr> exprList) {
    this.exprList = exprList;
    newline = false;
  }

  public WriteOP(List<Expr> exprList, Boolean newline) {
    this.exprList = exprList;
    this.newline = newline;
  }

  public List<Expr> getExprList() {
    return exprList;
  }

  public Boolean hasNewline() {
    return newline;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
