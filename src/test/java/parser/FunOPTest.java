package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Utility;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.FunOP;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FunOPTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: return 1.2; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    FunOP funOP = programOP.getFunOPList().get(0);
    Assertions.assertEquals(1, funOP.getId().getId());
    Assertions.assertTrue(Utility.isListEmpty(funOP.getProcFunParamOPList()));
    Assertions.assertEquals(1, funOP.getReturnTypes().size());
    Assertions.assertEquals(Type.REAL, funOP.getReturnTypes().get(0));
    Assertions.assertNotNull(funOP.getBodyOP());

    source =
        new StringReader(
            "func f(a: integer, b: real) -> real, boolean: return 1.2; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    funOP = programOP.getFunOPList().get(0);
    Assertions.assertEquals(1, funOP.getId().getId());
    Assertions.assertEquals(2, funOP.getProcFunParamOPList().size());
    Assertions.assertEquals(2, funOP.getReturnTypes().size());
    Assertions.assertEquals(Type.REAL, funOP.getReturnTypes().get(0));
    Assertions.assertEquals(Type.BOOLEAN, funOP.getReturnTypes().get(1));
    Assertions.assertNotNull(funOP.getBodyOP());
  }

  @Test
  public void invalid() {

    // missing return TYPE in the signature
    StringReader source =
        new StringReader("func f() -> : return 1.2; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing Body
    source = new StringReader("func f() -> real: endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing endfunc
    source = new StringReader("func f() -> real: endproc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
