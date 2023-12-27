package test.java.scoping;

import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.exceptions.AlreadyDeclaredScopeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProgramOPTest {


  private ScopeTable init(String sourceStr) throws Exception {
    return ScopingUtility.astScoped(sourceStr).getScopeTable();
  }

  @Test
  public void varDeclOPListValid() throws Exception {
    ScopeTable scopeTable = init("proc main(): endproc");
    Assertions.assertEquals(1, scopeTable.getTable().size());

    scopeTable = init("var a: integer;\\ proc main(): endproc");
    Assertions.assertEquals(2, scopeTable.getTable().size());

    scopeTable = init("var a: integer; b: boolean;\\ proc main(): endproc");
    Assertions.assertEquals(3, scopeTable.getTable().size());

    scopeTable = init("var a, b: integer;\\ proc main(): endproc");
    Assertions.assertEquals(3, scopeTable.getTable().size());

    scopeTable = init("var a, b: integer;\\ proc main(): endproc var c ^= 2;\\");
    Assertions.assertEquals(4, scopeTable.getTable().size());

    scopeTable = init("var a, b: integer;\\ proc main(): var c: boolean;\\ endproc var c ^= 2;\\");
    Assertions.assertEquals(4, scopeTable.getTable().size());
  }

  @Test
  public void procOPListValid() throws Exception {
    ScopeTable scopeTable = init("proc main(): endproc proc main2(): endproc");
    Assertions.assertEquals(2, scopeTable.getTable().size());
  }

  @Test
  public void funOPListValid() throws Exception {
    ScopeTable scopeTable = init("proc main(): endproc func f() -> real: return 1; endfunc");
    Assertions.assertEquals(2, scopeTable.getTable().size());

    scopeTable = init(
        "func f() -> real: return 1; endfunc proc main(): endproc func main2(a: integer) -> integer: return 0; endfunc");
    Assertions.assertEquals(3, scopeTable.getTable().size());
  }

  @Test
  public void valid() throws Exception {

    ScopeTable scopeTable =
        init("var a: boolean;\\ proc main(): endproc func f() -> real: return 1; endfunc");
    Assertions.assertEquals(3, scopeTable.getTable().size());

    scopeTable = init(
        """
            func f() -> real:
              return 1;
            endfunc
                        
            var a, b :string;\\
                        
            proc main(): endproc
                        
            func main2(a: integer) -> integer:
              return 0;
            endfunc
                        
            var abc ^= 32.23;\\
                        
            proc p(out a: integer):
              a ^= 6;
            endproc
            """);
    Assertions.assertEquals(7, scopeTable.getTable().size());
  }

  public void invalid() {

    String source =
        """
            func f() -> real:
              return 1;
            endfunc
                        
            var a, b :string;\\
                        
            proc main(): endproc
                        
            func main2(a: integer) -> integer:
              return 0;
            endfunc
                        
            var a ^= 32.78;\\
                        
            proc p(out a: integer):
              a ^= 6;
            endproc
            """;
    Assertions.assertThrows(AlreadyDeclaredScopeException.class, () -> init(source));

    String source1 = "fun f() -> real: endfun proc main() endproc var f: boolean;\n";
    Assertions.assertThrows(AlreadyDeclaredScopeException.class, () -> init(source1));
  }
}
