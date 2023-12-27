package test.java.lexer;

import java.io.IOException;
import main.esercitazione5.Yylex;
import main.esercitazione5.sym;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LexerTest {


  @Test
  public void valid() throws IOException {
    String source = "%hello%\nvar a: integer;";
    Yylex lexer = LexerUtility.lexer(source);
    Assertions.assertEquals(sym.VAR, lexer.next_token().sym);
    Assertions.assertEquals(sym.ID, lexer.next_token().sym);
    Assertions.assertEquals(sym.COLON, lexer.next_token().sym);
    Assertions.assertEquals(sym.INTEGER, lexer.next_token().sym);
    Assertions.assertEquals(sym.SEMI, lexer.next_token().sym);
    Assertions.assertEquals(sym.EOF, lexer.next_token().sym);

    source = "proc fun(a:real) endproc";
    lexer = LexerUtility.lexer(source);
    Assertions.assertEquals(sym.PROCEDURE, lexer.next_token().sym);
    Assertions.assertEquals(sym.ID, lexer.next_token().sym);
    Assertions.assertEquals(sym.LPAR, lexer.next_token().sym);
    Assertions.assertEquals(sym.ID, lexer.next_token().sym);
    Assertions.assertEquals(sym.COLON, lexer.next_token().sym);
    Assertions.assertEquals(sym.REAL, lexer.next_token().sym);
    Assertions.assertEquals(sym.RPAR, lexer.next_token().sym);
    Assertions.assertEquals(sym.ENDPROCEDURE, lexer.next_token().sym);
    Assertions.assertEquals(sym.EOF, lexer.next_token().sym);
  }
}
