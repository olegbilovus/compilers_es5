package main.esercitazione5.scope.exceptions;

public class NotAProcScopeException extends RuntimeException {

  public NotAProcScopeException(String symbol, String code) {
    super("'" + symbol + "' is not a procedure in this scope, error in '" + code + "'");
  }

}
