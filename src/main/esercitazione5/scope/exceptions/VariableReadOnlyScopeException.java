package main.esercitazione5.scope.exceptions;

public class VariableReadOnlyScopeException extends RuntimeException {

  public VariableReadOnlyScopeException(String symbol, String code) {
    super("'" + symbol + "' is read only, error in '" + code + "'");
  }

}
