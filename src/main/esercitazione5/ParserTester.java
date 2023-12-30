package main.esercitazione5;

import java.io.StringReader;
import java.util.Scanner;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.visitors.DebugVisitor;
import main.esercitazione5.visitors.GraphvizASTVisitor;
import main.esercitazione5.visitors.Visitor;

public class ParserTester {

  public static void main(String[] args) throws Exception {
    StringReader in = Utility.readFile(args[0], args[1]);
    Yylex lexer = new Yylex(in);
    parser p = new parser(lexer);

    ProgramOP ast = (ProgramOP) p.parse().value;

    Scanner s = new Scanner(System.in);
    System.out.println("""
        Select the Visitor:
        1. DebugVisitor
        2. GraphvizASTVisitor
        """);
    int c = s.nextInt();

    Visitor<String> v = switch (c) {
      case 2 -> new GraphvizASTVisitor(lexer.getStringTable());
      default -> new DebugVisitor(lexer.getStringTable());
    };

    System.out.println(v.visit(ast));
  }
}
