package main.esercitazione5.ast.nodes;

import main.esercitazione5.visitors.Visitor;

public abstract class Node {

  public abstract <T> T accept(Visitor<T> v);

}
