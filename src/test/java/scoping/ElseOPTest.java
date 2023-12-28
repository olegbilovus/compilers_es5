package test.java.scoping;

import main.esercitazione5.ast.nodes.stat.IfOP;
import main.esercitazione5.scope.ScopeTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ElseOPTest {

  private ScopeTable init(String sourceStr) throws Exception {
    return ((IfOP) ScopingUtility.astScoped(sourceStr).getFunOPList().get(0).getBodyOP()
        .getStatList()
        .get(0)).getElse().getScopeTable();
  }

  @Test
  public void valid() throws Exception {
    ScopeTable scopeTable =
        init("func f() -> real: if true then else return 4; endif; endfunc proc main(): endproc");
    Assertions.assertTrue(scopeTable.getTable().isEmpty());
    // expecting 2 prev tables because Else does NOT have to include the If table in its active tables
    Assertions.assertEquals(2, ScopingUtility.numPrevTables(scopeTable));

    scopeTable = init(
        "func f() -> real: if true then else var a ^= true;\\ endif; endfunc proc main(): endproc");
    Assertions.assertEquals(1, scopeTable.getTable().size());
    Assertions.assertEquals(2, ScopingUtility.numPrevTables(scopeTable));
  }
}
