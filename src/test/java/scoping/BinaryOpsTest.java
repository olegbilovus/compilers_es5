package test.java.scoping;

import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.FuncMultReturnValScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BinaryOpsTest {

  private ScopeTable init(String sourceStr) throws Exception {
    return ScopingUtility.astScoped(sourceStr).getScopeTable();
  }

  @Test public void valid() {

    Assertions.assertDoesNotThrow(() -> init(
        "func f() -> real: return 1; endfunc proc main(): var a: real;\\ a ^= f() + f(); endproc"));

    Assertions.assertDoesNotThrow(() -> init(
        "func f() -> real: return 1; endfunc proc main(): var a: real;\\ a ^= -f(); endproc"));

    Assertions.assertDoesNotThrow(() -> init(
        "func f() -> real: return 1; endfunc proc main(): var a: real;\\ a ^= -f() + f(); endproc"));

    Assertions.assertDoesNotThrow(() -> init(
        "func f() -> real: return 1; endfunc proc main(): var a: real;\\ a ^= f() && f(); endproc"));
  }

  @Test public void invalid() {

    // f return multiple values
    Assertions.assertThrows(FuncMultReturnValScopeException.class, () -> init(
        "func f() -> real, real: return 1, 2; endfunc proc main(): var a: real;\\ a ^= f() + f(); endproc"));

    // f return multiple values
    Assertions.assertThrows(FuncMultReturnValScopeException.class, () -> init(
        "func f() -> real, real: return 1, 2; endfunc proc main(): var a: real;\\ a ^= -f(); endproc"));

    // f return multiple values
    Assertions.assertThrows(FuncMultReturnValScopeException.class, () -> init(
        "func f() -> real, real: return 1, 2; endfunc proc main(): var a: real;\\ a ^= f() && f(); endproc"));

  }
}
