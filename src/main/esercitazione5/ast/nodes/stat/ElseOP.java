package main.esercitazione5.ast.nodes.stat;

import main.esercitazione5.ast.nodes.BodyOP;
import main.esercitazione5.visitors.Visitor;

public class ElseOP extends Stat {

  private final BodyOP body;


  public ElseOP(BodyOP body) {
    this.body = body;
  }

  public BodyOP getBody() {
    return body;
  }


  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
