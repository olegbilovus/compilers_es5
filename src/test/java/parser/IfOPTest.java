package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Utility;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.IfOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IfOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: if true then endif; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    IfOP ifOP = (IfOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertNotNull(ifOP.getCondition());
    Assertions.assertNull(ifOP.getBody());
    Assertions.assertTrue(Utility.isListEmpty(ifOP.getElifOPList()));
    Assertions.assertNull(ifOP.getElseBody());

    source =
        new StringReader(
            "func f() -> real: if true then a ^= 4; else a ^= 5; endif; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    ifOP = (IfOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertNotNull(ifOP.getCondition());
    Assertions.assertNotNull(ifOP.getBody());
    Assertions.assertTrue(Utility.isListEmpty(ifOP.getElifOPList()));
    Assertions.assertNotNull(ifOP.getElseBody());

    source =
        new StringReader(
            "func f() -> real: if a > 5 then a ^= 4; elseif a < 0 then a^= 1; elseif false then else endif; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    ifOP = (IfOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertNotNull(ifOP.getCondition());
    Assertions.assertNotNull(ifOP.getBody());
    Assertions.assertEquals(2, ifOP.getElifOPList().size());
    Assertions.assertNull(ifOP.getElseBody());
  }

  @Test
  public void invalid() {

    // missing condition
    StringReader source =
        new StringReader("func f() -> real: if then endif; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing THEN
    source = new StringReader("func f() -> real: if true endif; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing ENDIF
    source = new StringReader("func f() -> real: if true then; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
