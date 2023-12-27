package test.java.parser;

import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.ReturnOP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReturnOPTest {

  private ReturnOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return (ReturnOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
  }

  @Test
  public void valid() throws Exception {
    ReturnOP returnOP = init("func f() -> real: return 2; endfunc proc main(): endproc");
    Assertions.assertEquals(1, returnOP.getExprList().size());

    returnOP = init("func f() -> real, boolean: return 2+4+6, 5.5; endfunc proc main(): endproc");
    Assertions.assertEquals(2, returnOP.getExprList().size());
  }
}
