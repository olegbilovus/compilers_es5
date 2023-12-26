package test.java.parser;

import java.io.StringReader;
import java.util.List;
import main.esercitazione5.Utility;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.VarDeclOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VarDeclOPTest {


  @Test
  public void valid() throws Exception {

    StringReader source = new StringReader("var a: integer;\\ proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    VarDeclOP varDeclOP = programOP.getVarDeclOPList().get(0);
    Assertions.assertEquals(1, varDeclOP.getIdList().size());
    Assertions.assertTrue(Utility.isListEmpty(varDeclOP.getConstValueList()));
    Assertions.assertEquals(Type.INTEGER, varDeclOP.getType());

    source = new StringReader("var a, b: integer;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    varDeclOP = programOP.getVarDeclOPList().get(0);
    Assertions.assertEquals(2, varDeclOP.getIdList().size());
    Assertions.assertTrue(Utility.isListEmpty(varDeclOP.getConstValueList()));
    Assertions.assertEquals(Type.INTEGER, varDeclOP.getType());

    source = new StringReader("var a ^= 1.2;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    varDeclOP = programOP.getVarDeclOPList().get(0);
    Assertions.assertEquals(1, varDeclOP.getIdList().size());
    Assertions.assertEquals(1, varDeclOP.getConstValueList().size());
    Assertions.assertNull(varDeclOP.getType());

    source = new StringReader("var a, b ^= 1.2, 3;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    varDeclOP = programOP.getVarDeclOPList().get(0);
    Assertions.assertEquals(2, varDeclOP.getIdList().size());
    Assertions.assertEquals(2, varDeclOP.getConstValueList().size());
    Assertions.assertNull(varDeclOP.getType());

    source = new StringReader("var a ^= 1.2; b ^= 3;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    List<VarDeclOP> varDeclOPList = programOP.getVarDeclOPList();
    Assertions.assertEquals(2, varDeclOPList.size());
  }

  @Test
  public void invalid() {

    // missing TYPE
    StringReader source = new StringReader("var a;\\ proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing ENDVAR
    source = new StringReader("var a: boolean; proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing VAR
    source = new StringReader("a: boolean;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // missing SEMI
    source = new StringReader("var a: boolean\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // #IDs != #Consts
    source = new StringReader("var a, b ^= 4; proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);

    // invalide type
    source = new StringReader("var b: double; proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    Assertions.assertThrows(Exception.class, p::parse);
  }
}
