package main.esercitazione5.scope.exceptions;

public class NumAssignExprIncorrectScopeException extends RuntimeException {

  public NumAssignExprIncorrectScopeException(String code, int expected, int given) {
    super("The number of expressions is not the same of the variables in: '" + code
        + "'. Expected: " + expected + ", given: " + given);
  }

}
