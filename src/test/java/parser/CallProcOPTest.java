package test.java.parser;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.CallProcOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CallProcOPTest {

  private CallProcOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return (CallProcOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
  }

  @Test
  public void valid() throws Exception {
    CallProcOP callProcOP =
        init("func f() -> real: p(); endfunc proc main(): endproc");
    Assertions.assertEquals(2, callProcOP.getId().getId());
    Assertions.assertTrue(Utility.isListEmpty(callProcOP.getParams()));

    callProcOP = init("func f() -> real, boolean: p(2+4, @a); endfunc proc main(): endproc");
    Assertions.assertEquals(2, callProcOP.getId().getId());
    Assertions.assertEquals(2, callProcOP.getParams().size());
  }

  @Test
  public void invalid() {

    // assign a procedure to a variable
    parser p = ParserUtility.parser(
        "func f() -> real, boolean: a ^= p(2+4, @a); endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
