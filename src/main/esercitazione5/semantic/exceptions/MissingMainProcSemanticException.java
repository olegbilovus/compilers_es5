package main.esercitazione5.semantic.exceptions;

public class MissingMainProcSemanticException extends RuntimeException {

  public MissingMainProcSemanticException() {
    super("Missing a main procedure");
  }

}
