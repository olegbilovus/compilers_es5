package main.esercitazione5.scope.exceptions;

public class NumReturnExprIncorrectScopeException extends RuntimeException {

  public NumReturnExprIncorrectScopeException(String funName, int expected, int given) {
    super("The number of returned expressions is not the same of the declared in function: '"
        + funName
        + "'. Expected: " + expected + ", given:" + given);
  }

}
