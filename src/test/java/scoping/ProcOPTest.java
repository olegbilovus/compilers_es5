package test.java.scoping;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.ParamAccess;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.scope.ScopeEntry;
import main.esercitazione5.scope.ScopeKind;
import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.AlreadyDeclaredScopeException;
import main.esercitazione5.scope.exceptions.UndeclaredScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProcOPTest {

  private ScopeTable init(ProgramOP programOP) {
    return programOP.getProcOPList().get(0).getScopeTable();
  }

  @Test
  public void valid() throws Exception {
    ProgramOP programOP =
        ScopingUtility.astScoped("proc p(): endproc proc main(): endproc");
    ScopeTable global = programOP.getScopeTable();
    ScopeEntry entry = global.lookup(1, null);
    Assertions.assertEquals(ScopeKind.PROC, entry.getKind());
    Assertions.assertTrue(Utility.isListEmpty(entry.getListType1()));
    Assertions.assertTrue(Utility.isListEmpty(entry.getListType2()));
    ScopeTable scopeTable = init(programOP);
    Assertions.assertTrue(scopeTable.getTable().isEmpty());
    Assertions.assertEquals(1, ScopingUtility.numPrevTables(scopeTable));

    programOP = ScopingUtility.astScoped(
        "proc p(a: integer, out b: real): var x ^= 45;\\ endproc proc main(): endproc");
    global = programOP.getScopeTable();
    entry = global.lookup(1, null);
    Assertions.assertEquals(ScopeKind.PROC, entry.getKind());
    Assertions.assertTrue(Utility.isListEmpty(entry.getListType2()));
    Assertions.assertEquals(2, entry.getListType1().size());
    Assertions.assertEquals(Type.INTEGER, entry.getListType1().get(0).type());
    Assertions.assertEquals(ParamAccess.INOUT, entry.getListType1().get(0).paramAccess());
    Assertions.assertEquals(Type.REAL, entry.getListType1().get(1).type());
    Assertions.assertEquals(ParamAccess.OUT, entry.getListType1().get(1).paramAccess());
    scopeTable = init(programOP);
    Assertions.assertEquals(3, scopeTable.getTable().size());
    Assertions.assertEquals(1, ScopingUtility.numPrevTables(scopeTable));
  }

  @Test
  public void invalid() {

    // redefine a parameter in the signature
    Assertions.assertThrows(AlreadyDeclaredScopeException.class,
        () -> ScopingUtility.astScoped(
            "proc p(a: real, out a: boolean): endproc proc main(): endproc"));

    // redefine a parameter inside the body
    Assertions.assertThrows(AlreadyDeclaredScopeException.class,
        () -> ScopingUtility.astScoped(
            "proc p(a: real): var a: string;\\ endproc proc main(): endproc"));

    // use not existing variable
    Assertions.assertThrows(UndeclaredScopeException.class,
        () -> ScopingUtility.astScoped(
            "proc p(a: real): c ^= 4; endproc proc main(): endproc"));
  }
}
