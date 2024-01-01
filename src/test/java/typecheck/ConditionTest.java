package test.java.typecheck;

import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.stat.ElifOP;
import main.esercitazione5.ast.nodes.stat.IfOP;
import main.esercitazione5.ast.nodes.stat.Stat;
import main.esercitazione5.ast.nodes.stat.WhileOP;
import main.esercitazione5.typecheck.exceptions.ConditionNotABooleanTypeCheckException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConditionTest {

  private Stat init(String sourceStr) throws Exception {
    return (TypeCheckUtility.ast(sourceStr).getProcOPList().get(0).getBodyOP().getStatList()
        .get(0));
  }

  @Test public void valid() throws Exception {

    WhileOP whileOP = (WhileOP) init("proc main(): while true do endwhile; endproc");
    Assertions.assertEquals(Type.BOOLEAN, whileOP.getCondition().getNodeType());

    whileOP = (WhileOP) init("proc main(): while false do endwhile; endproc");
    Assertions.assertEquals(Type.BOOLEAN, whileOP.getCondition().getNodeType());

    whileOP = (WhileOP) init("proc main(): while true && false do endwhile; endproc");
    Assertions.assertEquals(Type.BOOLEAN, whileOP.getCondition().getNodeType());

    whileOP = (WhileOP) init("proc main(): while 1 > 2 do endwhile; endproc");
    Assertions.assertEquals(Type.BOOLEAN, whileOP.getCondition().getNodeType());

    whileOP = (WhileOP) init("proc main(a: real): while a > 1 do endwhile; endproc");
    Assertions.assertEquals(Type.BOOLEAN, whileOP.getCondition().getNodeType());

    IfOP ifOP = (IfOP) init("proc main(): if true then endif; endproc");
    Assertions.assertEquals(Type.BOOLEAN, ifOP.getCondition().getNodeType());

    ifOP = (IfOP) init("proc main(): if 1 < 10 then endif; endproc");
    Assertions.assertEquals(Type.BOOLEAN, ifOP.getCondition().getNodeType());

    ifOP = (IfOP) init("proc main(): if 1+1 < 3 then endif; endproc");
    Assertions.assertEquals(Type.BOOLEAN, ifOP.getCondition().getNodeType());

    ElifOP elifOP = ((IfOP) init(
        "proc main(): if false then elseif true then endif; endproc")).getElifOPList().get(0);
    Assertions.assertEquals(Type.BOOLEAN, elifOP.getCondition().getNodeType());

    elifOP = ((IfOP) init(
        "proc main(): if false then elseif 1 > 2/8.5 then endif; endproc")).getElifOPList().get(0);
    Assertions.assertEquals(Type.BOOLEAN, elifOP.getCondition().getNodeType());


  }

  @Test public void invalid() {
    // integer
    Assertions.assertThrows(ConditionNotABooleanTypeCheckException.class,
        () -> init("proc main(): while 1 do endwhile; endproc"));

    // string
    Assertions.assertThrows(ConditionNotABooleanTypeCheckException.class,
        () -> init("proc main(): while \"T\" do endwhile; endproc"));

    // integer + integer
    Assertions.assertThrows(ConditionNotABooleanTypeCheckException.class,
        () -> init("proc main(): while 1+1 do endwhile; endproc"));

    // integer + integer
    Assertions.assertThrows(ConditionNotABooleanTypeCheckException.class,
        () -> init("proc main(): if 1+1 then endif; endproc"));

    // integer
    Assertions.assertThrows(ConditionNotABooleanTypeCheckException.class,
        () -> init("proc main(): if 1 then endif; endproc"));

    // integer
    Assertions.assertThrows(ConditionNotABooleanTypeCheckException.class,
        () -> init("proc main(): if false then elseif 1 then endif; endproc"));


  }
}
