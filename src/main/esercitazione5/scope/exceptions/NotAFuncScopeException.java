package main.esercitazione5.scope.exceptions;

public class NotAFuncScopeException extends RuntimeException {

  public NotAFuncScopeException(String symbol, String code) {
    super("'" + symbol + "' is not a function in this scope, error in '" + code + "'");
  }

}
