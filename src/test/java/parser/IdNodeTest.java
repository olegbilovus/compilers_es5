package test.java.parser;

import java.util.List;
import main.esercitazione5.ast.nodes.IdNode;
import main.esercitazione5.ast.nodes.ProgramOP;
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
  }
}
