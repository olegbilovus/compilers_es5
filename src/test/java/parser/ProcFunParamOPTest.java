package test.java.parser;

import java.io.StringReader;
import java.util.List;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.ParamAccess;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.ProcFunParamOP;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProcFunParamOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader(
            "func f(a: integer) -> real, boolean: return 1.2; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    ProcFunParamOP procFunParamOP = programOP.getFunOPList().get(0).getProcFunParamOPList().get(0);
    Assertions.assertEquals(2, procFunParamOP.getId().getId());
    Assertions.assertEquals(Type.INTEGER, procFunParamOP.getType());
    Assertions.assertEquals(ParamAccess.IN, procFunParamOP.getParamAccess());

    source =
        new StringReader(
            "func f(a: string, b: boolean) -> real, boolean: return 1.2; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    List<ProcFunParamOP> procFunParamOPList =
        programOP.getFunOPList().get(0).getProcFunParamOPList();
    Assertions.assertEquals(2, procFunParamOPList.get(0).getId().getId());
    Assertions.assertEquals(Type.STRING, procFunParamOPList.get(0).getType());
    Assertions.assertEquals(ParamAccess.IN, procFunParamOPList.get(0).getParamAccess());
    Assertions.assertEquals(3, procFunParamOPList.get(1).getId().getId());
    Assertions.assertEquals(Type.BOOLEAN, procFunParamOPList.get(1).getType());
    Assertions.assertEquals(ParamAccess.IN, procFunParamOPList.get(1).getParamAccess());

    source =
        new StringReader(
            "proc p(out a: string, b: boolean): endproc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    procFunParamOPList =
        programOP.getProcOPList().get(0).getProcFunParamOPList();
    Assertions.assertEquals(2, procFunParamOPList.get(0).getId().getId());
    Assertions.assertEquals(Type.STRING, procFunParamOPList.get(0).getType());
    Assertions.assertEquals(ParamAccess.OUT, procFunParamOPList.get(0).getParamAccess());
    Assertions.assertEquals(3, procFunParamOPList.get(1).getId().getId());
    Assertions.assertEquals(Type.BOOLEAN, procFunParamOPList.get(1).getType());
    Assertions.assertEquals(ParamAccess.INOUT, procFunParamOPList.get(1).getParamAccess());
  }

  @Test
  public void invalid() {

    // missing TYPE of the parameters
    StringReader source =
        new StringReader("func f(a, b) -> : return 1.2; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing one of the TYPE of the parameters
    source = new StringReader("func f(a, b: integer) -> real: endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // using OUT in a function
    source = new StringReader("func f(out a: integer) -> real: endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
