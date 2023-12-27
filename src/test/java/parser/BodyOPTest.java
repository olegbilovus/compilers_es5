package test.java.parser;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.nodes.BodyOP;
import main.esercitazione5.ast.nodes.ProgramOP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BodyOPTest {

  @Test
  public void valid() throws Exception {
    ProgramOP programOP =
        ParserUtility.ast("func f() -> real: return 1.2; endfunc proc main(): endproc");
    BodyOP bodyOP = programOP.getFunOPList().get(0).getBodyOP();
    Assertions.assertTrue(Utility.isListEmpty(bodyOP.getVarDeclOPList()));
    Assertions.assertEquals(1, bodyOP.getStatList().size());

    programOP =
        ParserUtility.ast("proc f(): var a ^= 4; b: boolean;\\ endproc proc main(): endproc");
    bodyOP = programOP.getProcOPList().get(0).getBodyOP();
    Assertions.assertEquals(2, bodyOP.getVarDeclOPList().size());
    Assertions.assertTrue(Utility.isListEmpty(bodyOP.getStatList()));
  }

}
