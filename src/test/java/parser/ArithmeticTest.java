package test.java.parser;

import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.expr.AddOP;
import main.esercitazione5.ast.nodes.expr.AndOP;
import main.esercitazione5.ast.nodes.expr.DiffOP;
import main.esercitazione5.ast.nodes.expr.DivOP;
import main.esercitazione5.ast.nodes.expr.GTOP;
import main.esercitazione5.ast.nodes.expr.IntegerConstExpr;
import main.esercitazione5.ast.nodes.expr.MulOP;
import main.esercitazione5.ast.nodes.expr.NotOP;
import main.esercitazione5.ast.nodes.expr.UminusOP;
import main.esercitazione5.ast.nodes.stat.AssignOP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArithmeticTest {

  private AssignOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
  }

  @Test
  public void valid() throws Exception {
    AssignOP assignOP = init("func f() -> real: a ^= 2 + 3 + 4; endfunc proc main(): endproc");
    Assertions.assertInstanceOf(AddOP.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprRight());

    assignOP = init("func f() -> real: a ^= 2 + (3 + 4); endfunc proc main(): endproc");
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(AddOP.class,
        assignOP.getExprList().get(0).getExprRight());
    Assertions.assertTrue(assignOP.getExprList().get(0).getExprRight().isInPar());

    assignOP = init("func f() -> real: a ^= 2 + 3 * 4; endfunc proc main(): endproc");
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(MulOP.class,
        assignOP.getExprList().get(0).getExprRight());

    assignOP = init("func f() -> real: a ^= 2 + 3 / 4; endfunc proc main(): endproc");
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(DivOP.class,
        assignOP.getExprList().get(0).getExprRight());

    assignOP = init("func f() -> real: a ^= (2 + 3) * 4; endfunc proc main(): endproc");
    Assertions.assertInstanceOf(MulOP.class,
        assignOP.getExprList().get(0));
    Assertions.assertInstanceOf(AddOP.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertTrue(assignOP.getExprList().get(0).getExprLeft().isInPar());
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprRight());

    assignOP = init("func f() -> real: a ^= 2 + -3; endfunc proc main(): endproc");
    Assertions.assertInstanceOf(AddOP.class,
        assignOP.getExprList().get(0));
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(UminusOP.class,
        assignOP.getExprList().get(0).getExprRight());

    assignOP = init("func f() -> real: a ^= 2 - -3; endfunc proc main(): endproc");
    Assertions.assertInstanceOf(DiffOP.class,
        assignOP.getExprList().get(0));
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(UminusOP.class,
        assignOP.getExprList().get(0).getExprRight());

    assignOP = init("func f() -> real: a ^= !2 > 4; endfunc proc main(): endproc");
    Assertions.assertInstanceOf(NotOP.class,
        assignOP.getExprList().get(0));
    Assertions.assertInstanceOf(GTOP.class,
        assignOP.getExprList().get(0).getExprLeft());

    assignOP = init("func f() -> real: a ^= !(2 > 4 && true); endfunc proc main(): endproc");
    Assertions.assertInstanceOf(NotOP.class,
        assignOP.getExprList().get(0));
    Assertions.assertInstanceOf(AndOP.class,
        assignOP.getExprList().get(0).getExprLeft());

    assignOP = init("func f() -> real: a ^= !2 > 4 && true; endfunc proc main(): endproc");
    Assertions.assertInstanceOf(AndOP.class,
        assignOP.getExprList().get(0));
    Assertions.assertInstanceOf(NotOP.class,
        assignOP.getExprList().get(0).getExprLeft());
  }
}
