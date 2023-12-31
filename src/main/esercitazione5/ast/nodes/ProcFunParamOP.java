package main.esercitazione5.ast.nodes;


import main.esercitazione5.ast.ParamAccess;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.expr.IdNode;
import main.esercitazione5.visitors.Visitor;

public class ProcFunParamOP extends Node {

  private final ParamAccess paramAccess;
  private final IdNode id;

  private Type type;

  public ProcFunParamOP(ParamAccess paramAccess, IdNode id, Type type) {
    this.paramAccess = paramAccess;
    this.id = id;
    this.type = type;
  }

  public ParamAccess getParamAccess() {
    return paramAccess;
  }

  public IdNode getId() {
    return id;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
