package test.java.parser;

import java.util.List;
import main.esercitazione5.Utility;
import main.esercitazione5.ast.Const;
import main.esercitazione5.ast.ConstValue;
import main.esercitazione5.ast.nodes.ProgramOP;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConstValueTest {

  private List<ConstValue> init(String sourceStr) throws Exception {
    ProgramOP programOP = ParserUtility.ast(sourceStr);
    return programOP.getVarDeclOPList().get(0).getConstValueList();
  }

  @Test
  public void valid() throws Exception {

    List<ConstValue> constValueList = init("var a: integer;\\ proc main(): endproc");
    Assertions.assertTrue(Utility.isListEmpty(constValueList));

    constValueList = init("var a ^= 1.3;\\ proc main(): endproc");
    Assertions.assertEquals("1.3", constValueList.get(0).value());
    Assertions.assertEquals(Const.REAL_CONST, constValueList.get(0).constType());

    constValueList = init("var a, b ^= 1.2, 3;\\ proc main(): endproc");
    Assertions.assertEquals("1.2", constValueList.get(0).value());
    Assertions.assertEquals(Const.REAL_CONST, constValueList.get(0).constType());
    Assertions.assertEquals("3", constValueList.get(1).value());
    Assertions.assertEquals(Const.INTEGER_CONST, constValueList.get(1).constType());

    constValueList = init("var a ^= \"hello\";\\ proc main(): endproc");
    Assertions.assertEquals("2", constValueList.get(0).value());
    Assertions.assertEquals(Const.STRING_CONST, constValueList.get(0).constType());

    constValueList = init("var a ^= true;\\ proc main(): endproc");
    Assertions.assertNull(constValueList.get(0).value());
    Assertions.assertEquals(Const.TRUE, constValueList.get(0).constType());

    constValueList = init("var a ^= false;\\ proc main(): endproc");
    Assertions.assertNull(constValueList.get(0).value());
    Assertions.assertEquals(Const.FALSE, constValueList.get(0).constType());
  }
}
