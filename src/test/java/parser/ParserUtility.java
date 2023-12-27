package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;

public class ParserUtility {

  private ParserUtility() {
  }

  public static ProgramOP ast(String sourceStr) throws Exception {
    StringReader source = new StringReader(sourceStr);
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    return (ProgramOP) p.parse().value;
  }

  public static parser parser(String sourceStr) {
    StringReader source = new StringReader(sourceStr);
    Yylex lexer = new Yylex(source);
    return new parser(lexer);
  }

}
