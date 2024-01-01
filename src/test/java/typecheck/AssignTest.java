package test.java.typecheck;

import main.esercitazione5.typecheck.exceptions.TypeArgsExprIncorrectTypeCheckException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AssignTest {

  private void init(String sourceStr) throws Exception {
    TypeCheckUtility.ast(sourceStr);
  }

  @Test public void valid() {

    Assertions.assertDoesNotThrow(() -> init("proc main(a: real): a ^= a; endproc"));

    Assertions.assertDoesNotThrow(() -> init("proc main(a: real): a ^= a + 1; endproc"));

    Assertions.assertDoesNotThrow(
        () -> init("proc main(a: real): var b: real;\\ a, b ^= b, a; endproc"));

    Assertions.assertDoesNotThrow(() -> init(
        "func f(a: real) -> real: return 1.2; endfunc proc main(b: real): b^= f(b); endproc"));


  }

  @Test public void invalid() {
    // real, but expected integer
    Assertions.assertThrows(TypeArgsExprIncorrectTypeCheckException.class,
        () -> init("proc main(a: integer): a ^= 4.0; endproc"));

    // boolean, but expected integer
    Assertions.assertThrows(TypeArgsExprIncorrectTypeCheckException.class,
        () -> init("proc main(a: integer): a ^= true || 1 > 2; endproc"));

    // boolean, but expected real
    Assertions.assertThrows(TypeArgsExprIncorrectTypeCheckException.class, () -> init(
        "func f(a: real) -> boolean: return a > 2; endfunc proc main(b: real): b^= f(b); endproc"));

    // integer, but expected real
    Assertions.assertThrows(TypeArgsExprIncorrectTypeCheckException.class, () -> init(
        "func f(a: real) -> integer: return 1; endfunc proc main(b: real): b^= f(b); endproc"));


  }
}
