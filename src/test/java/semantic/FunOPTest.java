package test.java.semantic;

import main.esercitazione5.semantic.exceptions.MissingReturnInFuncSemanticException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.java.scoping.ScopingUtility;

public class FunOPTest {

  private void init(String sourceStr) throws Exception {
    SemanticUtility.astSemantic(sourceStr);
  }

  @Test
  public void valid() {
    Assertions.assertDoesNotThrow(
        () -> init("func f() -> integer: return 1; endfunc proc main(): endproc"));
  }

  @Test
  public void invalid() {

    // using a return statement in a procedure
    Assertions.assertThrows(MissingReturnInFuncSemanticException.class,
        () -> ScopingUtility.astScoped(
            "func f() -> integer: var a: real;\\ endfunc proc main(): endproc"));
  }
}
