package test.java.semantic;

import java.io.StringReader;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import main.esercitazione5.visitors.ScopingVisitor;
import main.esercitazione5.visitors.SemanticVisitor;

public class SemanticUtility {

  private SemanticUtility() {
  }

  public static ProgramOP astSemantic(String sourceStr) throws Exception {
    StringReader source = new StringReader(sourceStr);
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    programOP.accept(new ScopingVisitor(lexer.getStringTable()));
    programOP.accept(new SemanticVisitor(lexer.getStringTable()));

    return programOP;
  }

}
