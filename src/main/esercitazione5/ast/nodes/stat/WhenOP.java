package main.esercitazione5.ast.nodes.stat;

import java.util.List;
import main.esercitazione5.ast.nodes.BodyOP;
import main.esercitazione5.ast.nodes.expr.Expr;
import main.esercitazione5.visitors.Visitor;

public class WhenOP extends Stat {

  private final Expr condition;
  private final BodyOP body;


  public WhenOP(Expr condition, BodyOP body) {
    this.condition = condition;
    this.body = body;
  }

  public Expr getCondition() {
    return condition;
  }

  public BodyOP getBody() {
    return body;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
