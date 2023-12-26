package main.esercitazione5.scope.exceptions;

public class AlreadyDeclaredScopeException extends RuntimeException {

  public AlreadyDeclaredScopeException(String symbol) {
    super("'" + symbol + "' is already declared");
  }

}
