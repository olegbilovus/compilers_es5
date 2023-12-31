package main.esercitazione5.scope.exceptions;

public class MissingRefSymbolScopeException extends RuntimeException {

  public MissingRefSymbolScopeException(String symbol, Integer paramNum, String procName,
      String code) {
    super("'" + symbol + "' is missing the REF symbol '@'. The parameter #" + paramNum
        + " in the procedure '" + procName + "' requires a REF, error in '" + code + "'");
  }

}
