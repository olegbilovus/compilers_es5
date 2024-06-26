package main.esercitazione5.ast.nodes.stat;

import java.util.List;
import main.esercitazione5.ast.nodes.BodyOP;
import main.esercitazione5.ast.nodes.expr.Expr;
import main.esercitazione5.visitors.Visitor;

public class IfOP extends Stat {

  private final Expr condition;
  private final BodyOP body;

  private final List<ElifOP> elifOPList;

  private final ElseOP elseOP;


  public IfOP(Expr condition, BodyOP body, List<ElifOP> elifOPList, ElseOP elseOP) {
    this.condition = condition;
    this.body = body;
    this.elifOPList = elifOPList;
    this.elseOP = elseOP;
  }

  public Expr getCondition() {
    return condition;
  }

  public BodyOP getBody() {
    return body;
  }

  public List<ElifOP> getElifOPList() {
    return elifOPList;
  }

  public ElseOP getElse() {
    return elseOP;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
