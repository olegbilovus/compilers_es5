package test.java.lexer;

import java.io.IOException;
import java_cup.runtime.Symbol;
import main.esercitazione5.Yylex;
import main.esercitazione5.sym;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdTest {


  @Test
  public void valid() throws IOException {
    Symbol token = LexerUtility.token("a: integer;");
    Assertions.assertEquals(sym.ID, token.sym);
    Assertions.assertEquals(1, token.value);

    Yylex lexer = LexerUtility.lexer("a b");
    token = lexer.next_token();
    Assertions.assertEquals(sym.ID, token.sym);
    Assertions.assertEquals(1, token.value);
    token = lexer.next_token();
    Assertions.assertEquals(sym.ID, token.sym);
    Assertions.assertEquals(2, token.value);
  }

  @Test
  public void invalid() throws IOException {
    Symbol token = LexerUtility.token("4a ^= 4");
    Assertions.assertEquals(sym.INTEGER_CONST, token.sym);
  }
}
