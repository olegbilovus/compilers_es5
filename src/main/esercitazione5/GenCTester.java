package main.esercitazione5;

import java.io.StringReader;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.visitors.GenCVisitor;
import main.esercitazione5.visitors.ScopingVisitor;
import main.esercitazione5.visitors.SemanticVisitor;
import main.esercitazione5.visitors.TypeCheckVisitor;

public class GenCTester {

  public static void main(String[] args) throws Exception {
    StringReader in = Utility.readFile(args[0], args[1]);
    Yylex lexer = new Yylex(in);
    parser p = new parser(lexer);

    ProgramOP ast = (ProgramOP) p.parse().value;

    ast.accept(new SemanticVisitor(lexer.getStringTable()));
    ast.accept(new ScopingVisitor(lexer.getStringTable()));
    ast.accept(new TypeCheckVisitor(lexer.getStringTable()));
    String genC = ast.accept(new GenCVisitor(lexer.getStringTable()));

    System.out.println(genC);
  }
}
