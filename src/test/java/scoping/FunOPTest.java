package test.java.scoping;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.ParamAccess;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.scope.ScopeEntry;
import main.esercitazione5.scope.ScopeKind;
import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.AlreadyDeclaredScopeException;
import main.esercitazione5.scope.exceptions.NumReturnExprIncorrectScopeException;
import main.esercitazione5.scope.exceptions.UndeclaredScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FunOPTest {

  private ScopeTable init(ProgramOP programOP) {
    return programOP.getFunOPList().get(0).getScopeTable();
  }

  @Test
  public void valid() throws Exception {
    ProgramOP programOP =
        ScopingUtility.astScoped("func f() -> real: return 1.2; endfunc proc main(): endproc");
    ScopeTable global = programOP.getScopeTable();
    ScopeEntry entry = global.lookup(1, null);
    Assertions.assertEquals(ScopeKind.FUN, entry.getKind());
    Assertions.assertTrue(Utility.isListEmpty(entry.getListType1()));
    Assertions.assertEquals(1, entry.getListType2().size());
    Assertions.assertEquals(Type.REAL, entry.getListType2().get(0));
    ScopeTable scopeTable = init(programOP);
    Assertions.assertTrue(scopeTable.getTable().isEmpty());
    Assertions.assertEquals(1, ScopingUtility.numPrevTables(scopeTable));

    programOP = ScopingUtility.astScoped(
        "func f(a: integer, b: real) -> real, boolean: var x ^= 45;\\ return 1, false; endfunc proc main(): endproc");
    global = programOP.getScopeTable();
    entry = global.lookup(1, null);
    Assertions.assertEquals(ScopeKind.FUN, entry.getKind());
    Assertions.assertEquals(2, entry.getListType1().size());
    Assertions.assertEquals(2, entry.getListType2().size());
    Assertions.assertEquals(Type.INTEGER, entry.getListType1().get(0).type());
    Assertions.assertEquals(ParamAccess.IN, entry.getListType1().get(0).paramAccess());
    Assertions.assertEquals(Type.REAL, entry.getListType1().get(1).type());
    Assertions.assertEquals(ParamAccess.IN, entry.getListType1().get(1).paramAccess());
    scopeTable = init(programOP);
    Assertions.assertEquals(3, scopeTable.getTable().size());
    Assertions.assertEquals(1, ScopingUtility.numPrevTables(scopeTable));

    Assertions.assertDoesNotThrow(
        () -> ScopingUtility.astScoped(
            "func f(a: real) -> real, boolean: return 1, false; endfunc proc main(): endproc"));

    Assertions.assertDoesNotThrow(
        () -> ScopingUtility.astScoped(
            "func f(a: real, b: boolean) -> real, boolean: return f(1, true); endfunc proc main(): endproc"));

    String source = """
        func f(a: real) -> real, boolean:
          return g();
        endfunc
        func g() -> real, boolean:
          return 0, true;
        endfunc
        proc main(): endproc""";
    Assertions.assertDoesNotThrow(
        () -> ScopingUtility.astScoped(source));

    String source1 = """
        func f(a: real) -> real, boolean, integer:
          return g(), 3;
        endfunc
        func g() -> real, boolean:
          return 0, true;
        endfunc
        proc main(): endproc""";
    Assertions.assertDoesNotThrow(
        () -> ScopingUtility.astScoped(source1));
  }

  @Test
  public void invalid() {

    // redefine a parameter in the signature
    Assertions.assertThrows(AlreadyDeclaredScopeException.class,
        () -> ScopingUtility.astScoped(
            "func f(a: real, a: boolean) -> real: return 1.2; endfunc proc main(): endproc"));

    // redefine a parameter inside the body
    Assertions.assertThrows(AlreadyDeclaredScopeException.class,
        () -> ScopingUtility.astScoped(
            "func f(a: real) -> real: var a: string;\\ return 1; endfunc proc main(): endproc"));

    // return not existing variable
    Assertions.assertThrows(UndeclaredScopeException.class,
        () -> ScopingUtility.astScoped(
            "func f(a: real) -> real: return c; endfunc proc main(): endproc"));

    // the number of returned expressions is different from the function's signature
    Assertions.assertThrows(NumReturnExprIncorrectScopeException.class,
        () -> ScopingUtility.astScoped(
            "func f(a: real) -> real, boolean: return 1; endfunc proc main(): endproc"));

    // g returns diff number of args than f
    String source = """
        func f(a: real) -> real, boolean:
          return g();
        endfunc
        func g() -> real:
          return 0;
        endfunc
        proc main(): endproc""";
    Assertions.assertThrows(NumReturnExprIncorrectScopeException.class,
        () -> ScopingUtility.astScoped(source));

    // g returns not enough args
    String source1 = """
        func f(a: real) -> real, boolean, integer:
          return g();
        endfunc
        func g() -> real:
          return 0;
        endfunc
        proc main(): endproc""";
    Assertions.assertThrows(NumReturnExprIncorrectScopeException.class,
        () -> ScopingUtility.astScoped(source1));
  }
}
