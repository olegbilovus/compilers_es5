package test.java.lexer;

import java.io.IOException;
import java.io.StringReader;
import java_cup.runtime.Symbol;
import main.esercitazione5.Yylex;
import main.esercitazione5.sym;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DollarSignTest {


  @Test
  public void valid() throws IOException {
    StringReader source = new StringReader("$(a)");
    Yylex lexer = new Yylex(source);
    Symbol token = lexer.next_token();
    Assertions.assertEquals(sym.DOLLARSIGN, token.sym);
  }
}
