package main.esercitazione5.semantic.exceptions;

public class NumIdsNumConstsDiffSemanticException extends RuntimeException {

  public NumIdsNumConstsDiffSemanticException(String code) {
    super("The number of declared variables is not the same of the constants in: '" + code + "'");
  }

}
