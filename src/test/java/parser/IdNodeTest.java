package test.java.parser;

import java.io.StringReader;
import java.util.List;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.IdNode;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IdNodeTest {


  @Test
  public void valid() throws Exception {

    StringReader source = new StringReader("var a: integer;\\ proc main(): endproc");
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    List<IdNode> idNodeList = programOP.getVarDeclOPList().get(0).getIdList();
    Assertions.assertEquals(1, idNodeList.get(0).getId());

    source = new StringReader("var a, b: integer;\\ proc main(): endproc");
    lexer = new Yylex(source);
    p = new parser(lexer);
    programOP = (ProgramOP) p.parse().value;
    idNodeList = programOP.getVarDeclOPList().get(0).getIdList();
    Assertions.assertEquals(1, idNodeList.get(0).getId());
    Assertions.assertEquals(2, idNodeList.get(1).getId());
  }
}
