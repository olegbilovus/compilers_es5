package main.esercitazione5;

import java.io.StringReader;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.visitors.SemanticVisitor;

public class SemanticTester {

  public static void main(String[] args) throws Exception {
    StringReader in = Utility.readFile(args[0], args[1]);
    Yylex lexer = new Yylex(in);
    parser p = new parser(lexer);

    ProgramOP ast = (ProgramOP) p.parse().value;

    SemanticVisitor semanticVisitor = new SemanticVisitor(lexer.getStringTable());
    ast.accept(semanticVisitor);

    System.out.println(SemanticVisitor.SUCCESS + "\n");
  }
}
