package main.esercitazione5.ast.nodes.stat;

import java.util.List;
import main.esercitazione5.ast.nodes.IdNode;
import main.esercitazione5.ast.nodes.expr.Expr;
import main.esercitazione5.visitors.Visitor;

public class AssignOP extends Stat {

  private final List<IdNode> idNodeList;
  private final List<Expr> exprList;


  public AssignOP(List<IdNode> idNodeList, List<Expr> exprList) {
    this.idNodeList = idNodeList;
    this.exprList = exprList;
  }

  public List<Expr> getExprList() {
    return exprList;
  }

  public List<IdNode> getIdNodeList() {
    return idNodeList;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
