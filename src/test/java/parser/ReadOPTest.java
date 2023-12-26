package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Utility;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.ReadOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReadOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: <--; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    ReadOP readOP = (ReadOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertTrue(Utility.isListEmpty(readOP.getExprList()));

    source =
        new StringReader(
            "func f() -> real: <-- \"Input:\" $(a); endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    readOP = (ReadOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertEquals(2, readOP.getExprList().size());

    source =
        new StringReader(
            "func f() -> real: <-- \"Input\" $(a) \"Done\" + \"Thanks\"; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.debug_parse().value;
    readOP = (ReadOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertEquals(3, readOP.getExprList().size());
  }

  @Test
  public void invalid() {

    // multiple IDs in one $()
    StringReader source = new StringReader(
        "func f() -> real: --> \"hello\" + 4 $(a, b); endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // Expr in a $()
    source = new StringReader(
        "func f() -> real: --> \"hello\" + 4 $(a+b); endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
