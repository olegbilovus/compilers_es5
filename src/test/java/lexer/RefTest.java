package test.java.lexer;

import java.io.IOException;
import java_cup.runtime.Symbol;
import main.esercitazione5.Yylex;
import main.esercitazione5.sym;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RefTest {


  @Test
  public void valid() throws IOException {
    Yylex lexer = LexerUtility.lexer("p(@a);");
    lexer.next_token();
    lexer.next_token();
    Symbol token = lexer.next_token();
    Assertions.assertEquals(sym.REF, token.sym);
  }
}
