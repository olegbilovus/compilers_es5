package main.esercitazione5.ast.nodes.expr;

import java.util.List;
import main.esercitazione5.ast.nodes.Node;

public abstract class Expr extends Node {

  private final List<Expr> exprList;

  private Boolean inPar = false;

  protected Expr() {
    exprList = null;
  }

  protected Expr(Expr expr) {
    exprList = List.of(expr);
  }

  protected Expr(List<Expr> exprList) {
    this.exprList = exprList;
  }

  protected Expr(Expr exprLeft, Expr exprRight) {
    this(List.of(exprLeft, exprRight));
  }

  public List<Expr> getExprList() {
    return exprList;
  }

  public Expr getExprLeft() {
    if (exprList != null && !exprList.isEmpty()) {
      return exprList.get(0);
    }
    return null;
  }

  public Expr getExprRight() {
    if (exprList != null && exprList.size() >= 2) {
      return exprList.get(1);
    }
    return null;
  }

  public Boolean isInPar() {
    return inPar;
  }

  public void setInPar(Boolean inPar) {
    this.inPar = inPar;
  }
}

