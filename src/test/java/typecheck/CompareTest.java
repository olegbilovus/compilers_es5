package test.java.typecheck;

import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.expr.Expr;
import main.esercitazione5.ast.nodes.stat.AssignOP;
import main.esercitazione5.typecheck.exceptions.CompareTypeCheckException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CompareTest {

  private Expr init(String sourceStr) throws Exception {
    return ((AssignOP) TypeCheckUtility.ast(sourceStr).getProcOPList().get(0).getBodyOP()
        .getStatList().get(0)).getExprList().get(0);
  }

  @Test public void valid() throws Exception {
    Expr expr = init("proc main(a: boolean): a^= 1 = 3; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean): a^= 1 < 2; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean): a^= 2 <= 3 * 4; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean): a^= 2 + 3 > 4; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean): a^= (2 + 3) < 4.4; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean, b: real): a ^= 2 > b; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean, b: real): a^= 2.8 < -3; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean, b: real): a^= \"hello\" = \"hello\"; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

  }

  @Test public void invalid() {
    // int  > string
    Assertions.assertThrows(CompareTypeCheckException.class,
        () -> init("proc main(a: real): a^= 1>\"1\"; endproc"));

    // real  = string
    Assertions.assertThrows(CompareTypeCheckException.class,
        () -> init("proc main(a: real): a^= 1.5=\"1\"; endproc"));

    // int  < boolean
    Assertions.assertThrows(CompareTypeCheckException.class,
        () -> init("proc main(a: real): a^= 1<true; endproc"));

    // real  = boolean
    Assertions.assertThrows(CompareTypeCheckException.class,
        () -> init("proc main(a: real): a^= 1.5=true; endproc"));


  }
}
