package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Utility;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.CallProcOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CallProcOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: p(); endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    CallProcOP callProcOP =
        (CallProcOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertEquals(2, callProcOP.getId().getId());
    Assertions.assertTrue(Utility.isListEmpty(callProcOP.getParams()));

    source =
        new StringReader("func f() -> real, boolean: p(2+4, @a); endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    callProcOP = (CallProcOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertEquals(2, callProcOP.getId().getId());
    Assertions.assertEquals(2, callProcOP.getParams().size());
  }

  @Test
  public void invalid() {

    // assign a procedure to a variable
    StringReader source = new StringReader(
        "func f() -> real, boolean: a ^= p(2+4, @a); endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
