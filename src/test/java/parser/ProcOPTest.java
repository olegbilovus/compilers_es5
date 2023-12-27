package test.java.parser;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.nodes.ProcOP;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProcOPTest {

  private ProcOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return programOP.getProcOPList().get(0);
  }

  @Test
  public void valid() throws Exception {
    ProcOP procOP = init("proc main(): endproc");
    Assertions.assertEquals(1, procOP.getId().getId());
    Assertions.assertTrue(Utility.isListEmpty(procOP.getProcFunParamOPList()));
    Assertions.assertNull(procOP.getBodyOP());

    procOP = init("proc p(out a: integer, b: real): a ^= 0; endproc proc main(): endproc");
    Assertions.assertEquals(1, procOP.getId().getId());
    Assertions.assertEquals(2, procOP.getProcFunParamOPList().size());
    Assertions.assertNotNull(procOP.getBodyOP());

  }

  @Test
  public void invalid() {

    // adding return TYPE in the signature
    parser p = ParserUtility.parser("proc p() -> real: endproc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // missing endproc
    p = ParserUtility.parser("proc p(): endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
