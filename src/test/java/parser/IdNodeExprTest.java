package test.java.parser;

import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.expr.IdNodeExpr;
import main.esercitazione5.ast.nodes.stat.CallProcOP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdNodeExprTest {


  @Test
  public void valid() throws Exception {
    ProgramOP programOP =
        ParserUtility.ast("func f() -> real: p(a, @b); endfunc proc main(): endproc");
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
