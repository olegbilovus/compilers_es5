package test.java.lexer;

import java.io.IOException;
import java.io.StringReader;
import java_cup.runtime.Symbol;
import main.esercitazione5.Yylex;

public class LexerUtility {

  private LexerUtility() {
  }

  public static Symbol token(String sourceStr) throws IOException {
    StringReader source = new StringReader(sourceStr);
    Yylex lexer = new Yylex(source);
    return lexer.next_token();
  }

  public static Yylex lexer(String sourceStr) {
    StringReader source = new StringReader(sourceStr);
    return new Yylex(source);
  }

}
