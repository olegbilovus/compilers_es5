package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.expr.IdNodeExpr;
import main.esercitazione5.ast.nodes.stat.CallProcOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdNodeExprTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: p(a, @b); endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    CallProcOP callProcOP =
        (CallProcOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    IdNodeExpr idNodeExpr1 = (IdNodeExpr) callProcOP.getParams().get(0);
    Assertions.assertEquals(3, idNodeExpr1.getId().getId());
    Assertions.assertFalse(idNodeExpr1.isRef());
    IdNodeExpr idNodeExpr2 = (IdNodeExpr) callProcOP.getParams().get(1);
    Assertions.assertEquals(4, idNodeExpr2.getId().getId());
    Assertions.assertTrue(idNodeExpr2.isRef());
  }
}
