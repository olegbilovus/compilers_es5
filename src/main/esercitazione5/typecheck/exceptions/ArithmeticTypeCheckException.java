package main.esercitazione5.typecheck.exceptions;

import main.esercitazione5.ast.Type;

public class ArithmeticTypeCheckException extends RuntimeException {

  public ArithmeticTypeCheckException(Type left, Type right, Class op, String code) {
    super("Can not do a " + op.getSimpleName() + " between a " + left.name() + " and a "
        + right.name() + ", error in: '" + code + "'");
  }

}
