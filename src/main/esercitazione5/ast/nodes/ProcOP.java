package main.esercitazione5.ast.nodes;


import java.util.List;
import main.esercitazione5.visitors.Visitor;

public class ProcOP extends Node {

  private final IdNode id;
  private final List<ProcFunParamOP> procFunParamOPList;
  private final BodyOP bodyOP;

  public ProcOP(IdNode id, List<ProcFunParamOP> procFunParamOPList, BodyOP bodyOP) {
    this.id = id;
    this.procFunParamOPList = procFunParamOPList;
    this.bodyOP = bodyOP;
  }

  public IdNode getId() {
    return id;
  }

  public List<ProcFunParamOP> getProcFunParamOPList() {
    return procFunParamOPList;
  }


  public BodyOP getBodyOP() {
    return bodyOP;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
