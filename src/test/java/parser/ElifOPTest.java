package test.java.parser;

import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.ElifOP;
import main.esercitazione5.ast.nodes.stat.IfOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ElifOPTest {

  private ElifOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return ((IfOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0)).getElifOPList()
        .get(0);
  }

  @Test
  public void valid() throws Exception {
    ElifOP elifOP = init(
        "func f() -> real: if true then elseif a < 0 then a^= 1; endif; endfunc proc main(): endproc");
    Assertions.assertNotNull(elifOP.getCondition());
    Assertions.assertNotNull(elifOP.getBody());

    elifOP = init(
        "func f() -> real: if true then elseif a < 0 then endif; endfunc proc main(): endproc");
    Assertions.assertNotNull(elifOP.getCondition());
    Assertions.assertNull(elifOP.getBody());
  }

  @Test
  public void invalid() {

    // missing condition
    parser p = ParserUtility.parser(
        "func f() -> real: if true then elseif then endif; endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // missing THEN
    p = ParserUtility.parser(
        "func f() -> real: if true then elseif true endif; endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
