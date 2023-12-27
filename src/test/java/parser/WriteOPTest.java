package test.java.parser;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.stat.WriteOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WriteOPTest {

  private WriteOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return (WriteOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
  }

  @Test
  public void valid() throws Exception {
    WriteOP writeOP = init("func f() -> real: -->; endfunc proc main(): endproc");
    Assertions.assertTrue(Utility.isListEmpty(writeOP.getExprList()));
    Assertions.assertFalse(writeOP.hasNewline());

    writeOP = init("func f() -> real: -->!; endfunc proc main(): endproc");
    Assertions.assertTrue(Utility.isListEmpty(writeOP.getExprList()));
    Assertions.assertTrue(writeOP.hasNewline());

    writeOP = init("func f() -> real: --> \"hello\" $(a); endfunc proc main(): endproc");
    Assertions.assertEquals(2, writeOP.getExprList().size());
    Assertions.assertFalse(writeOP.hasNewline());

    writeOP = init(
        "func f() -> real: --> \"hello\" + \"world\" + \"!!\" $(a) \"end\"; endfunc proc main(): endproc");
    Assertions.assertEquals(3, writeOP.getExprList().size());
    Assertions.assertFalse(writeOP.hasNewline());
  }

  @Test
  public void invalid() {

    // concat a String with a number
    parser p = ParserUtility.parser(
        "func f() -> real: --> \"hello\" + 4 $(a); endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
