package test.java.parser;

import java.io.StringReader;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.expr.AddOP;
import main.esercitazione5.ast.nodes.expr.DiffOP;
import main.esercitazione5.ast.nodes.expr.DivOP;
import main.esercitazione5.ast.nodes.expr.GTOP;
import main.esercitazione5.ast.nodes.expr.IntegerConstExpr;
import main.esercitazione5.ast.nodes.expr.MulOP;
import main.esercitazione5.ast.nodes.expr.NotOP;
import main.esercitazione5.ast.nodes.expr.UminusOP;
import main.esercitazione5.ast.nodes.stat.AssignOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArithmeticTest {


  @Test
  public void valid() throws Exception {
    StringReader source =
        new StringReader("func f() -> real: a ^= 2 + 3 + 4; endfunc proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    AssignOP assignOP = (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertInstanceOf(AddOP.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprRight());

    source =
        new StringReader("func f() -> real: a ^= 2 + (3 + 4); endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    assignOP = (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(AddOP.class,
        assignOP.getExprList().get(0).getExprRight());
    Assertions.assertTrue(assignOP.getExprList().get(0).getExprRight().isInPar());

    source =
        new StringReader("func f() -> real: a ^= 2 + 3 * 4; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    assignOP = (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(MulOP.class,
        assignOP.getExprList().get(0).getExprRight());

    source =
        new StringReader("func f() -> real: a ^= 2 + 3 / 4; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    assignOP = (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(DivOP.class,
        assignOP.getExprList().get(0).getExprRight());

    source =
        new StringReader("func f() -> real: a ^= (2 + 3) * 4; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    assignOP = (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertInstanceOf(MulOP.class,
        assignOP.getExprList().get(0));
    Assertions.assertInstanceOf(AddOP.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertTrue(assignOP.getExprList().get(0).getExprLeft().isInPar());
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprRight());

    source =
        new StringReader("func f() -> real: a ^= 2 + -3; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    assignOP = (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertInstanceOf(AddOP.class,
        assignOP.getExprList().get(0));
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(UminusOP.class,
        assignOP.getExprList().get(0).getExprRight());

    source =
        new StringReader("func f() -> real: a ^= 2 - -3; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    assignOP = (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertInstanceOf(DiffOP.class,
        assignOP.getExprList().get(0));
    Assertions.assertInstanceOf(IntegerConstExpr.class,
        assignOP.getExprList().get(0).getExprLeft());
    Assertions.assertInstanceOf(UminusOP.class,
        assignOP.getExprList().get(0).getExprRight());

    source =
        new StringReader("func f() -> real: a ^= !2 > 4; endfunc proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    assignOP = (AssignOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    Assertions.assertInstanceOf(NotOP.class,
        assignOP.getExprList().get(0));
    Assertions.assertInstanceOf(GTOP.class,
        assignOP.getExprList().get(0).getExprLeft());
  }
}
