package test.java.semantic;

import main.esercitazione5.semantic.exceptions.NumIdsNumConstsDiffSemanticException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VarDeclOPTest {

  private void init(String sourceStr) throws Exception {
    SemanticUtility.astSemantic(sourceStr);
  }

  @Test
  public void invalid() {

    // number of IDs different from number of Constants
    Assertions.assertThrows(NumIdsNumConstsDiffSemanticException.class,
        () -> init("var a ^= 4.5, 5;\\ proc main(): endproc"));

    Assertions.assertThrows(NumIdsNumConstsDiffSemanticException.class,
        () -> init("var a, b ^= 4.5, 5, 6;\\ proc main(): endproc"));
  }
}
