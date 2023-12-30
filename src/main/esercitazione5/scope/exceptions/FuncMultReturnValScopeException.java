package main.esercitazione5.scope.exceptions;

public class FuncMultReturnValScopeException extends RuntimeException {


  public FuncMultReturnValScopeException(String symbol, String code, int given) {
    super("function '" + symbol + "', with " + given
        + " return values is not allowed here, error in '" + code + "'");
  }

}
