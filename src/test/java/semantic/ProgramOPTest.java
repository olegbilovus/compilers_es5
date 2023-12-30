package test.java.semantic;

import main.esercitazione5.semantic.exceptions.MissingMainProcSemanticException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProgramOPTest {


  private void init(String sourceStr) throws Exception {
    SemanticUtility.astSemantic(sourceStr);
  }

  @Test
  public void invalid() {

    // no main
    Assertions.assertThrows(MissingMainProcSemanticException.class, () -> init("var a: real;\\"));
  }
}
