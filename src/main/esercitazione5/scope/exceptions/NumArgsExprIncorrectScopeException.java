package main.esercitazione5.scope.exceptions;

public class NumArgsExprIncorrectScopeException extends RuntimeException {

  public NumArgsExprIncorrectScopeException(String code, String funName, int expected, int given) {
    super("The number of arguments is not the same of the declared in function: '" + funName
        + "', error in: '" + code + "'. Expected: " + expected + ", given: " + given);
  }

}
