package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.ElifOP;
import main.esercitazione5.ast.nodes.stat.IfOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ElifOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader(
            "func f() -> real: if true then elseif a < 0 then a^= 1; endif; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    ElifOP elifOP =
        ((IfOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0)).getElifOPList()
            .get(0);
    Assertions.assertNotNull(elifOP.getCondition());
    Assertions.assertNotNull(elifOP.getBody());

    source =
        new StringReader(
            "func f() -> real: if true then elseif a < 0 then endif; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    elifOP =
        ((IfOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0)).getElifOPList()
            .get(0);
    Assertions.assertNotNull(elifOP.getCondition());
    Assertions.assertNull(elifOP.getBody());
  }

  @Test
  public void invalid() {

    // missing condition
    StringReader source =
        new StringReader(
            "func f() -> real: if true then elseif then endif; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing THEN
    source = new StringReader(
        "func f() -> real: if true then elseif true endif; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
