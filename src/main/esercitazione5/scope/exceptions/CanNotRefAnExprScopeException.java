package main.esercitazione5.scope.exceptions;

public class CanNotRefAnExprScopeException extends RuntimeException {

  public CanNotRefAnExprScopeException(String expr, Integer paramNum, String procName,
      String code) {
    super("'" + expr + "' cannot be used as param #" + paramNum + " in the procedure '" + procName
        + "' because it is a ref param, error in '" + code + "'");
  }

}
