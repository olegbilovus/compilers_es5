package test.java.lexer;

import java.io.IOException;
import java_cup.runtime.Symbol;
import main.esercitazione5.sym;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DollarSignTest {


  @Test
  public void valid() throws IOException {
    Symbol token = LexerUtility.token("$(a)");
    Assertions.assertEquals(sym.DOLLARSIGN, token.sym);
  }
}
