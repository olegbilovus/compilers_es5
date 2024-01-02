package test.java.scoping;

import main.esercitazione5.StringTable;
import main.esercitazione5.scope.ScopeEntry;
import main.esercitazione5.scope.ScopeKind;
import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.AlreadyDeclaredScopeException;
import main.esercitazione5.scope.exceptions.UndeclaredScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScopeTableTest {


  @Test public void valid() {
    StringTable st = new StringTable();
    st.put("test");
    ScopeTable scopeTable = new ScopeTable(null);
    Assertions.assertEquals(0, scopeTable.getTable().size());
    int id = 1;
    ScopeEntry scopeEntry = new ScopeEntry(ScopeKind.VAR, null, null, 0);
    scopeTable.add(id, scopeEntry, st);
    Assertions.assertTrue(scopeTable.getTable().containsKey(id));
    Assertions.assertDoesNotThrow(() -> scopeTable.lookup(id, st));

    ScopeTable scopeTable2 = new ScopeTable(scopeTable);
    Assertions.assertDoesNotThrow(() -> scopeTable2.lookup(id, st));
    Assertions.assertDoesNotThrow(() -> scopeTable2.add(id, scopeEntry, st));

  }

  @Test public void invalid() {

    StringTable st = new StringTable();
    st.put("test");
    ScopeTable scopeTable = new ScopeTable(null);
    int id = 1;
    ScopeEntry scopeEntry = new ScopeEntry(ScopeKind.VAR, null, null, 0);
    scopeTable.add(id, scopeEntry, st);
    // adding same id to the same table
    Assertions.assertThrows(AlreadyDeclaredScopeException.class,
        () -> scopeTable.add(id, scopeEntry, st));
    // lookup for a not existing id
    Assertions.assertThrows(UndeclaredScopeException.class, () -> scopeTable.lookup(id + 1, st));

  }
}
