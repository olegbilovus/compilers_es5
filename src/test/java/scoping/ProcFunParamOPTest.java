package test.java.scoping;

import main.esercitazione5.ast.ParamAccess;
import main.esercitazione5.ast.Type;
import main.esercitazione5.scope.ScopeEntry;
import main.esercitazione5.scope.ScopeKind;
import main.esercitazione5.scope.ScopeTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProcFunParamOPTest {

  private ScopeTable initFun(String sourceStr) throws Exception {
    return ScopingUtility.astScoped(sourceStr).getFunOPList().get(0).getScopeTable();
  }

  private ScopeTable initProc(String sourceStr) throws Exception {
    return ScopingUtility.astScoped(sourceStr).getProcOPList().get(0).getScopeTable();
  }

  @Test
  public void valid() throws Exception {
    ScopeTable scopeTable = initFun(
        "func f(a: integer) -> real, boolean: return 1.2, true; endfunc proc main(): endproc");
    ScopeEntry entry = scopeTable.lookup(2, null);
    Assertions.assertEquals(ScopeKind.VAR, entry.getKind());
    Assertions.assertEquals(Type.INTEGER, entry.getListType1().get(0).type());
    Assertions.assertEquals(ParamAccess.IN, entry.getListType1().get(0).paramAccess());

    scopeTable = initFun(
        "func f(a: string, b: boolean) -> real, boolean: return 1.2, true; endfunc proc main(): endproc");
    entry = scopeTable.lookup(2, null);
    Assertions.assertEquals(ScopeKind.VAR, entry.getKind());
    Assertions.assertEquals(Type.STRING, entry.getListType1().get(0).type());
    Assertions.assertEquals(ParamAccess.IN, entry.getListType1().get(0).paramAccess());
    entry = scopeTable.lookup(3, null);
    Assertions.assertEquals(ScopeKind.VAR, entry.getKind());
    Assertions.assertEquals(Type.BOOLEAN, entry.getListType1().get(0).type());
    Assertions.assertEquals(ParamAccess.IN, entry.getListType1().get(0).paramAccess());

    scopeTable = initProc(
        "proc p(out a: real, b: boolean): endproc proc main(): endproc");
    entry = scopeTable.lookup(2, null);
    Assertions.assertEquals(ScopeKind.VAR, entry.getKind());
    Assertions.assertEquals(Type.REAL, entry.getListType1().get(0).type());
    Assertions.assertEquals(ParamAccess.OUT, entry.getListType1().get(0).paramAccess());
    entry = scopeTable.lookup(3, null);
    Assertions.assertEquals(ScopeKind.VAR, entry.getKind());
    Assertions.assertEquals(Type.BOOLEAN, entry.getListType1().get(0).type());
    Assertions.assertEquals(ParamAccess.INOUT, entry.getListType1().get(0).paramAccess());
  }
}
