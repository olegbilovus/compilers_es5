package test.java.parser;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.FunOP;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FunOPTest {

  private FunOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return programOP.getFunOPList().get(0);
  }

  @Test
  public void valid() throws Exception {
    FunOP funOP = init("func f() -> real: return 1.2; endfunc proc main(): endproc");
    Assertions.assertEquals(1, funOP.getId().getId());
    Assertions.assertTrue(Utility.isListEmpty(funOP.getProcFunParamOPList()));
    Assertions.assertEquals(1, funOP.getReturnTypes().size());
    Assertions.assertEquals(Type.REAL, funOP.getReturnTypes().get(0));
    Assertions.assertNotNull(funOP.getBodyOP());

    funOP = init(
        "func f(a: integer, b: real) -> real, boolean: return 1.2; endfunc proc main(): endproc");
    Assertions.assertEquals(1, funOP.getId().getId());
    Assertions.assertEquals(2, funOP.getProcFunParamOPList().size());
    Assertions.assertEquals(2, funOP.getReturnTypes().size());
    Assertions.assertEquals(Type.REAL, funOP.getReturnTypes().get(0));
    Assertions.assertEquals(Type.BOOLEAN, funOP.getReturnTypes().get(1));
    Assertions.assertNotNull(funOP.getBodyOP());
  }

  @Test
  public void invalid() {

    // missing return TYPE in the signature
    parser p = ParserUtility.parser("func f() -> : return 1.2; endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // missing Body
    p = ParserUtility.parser("func f() -> real: endfunc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // missing endfunc
    p = ParserUtility.parser("func f() -> real: endproc proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
