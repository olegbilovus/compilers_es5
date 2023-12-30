package main.esercitazione5;

import java.io.StringReader;
import java.util.Scanner;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.visitors.GraphvizScopeTablesVisitor;
import main.esercitazione5.visitors.ScopingVisitor;
import main.esercitazione5.visitors.SemanticVisitor;

public class ScopingTester {

  public static void main(String[] args) throws Exception {
    StringReader in = Utility.readFile(args[0], args[1]);
    Yylex lexer = new Yylex(in);
    parser p = new parser(lexer);

    ProgramOP ast = (ProgramOP) p.parse().value;

    Scanner s = new Scanner(System.in);
    System.out.println("""
        Select the Visitor:
        1. ScopingVisitor
        2. GraphvizScopeTablesVisitor
        """);
    int c = s.nextInt();

    ast.accept(new SemanticVisitor(lexer.getStringTable()));
    ScopingVisitor scopingVisitor = new ScopingVisitor(lexer.getStringTable());
    ast.accept(scopingVisitor);

    System.out.println(scopingVisitor + "\n");

    if (c == 2) {
      System.out.println(ast.accept(new GraphvizScopeTablesVisitor(lexer.getStringTable())));
    }
  }
}
