package test.java.parser;

import java.util.List;
import main.esercitazione5.Utility;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.VarDeclOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VarDeclOPTest {

  private VarDeclOP init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return programOP.getVarDeclOPList().get(0);
  }

  @Test
  public void valid() throws Exception {
    VarDeclOP varDeclOP = init("var a: integer;\\ proc main(): endproc");
    Assertions.assertEquals(1, varDeclOP.getIdList().size());
    Assertions.assertTrue(Utility.isListEmpty(varDeclOP.getConstValueList()));
    Assertions.assertEquals(Type.INTEGER, varDeclOP.getType());

    varDeclOP = init("var a, b: integer;\\ proc main(): endproc");
    Assertions.assertEquals(2, varDeclOP.getIdList().size());
    Assertions.assertTrue(Utility.isListEmpty(varDeclOP.getConstValueList()));
    Assertions.assertEquals(Type.INTEGER, varDeclOP.getType());

    varDeclOP = init("var a ^= 1.2;\\ proc main(): endproc");
    Assertions.assertEquals(1, varDeclOP.getIdList().size());
    Assertions.assertEquals(1, varDeclOP.getConstValueList().size());
    Assertions.assertNull(varDeclOP.getType());

    varDeclOP = init("var a, b ^= 1.2, 3;\\ proc main(): endproc");
    Assertions.assertEquals(2, varDeclOP.getIdList().size());
    Assertions.assertEquals(2, varDeclOP.getConstValueList().size());
    Assertions.assertNull(varDeclOP.getType());

    ProgramOP programOP = ParserUtility.ast("var a ^= 1.2; b ^= 3;\\ proc main(): endproc");
    List<VarDeclOP> varDeclOPList = programOP.getVarDeclOPList();
    Assertions.assertEquals(2, varDeclOPList.size());
  }

  @Test
  public void invalid() {

    // missing TYPE
    parser p = ParserUtility.parser("var a;\\ proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // missing ENDVAR
    p = ParserUtility.parser("var a: boolean; proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // missing VAR
    p = ParserUtility.parser("a: boolean;\\ proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // missing SEMI
    p = ParserUtility.parser("var a: boolean\\ proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // #IDs != #Consts
    p = ParserUtility.parser("var a, b ^= 4; proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);

    // invalide type
    p = ParserUtility.parser("var b: double; proc main(): endproc");
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
