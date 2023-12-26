package main.esercitazione5;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.visitors.DebugVisitor;
import main.esercitazione5.visitors.GraphvizASTVisitor;
import main.esercitazione5.visitors.Visitor;

public class ParserTester {

  public static void main(String[] args) throws Exception {
    String filePath =
        args[0] + File.separator + "src" + File.separator + "test_files" + File.separator + args[1];
    StringReader in = new StringReader(Files.readString(Paths.get(filePath)));
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
