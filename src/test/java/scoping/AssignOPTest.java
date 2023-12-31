package test.java.scoping;

import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.NumAssignExprIncorrectScopeException;
import main.esercitazione5.scope.exceptions.VariableReadOnlyScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssignOPTest {

  private ScopeTable init(String sourceStr) throws Exception {
    return ScopingUtility.astScoped(sourceStr).getScopeTable();
  }

  @Test public void valid() {
    Assertions.assertDoesNotThrow(() -> init(
        "var a: real;\\ func f() -> real: a ^= 2; return 1; endfunc proc main(): endproc"));

    Assertions.assertDoesNotThrow(() -> init(
        "var a, b: real;\\ func f() -> real: a, b ^= 2+4, 5.5; return 1; endfunc proc main(): endproc"));

    Assertions.assertDoesNotThrow(() -> init(
        "func f(a: real) -> real: if true then var a, b: real;\\ a, b ^= 4.4, 5.5; endif; return 1; endfunc proc main(): endproc"));

    String source = """
        var b, c: real;\\
        func f(a: real) -> real, boolean:
          b, c ^= g();
          return g();
        endfunc
        func g() -> real, boolean:
          return 0, true;
        endfunc
        proc main(): endproc""";
    Assertions.assertDoesNotThrow(() -> ScopingUtility.astScoped(source));
  }

  @Test public void invalid() {

    // assign to a variable read only
    Assertions.assertThrows(VariableReadOnlyScopeException.class, () -> init(
        "func f(a: real) -> real: if true then var b: real;\\ a, b ^= 4.4, 5.5; endif; return 1; endfunc proc main(): endproc"));

    // 2 const to 1 var
    Assertions.assertThrows(NumAssignExprIncorrectScopeException.class, () -> init(
        "var a: real;\\ func f() -> real: a ^= 2, 1; return 1; endfunc proc main(): endproc"));

    // 2 const to 1 var
    Assertions.assertThrows(NumAssignExprIncorrectScopeException.class, () -> init(
        "var a: real;\\ func f() -> real, boolean: a ^= 2+4, 5.5; return 1; endfunc proc main(): endproc"));

    // g returns 2 values, but 1 var is used
    String source = """
        var b, c: real;\\
        func f(a: real) -> real, boolean:
          c ^= g();
          return g();
        endfunc
        func g() -> real, boolean:
          return 0, true;
        endfunc
        proc main(): endproc""";
    Assertions.assertThrows(NumAssignExprIncorrectScopeException.class, () -> init(source));
  }
}
