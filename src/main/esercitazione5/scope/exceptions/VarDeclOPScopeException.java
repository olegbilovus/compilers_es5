package main.esercitazione5.scope.exceptions;

public class VarDeclOPScopeException extends RuntimeException {

  public VarDeclOPScopeException(String code) {
    super("The number of declared variables is not the same of the constants in: '" + code + "'");
  }

}
