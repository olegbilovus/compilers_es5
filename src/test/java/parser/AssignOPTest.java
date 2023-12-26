package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.AssignOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssignOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: a ^= 2; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    AssignOP assignOP = (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertEquals(1, assignOP.getIdNodeList().size());
    Assertions.assertEquals(1, assignOP.getExprList().size());

    source =
        new StringReader(
            "func f() -> real, boolean: a, b ^= 2+4, 5.5; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    assignOP = (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertEquals(2, assignOP.getIdNodeList().size());
    Assertions.assertEquals(2, assignOP.getExprList().size());

    source =
        new StringReader("func f() -> real, boolean: a, b ^= g(); endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    assignOP = (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertEquals(2, assignOP.getIdNodeList().size());
    Assertions.assertEquals(1, assignOP.getExprList().size());
  }
}
