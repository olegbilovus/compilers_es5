package test.java.semantic;

import main.esercitazione5.semantic.exceptions.ReturnInProcSemanticException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.java.scoping.ScopingUtility;

public class ProcOPTest {

  private void init(String sourceStr) throws Exception {
    SemanticUtility.astSemantic(sourceStr);
  }

  @Test
  public void valid() {
    Assertions.assertDoesNotThrow(
        () -> init("proc p(): var a: real; \\ endproc proc main(): endproc"));
  }

  @Test
  public void invalid() {

    // using a return statement in a procedure
    Assertions.assertThrows(ReturnInProcSemanticException.class,
        () -> ScopingUtility.astScoped(
            "proc main(): return 1; endproc"));
  }
}
