package test.java.scoping;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.ParamAccess;
import main.esercitazione5.ast.Type;
import main.esercitazione5.scope.ScopeEntry;
import main.esercitazione5.scope.ScopeKind;
import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.ScopeType;
import main.esercitazione5.scope.exceptions.AlreadyDeclaredScopeException;
import main.esercitazione5.semantic.exceptions.NumIdsNumConstsDiffSemanticException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VarDeclOPTest {

  private ScopeTable init(String sourceStr) throws Exception {
    return ScopingUtility.astScoped(sourceStr).getScopeTable();
  }

  @Test
  public void valid() throws Exception {
    ScopeTable scopeTable = init("var a: boolean;\\ proc main(): endproc");
    Assertions.assertEquals(2, scopeTable.getTable().size());
    ScopeEntry entry = scopeTable.lookup(1, null);
    Assertions.assertEquals(ScopeKind.VAR, entry.getKind());
    Assertions.assertEquals(1, entry.getListType1().size());
    ScopeType scopeType = entry.getListType1().get(0);
    Assertions.assertEquals(Type.BOOLEAN, scopeType.type());
    Assertions.assertEquals(ParamAccess.IN, scopeType.paramAccess());
    Assertions.assertTrue(Utility.isListEmpty(entry.getListType2()));

    scopeTable = init("var a ^= 4.5;\\ proc main(): endproc");
    Assertions.assertEquals(2, scopeTable.getTable().size());
    entry = scopeTable.lookup(1, null);
    Assertions.assertEquals(ScopeKind.VAR, entry.getKind());
    Assertions.assertEquals(1, entry.getListType1().size());
    scopeType = entry.getListType1().get(0);
    Assertions.assertEquals(Type.REAL, scopeType.type());
    Assertions.assertEquals(ParamAccess.IN, scopeType.paramAccess());
    Assertions.assertTrue(Utility.isListEmpty(entry.getListType2()));

    scopeTable = init("var a, b ^= true, \"hello\";\\ proc main(): endproc");
    Assertions.assertEquals(3, scopeTable.getTable().size());
    entry = scopeTable.lookup(2, null);
    Assertions.assertEquals(ScopeKind.VAR, entry.getKind());
    Assertions.assertEquals(1, entry.getListType1().size());
    scopeType = entry.getListType1().get(0);
    Assertions.assertEquals(Type.STRING, scopeType.type());
    Assertions.assertEquals(ParamAccess.IN, scopeType.paramAccess());
    Assertions.assertTrue(Utility.isListEmpty(entry.getListType2()));

    // first use and later init same scope
    Assertions.assertDoesNotThrow(() -> init("proc main(): a ^= 4; var a: integer;\\ endproc"));
  }

  @Test
  public void invalid() {

    // number of IDs different from number of Constants
    Assertions.assertThrows(NumIdsNumConstsDiffSemanticException.class,
        () -> init("var a ^= 4.5, 5;\\ proc main(): endproc"));

    // redefine the variable
    Assertions.assertThrows(AlreadyDeclaredScopeException.class,
        () -> init("var a ^= 4; a: boolean;\\ proc main(): endproc"));
  }
}
