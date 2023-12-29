package test.java.scoping;

import main.esercitazione5.scope.ScopeTable;
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
        init("func f() -> real: while true do endwhile; endfunc proc main(): endproc");
    Assertions.assertTrue(scopeTable.getTable().isEmpty());
    Assertions.assertEquals(2, ScopingUtility.numPrevTables(scopeTable));

    scopeTable =
        init(
            "func f() -> real: while true do var a ^= 4;\\ endwhile; endfunc proc main(): endproc");
    Assertions.assertEquals(1, scopeTable.getTable().size());
    Assertions.assertEquals(2, ScopingUtility.numPrevTables(scopeTable));
  }
}