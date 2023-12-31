package test.java.scoping;

import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.CanNotRefAnExprScopeException;
import main.esercitazione5.scope.exceptions.FuncMultReturnValScopeException;
import main.esercitazione5.scope.exceptions.MissingRefSymbolScopeException;
import main.esercitazione5.scope.exceptions.NotAProcScopeException;
import main.esercitazione5.scope.exceptions.NumArgsExprIncorrectScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CallProcOPTest {

  private ScopeTable init(String sourceStr) throws Exception {
    return ScopingUtility.astScoped(sourceStr).getScopeTable();
  }

  @Test public void valid() {

    Assertions.assertDoesNotThrow(() -> init(" proc main(): var a: real;\\ main(); endproc"));

    Assertions.assertDoesNotThrow(
        () -> init(" proc main(out b: string): var a: real;\\ main(@a); endproc"));


  }

  @Test public void invalid() {

    // p renamed as var in the local scope
    Assertions.assertThrows(NotAProcScopeException.class,
        () -> init("proc main(): var a, main: real;\\ main(); endproc"));

    // p(g()) g return multiple values
    Assertions.assertThrows(FuncMultReturnValScopeException.class, () -> init(
        "proc p(x: real, y: real): endproc func g() -> real, real: return 1, 2; endfunc proc main(): p(g()); endproc"));

    // incorrect number of passed args
    Assertions.assertThrows(NumArgsExprIncorrectScopeException.class,
        () -> init("proc main(): main(3); endproc"));

    // passing an Expr to a REF parameter
    Assertions.assertThrows(CanNotRefAnExprScopeException.class,
        () -> init("proc main(out a: real): main(3+4); endproc"));

    // missing @ symbol in a REF arg
    Assertions.assertThrows(MissingRefSymbolScopeException.class,
        () -> init("proc main(out a: real): var b: real;\\ main(b); endproc"));
  }
}
