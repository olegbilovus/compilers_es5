package main.esercitazione5.ast.nodes.stat;

import main.esercitazione5.ast.nodes.BodyOP;
import main.esercitazione5.visitors.Visitor;

public class OtherwiseOP extends Stat {

  private final BodyOP body;


  public OtherwiseOP(BodyOP body) {
    this.body = body;
  }

  public BodyOP getBody() {
    return body;
  }


  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
