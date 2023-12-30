package main.esercitazione5.semantic.exceptions;

public class ReturnInProcSemanticException extends RuntimeException {

  public ReturnInProcSemanticException(String procName) {
    super("Can not call a return statement in the procedure '" + procName + "'");
  }

}
