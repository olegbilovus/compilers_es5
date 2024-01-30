package main.esercitazione5.ast.nodes.stat;

import java.util.List;
import main.esercitazione5.ast.nodes.VarDeclOP;
import main.esercitazione5.visitors.Visitor;

public class LetLoopOP extends Stat {

  private final List<VarDeclOP> declOP;

  private final List<WhenOP> whenOPList;

  private final OtherwiseOP otherwiseOP;


  public LetLoopOP(List<VarDeclOP> declOP, List<WhenOP> whenOPList, OtherwiseOP otherwiseOP) {
    this.declOP = declOP;
    this.whenOPList = whenOPList;
    this.otherwiseOP = otherwiseOP;
  }

  public List<VarDeclOP> getDeclOP() {
    return declOP;
  }

  public List<WhenOP> getWhenOPList() {
    return whenOPList;
  }

  public OtherwiseOP getOtherwiseOP() {
    return otherwiseOP;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
