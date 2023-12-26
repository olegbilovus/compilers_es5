package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Utility;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProgramOPTest {


  @Test
  public void varDeclOPListValid() throws Exception {
    StringReader source = new StringReader("proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    Assertions.assertTrue(Utility.isListEmpty(programOP.getVarDeclOPList()));

    source = new StringReader("var a: integer;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(1, programOP.getVarDeclOPList().size());

    source = new StringReader("var a: integer; b: boolean;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(2, programOP.getVarDeclOPList().size());

    source = new StringReader("var a, b: integer;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(1, programOP.getVarDeclOPList().size());

    source = new StringReader("var a, b: integer;\\ proc main(): endproc var c ^= 2;\\");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(2, programOP.getVarDeclOPList().size());

    source = new StringReader(
        "var a, b: integer;\\ proc main(): var c: boolean;\\ endproc var c ^= 2;\\");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(2, programOP.getVarDeclOPList().size());
  }

  @Test
  public void procOPListValid() throws Exception {
    StringReader source = new StringReader("proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(1, programOP.getProcOPList().size());

    source = new StringReader("proc main(): endproc proc main2(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(2, programOP.getProcOPList().size());

    source =
        new StringReader("proc main(): endproc proc main2(out a: integer, b: string): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(2, programOP.getProcOPList().size());
  }

  @Test
  public void funOPListValid() throws Exception {
    StringReader source =
        new StringReader("proc main(): endproc func f() -> real: return 1; endfunc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(1, programOP.getFunOPList().size());

    source = new StringReader(
        "func f() -> real: return 1; endfunc proc main(): endproc func main2(a: integer) -> integer: return 0; endfunc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(2, programOP.getFunOPList().size());
  }

  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader(
            "var a: boolean;\\ proc main(): endproc func f() -> real: return 1; endfunc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(1, programOP.getVarDeclOPList().size());
    Assertions.assertEquals(1, programOP.getProcOPList().size());
    Assertions.assertEquals(1, programOP.getFunOPList().size());

    source = new StringReader(
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
            """);
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    Assertions.assertEquals(2, programOP.getVarDeclOPList().size());
    Assertions.assertEquals(2, programOP.getProcOPList().size());
    Assertions.assertEquals(2, programOP.getFunOPList().size());
  }
}
