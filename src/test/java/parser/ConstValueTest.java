package test.java.parser;

import java.io.StringReader;
import java.util.List;
import main.esercitazione5.Utility;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.Const;
import main.esercitazione5.ast.ConstValue;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConstValueTest {


  @Test
  public void valid() throws Exception {

    StringReader source = new StringReader("var a: integer;\\ proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    List<ConstValue> constValueList = programOP.getVarDeclOPList().get(0).getConstValueList();
    Assertions.assertTrue(Utility.isListEmpty(constValueList));

    source = new StringReader("var a ^= 1.3;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    constValueList = programOP.getVarDeclOPList().get(0).getConstValueList();
    Assertions.assertEquals("1.3", constValueList.get(0).value());
    Assertions.assertEquals(Const.REAL_CONST, constValueList.get(0).constType());

    source = new StringReader("var a, b ^= 1.2, 3;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    constValueList = programOP.getVarDeclOPList().get(0).getConstValueList();
    Assertions.assertEquals("1.2", constValueList.get(0).value());
    Assertions.assertEquals(Const.REAL_CONST, constValueList.get(0).constType());
    Assertions.assertEquals("3", constValueList.get(1).value());
    Assertions.assertEquals(Const.INTEGER_CONST, constValueList.get(1).constType());

    source = new StringReader("var a ^= \"hello\";\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    constValueList = programOP.getVarDeclOPList().get(0).getConstValueList();
    Assertions.assertEquals("2", constValueList.get(0).value());
    Assertions.assertEquals(Const.STRING_CONST, constValueList.get(0).constType());

    source = new StringReader("var a ^= true;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    constValueList = programOP.getVarDeclOPList().get(0).getConstValueList();
    Assertions.assertNull(constValueList.get(0).value());
    Assertions.assertEquals(Const.TRUE, constValueList.get(0).constType());

    source = new StringReader("var a ^= false;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    constValueList = programOP.getVarDeclOPList().get(0).getConstValueList();
    Assertions.assertNull(constValueList.get(0).value());
    Assertions.assertEquals(Const.FALSE, constValueList.get(0).constType());
  }
}
