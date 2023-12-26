package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Utility;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProcOP;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProcOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    ProcOP procOP = programOP.getProcOPList().get(0);
    Assertions.assertEquals(1, procOP.getId().getId());
    Assertions.assertTrue(Utility.isListEmpty(procOP.getProcFunParamOPList()));
    Assertions.assertNull(procOP.getBodyOP());

    source =
        new StringReader("proc p(out a: integer, b: real): a ^= 0; endproc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    procOP = programOP.getProcOPList().get(0);
    Assertions.assertEquals(1, procOP.getId().getId());
    Assertions.assertEquals(2, procOP.getProcFunParamOPList().size());
    Assertions.assertNotNull(procOP.getBodyOP());

  }

  @Test
  public void invalid() {

    // adding return TYPE in the signature
    StringReader source =
        new StringReader("proc p() -> real: endproc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing endproc
    source = new StringReader("proc p(): endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
