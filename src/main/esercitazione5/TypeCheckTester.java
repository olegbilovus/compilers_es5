package main.esercitazione5;

import java.io.StringReader;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.visitors.ScopingVisitor;
import main.esercitazione5.visitors.SemanticVisitor;
import main.esercitazione5.visitors.TypeCheckVisitor;

public class TypeCheckTester {

  public static void main(String[] args) throws Exception {
    StringReader in = Utility.readFile(args[0], args[1]);
    Yylex lexer = new Yylex(in);
    parser p = new parser(lexer);

    ProgramOP ast = (ProgramOP) p.parse().value;

    ast.accept(new SemanticVisitor(lexer.getStringTable()));
    ast.accept(new ScopingVisitor(lexer.getStringTable()));
    ast.accept(new TypeCheckVisitor(lexer.getStringTable()));

    System.out.println(TypeCheckVisitor.SUCCESS + "\n");
  }
}
