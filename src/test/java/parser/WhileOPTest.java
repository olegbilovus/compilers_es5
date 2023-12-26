package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.WhileOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WhileOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: while true do endwhile; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    WhileOP whileOP = (WhileOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertNotNull(whileOP.getCondition());
    Assertions.assertNull(whileOP.getBody());

    source =
        new StringReader(
            "func f() -> real: while true do a ^= f(); endwhile; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    whileOP = (WhileOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertNotNull(whileOP.getCondition());
    Assertions.assertNotNull(whileOP.getBody());
  }

  @Test
  public void invalid() {

    // missing condition
    StringReader source =
        new StringReader("func f() -> real: while do endwhile; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing DO
    source =
        new StringReader("func f() -> real: while true endwhile; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing ENDIF
    source = new StringReader("func f() -> real: while true do ; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
