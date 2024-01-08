package test.java.scoping;

import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.FuncMultReturnValScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IfOPTest {

  private ScopeTable init(String sourceStr) throws Exception {
    return ScopingUtility.astScoped(sourceStr).getFunOPList().get(0).getBodyOP().getStatList()
        .get(0).getScopeTable();
  }

  @Test
  public void valid() throws Exception {
    ScopeTable scopeTable =
        init("func f() -> real: if true then endif; return 1; endfunc proc main(): endproc");
    Assertions.assertTrue(scopeTable.getTable().isEmpty());
    Assertions.assertEquals(2, ScopingUtility.numPrevTables(scopeTable));

    scopeTable = init(
        "func f() -> real: if true then return 4; else return 5; endif; return 1; endfunc proc main(): endproc");
    Assertions.assertTrue(scopeTable.getTable().isEmpty());
    Assertions.assertEquals(2, ScopingUtility.numPrevTables(scopeTable));

    scopeTable = init(
        "func f() -> real: if true then var a ^= 4;\\ endif; return 1; endfunc proc main(): endproc");
    Assertions.assertEquals(1, scopeTable.getTable().size());
    Assertions.assertEquals(2, ScopingUtility.numPrevTables(scopeTable));

  }

  @Test
  public void invalid() {
    // f returns multiple values in a if condition
    Assertions.assertThrows(FuncMultReturnValScopeException.class,
        () -> ScopingUtility.astScoped(
            "func f() -> boolean, real: return true, 1.2; endfunc proc main(): if f() then endif; endproc"));
  }
}
