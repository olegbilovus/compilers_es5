package main.esercitazione5.scope.exceptions;

public class NotARefParameterScopeException extends RuntimeException {

  public NotARefParameterScopeException(String symbol, Integer paramNum, String procName,
      String code) {
    super("'" + symbol + "' is trying to use REF to a parameter which is not OUT. The parameter #"
        + paramNum + " in the procedure '" + procName + "' does not requires a REF, error in '"
        + code + "'");
  }

}
