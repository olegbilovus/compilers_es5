package test.java.lexer;

import java.io.IOException;
import java_cup.runtime.Symbol;
import main.esercitazione5.sym;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommentTest {


  @Test
  public void valid() throws IOException {
    Symbol token = LexerUtility.token("%hello%");
    Assertions.assertEquals(sym.EOF, token.sym);

    token = LexerUtility.token("%hello \t world%");
    Assertions.assertEquals(sym.EOF, token.sym);
  }

  @Test
  public void invalid() throws IOException {
    Symbol token = LexerUtility.token("%hello");
    Assertions.assertEquals(sym.error, token.sym);
    Assertions.assertEquals("Comment not closed", token.value);
  }
}
