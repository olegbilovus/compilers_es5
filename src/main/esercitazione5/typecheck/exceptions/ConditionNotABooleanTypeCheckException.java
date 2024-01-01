package main.esercitazione5.typecheck.exceptions;

import main.esercitazione5.ast.Type;

public class ConditionNotABooleanTypeCheckException extends RuntimeException {

  public ConditionNotABooleanTypeCheckException(Type type, Class op, String code) {
    super("The condition of " + op.getSimpleName() + " is a " + type.name()
        + " but has to be a Boolean, error in: '" + code + "'");
  }

}
