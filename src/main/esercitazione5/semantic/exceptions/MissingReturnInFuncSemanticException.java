package main.esercitazione5.semantic.exceptions;

public class MissingReturnInFuncSemanticException extends RuntimeException {

  public MissingReturnInFuncSemanticException(String funName) {
    super("Missing at the end a return statement in '" + funName + "'");
  }

}
