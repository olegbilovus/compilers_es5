package test.java.typecheck;

import java.util.List;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.expr.CallFunOP;
import main.esercitazione5.ast.nodes.stat.AssignOP;
import main.esercitazione5.ast.nodes.stat.Stat;
import main.esercitazione5.typecheck.exceptions.TypeArgsExprIncorrectTypeCheckException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParamsTest {

  private Stat init(String sourceStr) throws Exception {
    return (TypeCheckUtility.ast(sourceStr).getProcOPList().get(0).getBodyOP().getStatList()
        .get(0));
  }

  @Test public void valid() throws Exception {

    Assertions.assertDoesNotThrow(() -> init("proc main(a: real): main(a); endproc"));

    Assertions.assertDoesNotThrow(() -> init("proc main(a: real): main(4.5); endproc"));

    Assertions.assertDoesNotThrow(() -> init("proc main(a: real): main(a + 5); endproc"));

    CallFunOP callFunOP = (CallFunOP) ((AssignOP) init(
        "func f(a: real) -> real: return 1.2; endfunc proc main(b: real): b^= f(b); endproc")).getExprList()
        .get(0);
    Assertions.assertEquals(List.of(Type.REAL), callFunOP.getTypeList());

    callFunOP = (CallFunOP) ((AssignOP) init(
        "func f(a: real, b: boolean) -> real, boolean: return 1.2, false; endfunc proc main(b: real, a: boolean): b, a ^= f(1.0, true); endproc")).getExprList()
        .get(0);
    Assertions.assertEquals(List.of(Type.REAL, Type.BOOLEAN), callFunOP.getTypeList());

    // integer to a real
    Assertions.assertDoesNotThrow(() -> init(
        "func f(a: real) -> real: return 1.2; endfunc proc main(b: real): b^= f(1); endproc"));


  }

  @Test public void invalid() {
    // real, but expected integer
    Assertions.assertThrows(TypeArgsExprIncorrectTypeCheckException.class,
        () -> init("proc main(a: integer): main(a + 6.5); endproc"));

    // boolean, but expected integer
    Assertions.assertThrows(TypeArgsExprIncorrectTypeCheckException.class,
        () -> init("proc main(a: integer): main(true || true); endproc"));

    // boolean, but expected real
    Assertions.assertThrows(TypeArgsExprIncorrectTypeCheckException.class, () -> init(
        "func f(a: real) -> real: return 1.2; endfunc proc main(b: real): b^= f(true); endproc"));

    // out integer, but expected out real
    Assertions.assertThrows(TypeArgsExprIncorrectTypeCheckException.class,
        () -> init("proc main(out a: real): var i ^= 1;\\ main(@i); endproc"));


  }
}
