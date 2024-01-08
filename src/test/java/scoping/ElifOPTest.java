package test.java.scoping;

import main.esercitazione5.ast.nodes.stat.IfOP;
import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.FuncMultReturnValScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ElifOPTest {

  private ScopeTable init(String sourceStr) throws Exception {
    return ((IfOP) ScopingUtility.astScoped(sourceStr).getFunOPList().get(0).getBodyOP()
        .getStatList()
        .get(0)).getElifOPList().get(0).getScopeTable();
  }

  @Test
  public void valid() throws Exception {
    ScopeTable scopeTable =
        init(
            "func f() -> real: if true then elseif false then endif; return 1.1; endfunc proc main(): endproc");
    Assertions.assertTrue(scopeTable.getTable().isEmpty());
    // expecting 2 prev tables because Elif does NOT have to include the If table in its active tables
    Assertions.assertEquals(2, ScopingUtility.numPrevTables(scopeTable));

    scopeTable = init(
        "func f() -> real: if true then elseif false then var a ^= true;\\ endif; return 1; endfunc proc main(): endproc");
    Assertions.assertEquals(1, scopeTable.getTable().size());
    Assertions.assertEquals(2, ScopingUtility.numPrevTables(scopeTable));
  }

  @Test
  public void invalid(){
    // f returns multiple values in a elseif condition
    Assertions.assertThrows(FuncMultReturnValScopeException.class,
        () -> ScopingUtility.astScoped(
            "func f() -> boolean, real: return true, 1.2; endfunc proc main(): if true then elseif f() then endif; endproc"));
  }
}



