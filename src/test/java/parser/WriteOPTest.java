package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Utility;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.WriteOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WriteOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: -->; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    WriteOP writeOP = (WriteOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertTrue(Utility.isListEmpty(writeOP.getExprList()));
    Assertions.assertFalse(writeOP.hasNewline());

    source =
        new StringReader(
            "func f() -> real: -->!; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    writeOP = (WriteOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertTrue(Utility.isListEmpty(writeOP.getExprList()));
    Assertions.assertTrue(writeOP.hasNewline());

    source =
        new StringReader(
            "func f() -> real: --> \"hello\" $(a); endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    writeOP = (WriteOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertEquals(2, writeOP.getExprList().size());
    Assertions.assertFalse(writeOP.hasNewline());

    source =
        new StringReader(
            "func f() -> real: --> \"hello\" + \"world\" + \"!!\" $(a) \"end\"; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    writeOP = (WriteOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertEquals(3, writeOP.getExprList().size());
    Assertions.assertFalse(writeOP.hasNewline());
  }

  @Test
  public void invalid() {

    // concat a String with a number
    StringReader source = new StringReader(
        "func f() -> real: --> \"hello\" + 4 $(a); endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
