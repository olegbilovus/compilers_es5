package test.java.lexer;

import java.io.IOException;
import java_cup.runtime.Symbol;
import main.esercitazione5.sym;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringConstTest {


  @Test
  public void valid() throws IOException {
    Symbol token = LexerUtility.token("\"hello\"");
    Assertions.assertEquals(sym.STRING_CONST, token.sym);
    Assertions.assertEquals(1, token.value);

    token = LexerUtility.token("\"hello \t hey \n world\"");
    Assertions.assertEquals(sym.STRING_CONST, token.sym);
    Assertions.assertEquals(1, token.value);
  }

  @Test
  public void invalid() throws IOException {
    Symbol token = LexerUtility.token("\"hello");
    Assertions.assertEquals(sym.error, token.sym);
    Assertions.assertEquals("Constant String not completed", token.value);
  }
}
