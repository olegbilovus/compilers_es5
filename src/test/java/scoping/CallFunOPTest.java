package test.java.scoping;

import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.FuncMultReturnValScopeException;
import main.esercitazione5.scope.exceptions.NotAFuncScopeException;
import main.esercitazione5.scope.exceptions.NumArgsExprIncorrectScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CallFunOPTest {

  private ScopeTable init(String sourceStr) throws Exception {
    return ScopingUtility.astScoped(sourceStr).getScopeTable();
  }

  @Test
  public void valid() {

    Assertions.assertDoesNotThrow(
        () -> init(
            "func f() -> real: return 1; endfunc proc main(): var a: real;\\ a ^= f(); endproc"));

    Assertions.assertDoesNotThrow(
        () -> init(
            "func f() -> real, real: return 1, 2; endfunc proc main(): var a, b: real;\\ a, b ^= f(); endproc"));

  }

  @Test
  public void invalid() {

    // f renamed as var in the local scope
    Assertions.assertThrows(NotAFuncScopeException.class,
        () -> init(
            "func f() -> real: return 1; endfunc proc main(): var a, f: real;\\ a ^= f(); endproc"));

    // f(g()) g return multiple values
    Assertions.assertThrows(FuncMultReturnValScopeException.class,
        () -> init(
            "func f(x: real, y: real) -> real: return 1; endfunc func g() -> real, real: return 1, 2; endfunc proc main(): var a: real;\\ a ^= f(g()); endproc"));

    // incorrect number of passed args
    Assertions.assertThrows(NumArgsExprIncorrectScopeException.class,
        () -> init(
            "func f() -> real: return 1; endfunc proc main(): var a: real;\\ a ^= f(1); endproc"));
  }
}
