package main.esercitazione5.typecheck.exceptions;

import main.esercitazione5.ast.Type;

public class TypeArgsExprIncorrectTypeCheckException extends RuntimeException {

  public TypeArgsExprIncorrectTypeCheckException(String code, String where, int pos, Type expected,
      boolean out, Type given) {
    super("The type of argument #" + pos + " is not the same of the declared in: '" + where
        + "', error in: '" + code + "'. Expected:" + (out ? "OUT:" : "") + expected + ", given: "
        + given);
  }

}
