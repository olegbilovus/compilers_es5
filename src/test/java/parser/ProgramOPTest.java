package test.java.parser;

import main.esercitazione5.Utility;
import main.esercitazione5.ast.nodes.ProgramOP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProgramOPTest {


  @Test
  public void varDeclOPListValid() throws Exception {
    ProgramOP programOP = ParserUtility.ast("proc main(): endproc");
    Assertions.assertTrue(Utility.isListEmpty(programOP.getVarDeclOPList()));

    programOP = ParserUtility.ast("var a: integer;\\ proc main(): endproc");
    Assertions.assertEquals(1, programOP.getVarDeclOPList().size());

    programOP = ParserUtility.ast("var a: integer; b: boolean;\\ proc main(): endproc");
    Assertions.assertEquals(2, programOP.getVarDeclOPList().size());

    programOP = ParserUtility.ast("var a, b: integer;\\ proc main(): endproc");
    Assertions.assertEquals(1, programOP.getVarDeclOPList().size());

    programOP = ParserUtility.ast("var a, b: integer;\\ proc main(): endproc var c ^= 2;\\");
    Assertions.assertEquals(2, programOP.getVarDeclOPList().size());

    programOP = ParserUtility.ast(
        "var a, b: integer;\\ proc main(): var c: boolean;\\ endproc var c ^= 2;\\");
    Assertions.assertEquals(2, programOP.getVarDeclOPList().size());
  }

  @Test
  public void procOPListValid() throws Exception {
    ProgramOP programOP = ParserUtility.ast("proc main(): endproc");
    Assertions.assertEquals(1, programOP.getProcOPList().size());

    programOP = ParserUtility.ast("proc main(): endproc proc main2(): endproc");
    Assertions.assertEquals(2, programOP.getProcOPList().size());

    programOP =
        ParserUtility.ast("proc main(): endproc proc main2(out a: integer, b: string): endproc");
    Assertions.assertEquals(2, programOP.getProcOPList().size());
  }

  @Test
  public void funOPListValid() throws Exception {
    ProgramOP programOP =
        ParserUtility.ast("proc main(): endproc func f() -> real: return 1; endfunc");
    Assertions.assertEquals(1, programOP.getFunOPList().size());

    programOP = ParserUtility.ast(
        "func f() -> real: return 1; endfunc proc main(): endproc func main2(a: integer) -> integer: return 0; endfunc");
    Assertions.assertEquals(2, programOP.getFunOPList().size());
  }

  @Test
  public void valid() throws Exception {
    ProgramOP programOP = ParserUtility.ast(
        "var a: boolean;\\ proc main(): endproc func f() -> real: return 1; endfunc");
    Assertions.assertEquals(1, programOP.getVarDeclOPList().size());
    Assertions.assertEquals(1, programOP.getProcOPList().size());
    Assertions.assertEquals(1, programOP.getFunOPList().size());

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
                        
            var a ^= 32.23;\\
                        
            proc p(out a: integer):
              a ^= 6;
            endproc
            """;
    programOP = ParserUtility.ast(source);
    Assertions.assertEquals(2, programOP.getVarDeclOPList().size());
    Assertions.assertEquals(2, programOP.getProcOPList().size());
    Assertions.assertEquals(2, programOP.getFunOPList().size());
  }
}
