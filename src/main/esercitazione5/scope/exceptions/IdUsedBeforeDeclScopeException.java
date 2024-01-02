package main.esercitazione5.scope.exceptions;

public class IdUsedBeforeDeclScopeException extends RuntimeException {

  public IdUsedBeforeDeclScopeException(String symbol) {
    super("'" + symbol + "' was declared later but used before that declaration");
  }

}
