package test.java.parser;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.ReadOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ReadOPTest {

  private ReadOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return (ReadOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
  }

  @Test
  public void valid() throws Exception {
    ReadOP readOP = init("func f() -> real: <--; endfunc proc main(): endproc");
    Assertions.assertTrue(Utility.isListEmpty(readOP.getExprList()));

    readOP = init("func f() -> real: <-- \"Input:\" $(a); endfunc proc main(): endproc");
    Assertions.assertEquals(2, readOP.getExprList().size());

    readOP = init(
        "func f() -> real: <-- \"Input\" $(a) \"Done\" + \"Thanks\"; endfunc proc main(): endproc");
    Assertions.assertEquals(3, readOP.getExprList().size());
  }

  @Test
  public void invalid() {

    // multiple IDs in one $()
    parser p = ParserUtility.parser(
        "func f() -> real: --> \"hello\" + 4 $(a, b); endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // Expr in a $()
    p = ParserUtility.parser(
        "func f() -> real: --> \"hello\" + 4 $(a+b); endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
