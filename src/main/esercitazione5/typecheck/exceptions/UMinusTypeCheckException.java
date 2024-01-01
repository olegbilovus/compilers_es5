package main.esercitazione5.typecheck.exceptions;

import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.expr.UminusOP;

public class UMinusTypeCheckException extends RuntimeException {

  public UMinusTypeCheckException(Type left, String code) {
    super(
        "Can not do a " + UminusOP.class.getSimpleName() + " on a " + left.name() + " , error in: '"
            + code + "'");
  }

}
