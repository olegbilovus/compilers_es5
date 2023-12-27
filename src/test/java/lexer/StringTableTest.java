package test.java.lexer;

import java.io.IOException;
import main.esercitazione5.Yylex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringTableTest {


  @Test
  public void valid() throws IOException {
    String strSource = "hello";
    Yylex lexer = LexerUtility.lexer(strSource);
    lexer.next_token();
    Assertions.assertEquals(1, lexer.getStringTable().get(strSource));
    Assertions.assertEquals(strSource, lexer.getStringTable().get(1));

    strSource = "hello a b";
    String[] strSourceSplit = strSource.split(" ");
    lexer = LexerUtility.lexer(strSource);
    lexer.next_token();
    for (int i = 0; i < strSourceSplit.length; i++, lexer.next_token()) {
      Assertions.assertEquals(i + 1, lexer.getStringTable().get(strSourceSplit[i]));
      Assertions.assertEquals(strSourceSplit[i], lexer.getStringTable().get(i + 1));
    }
  }

  @Test
  public void invalid() throws IOException {
    String strSource = "hello hello";
    String[] strSourceSplit = strSource.split(" ");
    Yylex lexer = LexerUtility.lexer(strSource);
    lexer.next_token();
    lexer.next_token();
    Assertions.assertEquals(1, lexer.getStringTable().get(strSourceSplit[0]));
    Assertions.assertNotEquals(2, lexer.getStringTable().get(1));
  }
}
