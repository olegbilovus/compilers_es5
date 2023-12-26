package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Utility;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.expr.CallFunOP;
import main.esercitazione5.ast.nodes.stat.AssignOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CallFunOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: a ^= g(); endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    CallFunOP callFunOP =
        (CallFunOP) ((AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList()
            .get(0)).getExprList().get(0);
    Assertions.assertEquals(3, callFunOP.getId().getId());
    Assertions.assertTrue(Utility.isListEmpty(callFunOP.getExprList()));

    source =
        new StringReader("func f() -> real: a ^= g(a, 2+4); endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    callFunOP =
        (CallFunOP) ((AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList()
            .get(0)).getExprList().get(0);
    Assertions.assertEquals(3, callFunOP.getId().getId());
    Assertions.assertEquals(2, callFunOP.getExprList().size());
  }

  @Test
  public void invalid() {

    // use REF in a Function call
    StringReader source = new StringReader(
        "func f() -> real: a ^= g(@a, 2+4); endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
