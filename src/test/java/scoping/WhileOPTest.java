package test.java.scoping;

import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.FuncMultReturnValScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WhileOPTest {

  private ScopeTable init(String sourceStr) throws Exception {
    return ScopingUtility.astScoped(sourceStr).getFunOPList().get(0).getBodyOP().getStatList()
        .get(0).getScopeTable();
  }

  @Test
  public void valid() throws Exception {
    ScopeTable scopeTable =
        init("func f() -> real: while true do endwhile; return 1.1; endfunc proc main(): endproc");
    Assertions.assertTrue(scopeTable.getTable().isEmpty());
    Assertions.assertEquals(2, ScopingUtility.numPrevTables(scopeTable));

    scopeTable =
        init(
            "func f() -> real: while true do var a ^= 4;\\ endwhile; return 1; endfunc proc main(): endproc");
    Assertions.assertEquals(1, scopeTable.getTable().size());
    Assertions.assertEquals(2, ScopingUtility.numPrevTables(scopeTable));
  }

  @Test
  public void invalid(){
    // f returns multiple values in a while condition
    Assertions.assertThrows(FuncMultReturnValScopeException.class,
        () -> ScopingUtility.astScoped(
            "func f() -> boolean, real: return true, 1.2; endfunc proc main(): while f() do endwhile; endproc"));
  }
}
