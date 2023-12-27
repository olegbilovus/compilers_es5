package test.java.parser;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.IfOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IfOPTest {

  private IfOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return (IfOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
  }

  @Test
  public void valid() throws Exception {
    IfOP ifOP = init("func f() -> real: if true then endif; endfunc proc main(): endproc");
    Assertions.assertNotNull(ifOP.getCondition());
    Assertions.assertNull(ifOP.getBody());
    Assertions.assertTrue(Utility.isListEmpty(ifOP.getElifOPList()));
    Assertions.assertNull(ifOP.getElse());

    ifOP = init(
        "func f() -> real: if true then a ^= 4; else a ^= 5; endif; endfunc proc main(): endproc");
    Assertions.assertNotNull(ifOP.getCondition());
    Assertions.assertNotNull(ifOP.getBody());
    Assertions.assertTrue(Utility.isListEmpty(ifOP.getElifOPList()));
    Assertions.assertNotNull(ifOP.getElse());

    ifOP = init(
        "func f() -> real: if a > 5 then a ^= 4; elseif a < 0 then a^= 1; elseif false then else endif; endfunc proc main(): endproc");
    Assertions.assertNotNull(ifOP.getCondition());
    Assertions.assertNotNull(ifOP.getBody());
    Assertions.assertEquals(2, ifOP.getElifOPList().size());
    Assertions.assertNull(ifOP.getElse());
  }

  @Test
  public void invalid() {

    // missing condition
    parser p =
        ParserUtility.parser("func f() -> real: if then endif; endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // missing THEN
    p = ParserUtility.parser("func f() -> real: if true endif; endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // missing ENDIF
    p = ParserUtility.parser("func f() -> real: if true then; endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
