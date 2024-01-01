package test.java.typecheck;

import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.expr.Expr;
import main.esercitazione5.ast.nodes.stat.AssignOP;
import main.esercitazione5.typecheck.exceptions.LogicTypeCheckException;
import main.esercitazione5.typecheck.exceptions.NotTypeCheckException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LogicTest {

  private Expr init(String sourceStr) throws Exception {
    return ((AssignOP) TypeCheckUtility.ast(sourceStr).getProcOPList().get(0).getBodyOP()
        .getStatList().get(0)).getExprList().get(0);
  }

  @Test public void valid() throws Exception {
    Expr expr = init("proc main(a: boolean): a^= true && false; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean): a^= 1 < 2 && 1 > 2; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean): a^= 1 < 2 || 1 > 2; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean): a^= !1 < 2 || 1 > 2; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean): a^= !1 < 2 || 1 > 2; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean): a^= !(1 < 2 || 1 > 2); endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());

    expr = init("proc main(a: boolean): a^= !true; endproc");
    Assertions.assertEquals(Type.BOOLEAN, expr.getNodeType());


  }

  @Test public void invalid() {
    // bool && string
    Assertions.assertThrows(LogicTypeCheckException.class,
        () -> init("proc main(a: boolean): a^= 1 && \"1\"; endproc"));

    // int  !! int
    Assertions.assertThrows(LogicTypeCheckException.class,
        () -> init("proc main(a: boolean): a^= 1 || 1; endproc"));

    // ! int
    Assertions.assertThrows(NotTypeCheckException.class,
        () -> init("proc main(a: boolean): a^= !1; endproc"));

    // ! string
    Assertions.assertThrows(NotTypeCheckException.class,
        () -> init("proc main(a: boolean): a^= !\"hello\"; endproc"));


  }
}
