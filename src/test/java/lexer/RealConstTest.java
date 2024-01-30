package test.java.lexer;

import java.io.IOException;
import java_cup.runtime.Symbol;
import main.esercitazione5.Yylex;
import main.esercitazione5.sym;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RealConstTest {


  @Test
  public void valid() throws IOException {
    Symbol token = LexerUtility.token("4.5");
    Assertions.assertEquals(sym.REAL_CONST, token.sym);

    token = LexerUtility.token("0.00005");
    Assertions.assertEquals(sym.REAL_CONST, token.sym);
  }

  @Test
  public void invalid() throws IOException {
    Yylex lexer = LexerUtility.lexer("4..5");
    Symbol token = lexer.next_token();
    Assertions.assertEquals(sym.INTEGER_CONST, token.sym);
    token = lexer.next_token();
    Assertions.assertEquals(sym.DOT, token.sym);

    token = LexerUtility.token(".00005");
    Assertions.assertEquals(sym.DOT, token.sym);
  }
}
