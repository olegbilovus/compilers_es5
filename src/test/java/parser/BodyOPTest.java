package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Utility;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.BodyOP;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BodyOPTest {

  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: return 1.2; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    BodyOP bodyOP = programOP.getFunOPList().get(0).getBodyOP();
    Assertions.assertTrue(Utility.isListEmpty(bodyOP.getVarDeclOPList()));
    Assertions.assertEquals(1, bodyOP.getStatList().size());

    source =
        new StringReader("proc f(): var a ^= 4; b: boolean;\\ endproc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    bodyOP = programOP.getProcOPList().get(0).getBodyOP();
    Assertions.assertEquals(2, bodyOP.getVarDeclOPList().size());
    Assertions.assertTrue(Utility.isListEmpty(bodyOP.getStatList()));
  }

}
