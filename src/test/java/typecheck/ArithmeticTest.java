package test.java.typecheck;

import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.expr.Expr;
import main.esercitazione5.ast.nodes.stat.AssignOP;
import main.esercitazione5.typecheck.exceptions.ArithmeticTypeCheckException;
import main.esercitazione5.typecheck.exceptions.UMinusTypeCheckException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArithmeticTest {

  private Expr init(String sourceStr) throws Exception {
    return ((AssignOP) TypeCheckUtility.ast(sourceStr).getProcOPList().get(0).getBodyOP()
        .getStatList().get(0)).getExprList().get(0);
  }

  @Test public void valid() throws Exception {

    Expr expr = init("proc main(a: integer): a^= 1+1; endproc");
    Assertions.assertEquals(Type.INTEGER, expr.getNodeType());

    expr = init("proc main(a: integer): a^= 2 + (3 + 4); endproc");
    Assertions.assertEquals(Type.INTEGER, expr.getNodeType());

    expr = init("proc main(a: integer): a^= 2 + 3 * 4; endproc");
    Assertions.assertEquals(Type.INTEGER, expr.getNodeType());

    expr = init("proc main(a: integer): a^= 2 + 3 / 4; endproc");
    Assertions.assertEquals(Type.INTEGER, expr.getNodeType());

    expr = init("proc main(a: real): a^= (2 + 3) * 4.4; endproc");
    Assertions.assertEquals(Type.REAL, expr.getNodeType());

    expr = init("proc main(a: real): a ^= 2 + -3.2; endproc");
    Assertions.assertEquals(Type.REAL, expr.getNodeType());

    expr = init("proc main(a: real): a^= 2.8 - -3; endproc");
    Assertions.assertEquals(Type.REAL, expr.getNodeType());

    expr = init("proc main(a: real, b: real): a ^= 2 + -b; endproc");
    Assertions.assertEquals(Type.REAL, expr.getNodeType());

    expr = init("proc main(a: real, b: integer): a ^= b + -3.2; endproc");
    Assertions.assertEquals(Type.REAL, expr.getNodeType());

    // string concat
    expr = init("proc main(a: string): a^= \"hello\" + \"world\"; endproc");
    Assertions.assertEquals(Type.STRING, expr.getNodeType());

    expr = init("proc main(a: string): a^= 1+\"1\"; endproc");
    Assertions.assertEquals(Type.STRING, expr.getNodeType());

    expr = init("proc main(a: string): a^= 1.1+\"1\"; endproc");
    Assertions.assertEquals(Type.STRING, expr.getNodeType());

    expr = init("proc main(a: string): a^= true+\"1\"; endproc");
    Assertions.assertEquals(Type.STRING, expr.getNodeType());

  }

  @Test public void invalid() {
    // int  + boolean
    Assertions.assertThrows(ArithmeticTypeCheckException.class,
        () -> init("proc main(a: real): a^= 1+true; endproc"));

    // real  + boolean
    Assertions.assertThrows(ArithmeticTypeCheckException.class,
        () -> init("proc main(a: real): a^= 1.5+true; endproc"));

    // UMinus on a string
    Assertions.assertThrows(UMinusTypeCheckException.class,
        () -> init("proc main(a: real): a^= 1+ -\"1\"; endproc"));

    // UMinus on a boolean
    Assertions.assertThrows(UMinusTypeCheckException.class,
        () -> init("proc main(a: real): a^= 1+ -false; endproc"));


  }
}
