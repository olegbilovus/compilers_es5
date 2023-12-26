package main.esercitazione5.scope.exceptions;

public class UndeclaredScopeException extends RuntimeException {

  public UndeclaredScopeException(String symbol) {
    super("'" + symbol + "' undeclared");
  }

}
