package test.java.lexer;

import java.io.IOException;
import java.io.StringReader;
import java_cup.runtime.Symbol;
import main.esercitazione5.Yylex;
import main.esercitazione5.sym;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegerConstTest {


  @Test
  public void valid() throws IOException {
    StringReader source = new StringReader("4");
    Yylex lexer = new Yylex(source);
    Symbol token = lexer.next_token();
    Assertions.assertEquals(sym.INTEGER_CONST, token.sym);

    source = new StringReader("0");
    lexer = new Yylex(source);
    token = lexer.next_token();
    Assertions.assertEquals(sym.INTEGER_CONST, token.sym);
  }

  @Test
  public void invalid() throws IOException {
    StringReader source = new StringReader(".5");
    Yylex lexer = new Yylex(source);
    Symbol token = lexer.next_token();
    Assertions.assertEquals(sym.error, token.sym);
  }
}
