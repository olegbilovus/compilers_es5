package main.esercitazione5.ast.nodes;


import java.util.List;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.expr.IdNode;
import main.esercitazione5.visitors.Visitor;

public class FunOP extends Node {

  private final IdNode id;
  private final List<ProcFunParamOP> procFunParamOPList;

  private final List<Type> returnTypes;

  private final BodyOP bodyOP;

  public FunOP(IdNode id, List<ProcFunParamOP> procFunParamOPList, List<Type> returnTypes,
      BodyOP bodyOP) {
    this.id = id;
    this.procFunParamOPList = procFunParamOPList;
    this.returnTypes = returnTypes;
    this.bodyOP = bodyOP;
  }

  public IdNode getId() {
    return id;
  }

  public List<ProcFunParamOP> getProcFunParamOPList() {
    return procFunParamOPList;
  }

  public List<Type> getReturnTypes() {
    return returnTypes;
  }

  public BodyOP getBodyOP() {
    return bodyOP;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
