package test.java.parser;

import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.AssignOP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssignOPTest {

  private AssignOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
  }

  @Test
  public void valid() throws Exception {
    AssignOP assignOP = init("func f() -> real: a ^= 2; endfunc proc main(): endproc");
    Assertions.assertEquals(1, assignOP.getIdNodeList().size());
    Assertions.assertEquals(1, assignOP.getExprList().size());

    assignOP = init("func f() -> real, boolean: a, b ^= 2+4, 5.5; endfunc proc main(): endproc");
    Assertions.assertEquals(2, assignOP.getIdNodeList().size());
    Assertions.assertEquals(2, assignOP.getExprList().size());

    assignOP = init("func f() -> real, boolean: a, b ^= g(); endfunc proc main(): endproc");
    Assertions.assertEquals(2, assignOP.getIdNodeList().size());
    Assertions.assertEquals(1, assignOP.getExprList().size());
  }
}
