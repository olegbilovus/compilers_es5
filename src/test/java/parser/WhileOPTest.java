package test.java.parser;

import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.WhileOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WhileOPTest {

  private WhileOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return (WhileOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
  }

  @Test
  public void valid() throws Exception {
    WhileOP whileOP =
        init("func f() -> real: while true do endwhile; endfunc proc main(): endproc");
    Assertions.assertNotNull(whileOP.getCondition());
    Assertions.assertNull(whileOP.getBody());

    whileOP =
        init("func f() -> real: while true do a ^= f(); endwhile; endfunc proc main(): endproc");
    Assertions.assertNotNull(whileOP.getCondition());
    Assertions.assertNotNull(whileOP.getBody());
  }

  @Test
  public void invalid() {

    // missing condition
    parser p =
        ParserUtility.parser("func f() -> real: while do endwhile; endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // missing DO
    p = ParserUtility.parser("func f() -> real: while true endwhile; endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // missing ENDIF
    p = ParserUtility.parser("func f() -> real: while true do ; endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
