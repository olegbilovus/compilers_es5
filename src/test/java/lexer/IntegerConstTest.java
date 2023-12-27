package test.java.lexer;

import java.io.IOException;
import java_cup.runtime.Symbol;
import main.esercitazione5.sym;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IntegerConstTest {


  @Test
  public void valid() throws IOException {
    Symbol token = LexerUtility.token("4");
    Assertions.assertEquals(sym.INTEGER_CONST, token.sym);

    token = LexerUtility.token("0");
    Assertions.assertEquals(sym.INTEGER_CONST, token.sym);
  }

  @Test
  public void invalid() throws IOException {
    Symbol token = LexerUtility.token(".5");
    Assertions.assertEquals(sym.error, token.sym);
  }
}
