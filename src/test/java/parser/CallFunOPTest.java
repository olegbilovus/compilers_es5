package test.java.parser;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.expr.CallFunOP;
import main.esercitazione5.ast.nodes.stat.AssignOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CallFunOPTest {

  private CallFunOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return (CallFunOP) ((AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList()
        .get(0)).getExprList().get(0);
  }

  @Test
  public void valid() throws Exception {

    CallFunOP callFunOP = init("func f() -> real: a ^= g(); endfunc proc main(): endproc");
    Assertions.assertEquals(3, callFunOP.getId().getId());
    Assertions.assertTrue(Utility.isListEmpty(callFunOP.getExprList()));

    callFunOP = init("func f() -> real: a ^= g(a, 2+4); endfunc proc main(): endproc");
    Assertions.assertEquals(3, callFunOP.getId().getId());
    Assertions.assertEquals(2, callFunOP.getExprList().size());
  }

  @Test
  public void invalid() {

    // use REF in a Function call
    parser p =
        ParserUtility.parser("func f() -> real: a ^= g(@a, 2+4); endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
