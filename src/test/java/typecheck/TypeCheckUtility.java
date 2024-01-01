package test.java.typecheck;

import java.io.StringReader;
import main.esercitazione5.Yylex;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.parser;
import main.esercitazione5.visitors.ScopingVisitor;
import main.esercitazione5.visitors.SemanticVisitor;
import main.esercitazione5.visitors.TypeCheckVisitor;

public class TypeCheckUtility {

  private TypeCheckUtility() {
  }

  public static ProgramOP ast(String sourceStr) throws Exception {
    StringReader source = new StringReader(sourceStr);
    Yylex lexer = new Yylex(source);
    parser p = new parser(lexer);
    ProgramOP programOP = (ProgramOP) p.parse().value;
    programOP.accept(new SemanticVisitor(lexer.getStringTable()));
    programOP.accept(new ScopingVisitor(lexer.getStringTable()));
    programOP.accept(new TypeCheckVisitor(lexer.getStringTable()));

    return programOP;
  }

}
