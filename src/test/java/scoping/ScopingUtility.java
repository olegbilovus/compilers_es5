package test.java.scoping;

import java.io.StringReader;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import main.esercitazione5.visitors.ScopingVisitor;

public class ScopingUtility {

  private ScopingUtility() {
  }

  public static ProgramOP astScoped(String sourceStr) throws Exception {
    StringReader source = new StringReader(sourceStr);
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    ScopingVisitor scopingVisitor = new ScopingVisitor(lexer.getStringTable());
    programOP.accept(scopingVisitor);

    return programOP;
  }

}
