package main.esercitazione5.semantic.exceptions;

import main.esercitazione5.ast.nodes.stat.LetLoopOP;

public class VarDeclInLetLoopSemanticException extends RuntimeException {

  public VarDeclInLetLoopSemanticException(String code) {
    super("Can not declare vars in a " + LetLoopOP.class.getSimpleName() + "error in: '" + code
        + "'");
  }

}
