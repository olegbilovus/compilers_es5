package main.esercitazione5.typecheck.exceptions;

import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.expr.NotOP;

public class NotTypeCheckException extends RuntimeException {

  public NotTypeCheckException(Type left, String code) {
    super(
        "Can not do a " + NotOP.class.getSimpleName() + " on a " + left.name() + " , error in: '"
            + code + "'");
  }

}
