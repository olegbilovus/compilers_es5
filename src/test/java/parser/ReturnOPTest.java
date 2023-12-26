package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.ReturnOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReturnOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: return 2; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    ReturnOP returnOP = (ReturnOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertEquals(1, returnOP.getExprList().size());

    source =
        new StringReader(
            "func f() -> real, boolean: return 2+4+6, 5.5; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    returnOP = (ReturnOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertEquals(2, returnOP.getExprList().size());
  }
}
