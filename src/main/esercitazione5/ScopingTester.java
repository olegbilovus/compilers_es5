package main.esercitazione5;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.visitors.GraphvizScopeTablesVisitor;
import main.esercitazione5.visitors.ScopingVisitor;

public class ScopingTester {

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
        1. ScopingVisitor
        2. GraphvizScopeTablesVisitor
        """);
    int c = s.nextInt();

    ScopingVisitor scopingVisitor = new ScopingVisitor(lexer.getStringTable());
    ast.accept(scopingVisitor);

    System.out.println(scopingVisitor + "\n");

    if (c == 2) {
      System.out.println(ast.accept(new GraphvizScopeTablesVisitor(lexer.getStringTable())));
    }
  }
}
