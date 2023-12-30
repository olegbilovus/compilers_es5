package test.java.scoping;

import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.FuncMultReturnValScopeException;
import main.esercitazione5.scope.exceptions.NotAProcScopeException;
import main.esercitazione5.scope.exceptions.NumArgsExprIncorrectScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CallProcOPTest {

  private ScopeTable init(String sourceStr) throws Exception {
    return ScopingUtility.astScoped(sourceStr).getScopeTable();
  }

  @Test
  public void valid() {

    Assertions.assertDoesNotThrow(
        () -> init(
            " proc main(): var a: real;\\ main(); endproc"));


  }

  @Test
  public void invalid() {

    // p renamed as var in the local scope
    Assertions.assertThrows(NotAProcScopeException.class,
        () -> init(
            "proc main(): var a, main: real;\\ main(); endproc"));

    // p(g()) g return multiple values
    Assertions.assertThrows(FuncMultReturnValScopeException.class,
        () -> init(
            "proc p(x: real, y: real): endproc func g() -> real, real: return 1, 2; endfunc proc main(): p(g()); endproc"));

    // incorrect number of passed args
    Assertions.assertThrows(NumArgsExprIncorrectScopeException.class,
        () -> init(
            "proc main(): main(3); endproc"));
  }
}
