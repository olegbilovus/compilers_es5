package test.java.parser;

import java.util.List;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.expr.IdNode;
import main.esercitazione5.ast.nodes.stat.CallProcOP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdNodeTest {

  private List<IdNode> init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return programOP.getVarDeclOPList().get(0).getIdList();
  }

  @Test
  public void valid() throws Exception {

    List<IdNode> idNodeList = init("var a: integer;\\ proc main(): endproc");
    Assertions.assertEquals(1, idNodeList.get(0).getId());

    idNodeList = init("var a, b: integer;\\ proc main(): endproc");
    Assertions.assertEquals(1, idNodeList.get(0).getId());
    Assertions.assertEquals(2, idNodeList.get(1).getId());

    ProgramOP programOP =
        ParserUtility.ast("func f() -> real: p(a, @b); endfunc proc main(): endproc");
    CallProcOP callProcOP =
        (CallProcOP) programOP.getFunOPList().get(0).getBodyOP().getStatList().get(0);
    IdNode idNode1 = (IdNode) callProcOP.getParams().get(0);
    Assertions.assertEquals(3, idNode1.getId());
    Assertions.assertFalse(idNode1.isRef());
    IdNode idNode2 = (IdNode) callProcOP.getParams().get(1);
    Assertions.assertEquals(4, idNode2.getId());
    Assertions.assertTrue(idNode2.isRef());
  }
}
