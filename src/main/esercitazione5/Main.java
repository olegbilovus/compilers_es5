package main.esercitazione5;

import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.visitors.GenCVisitor;
import main.esercitazione5.visitors.ScopingVisitor;
import main.esercitazione5.visitors.SemanticVisitor;
import main.esercitazione5.visitors.TypeCheckVisitor;

public class Main {

  public static void main(String[] args) throws Exception {
    File fileInput = new File(args[0]);
    Reader reader = new StringReader(Files.readString(fileInput.toPath()));

    Yylex lexer = new Yylex(reader);
    parser p = new parser(lexer);
    ProgramOP ast = (ProgramOP) p.parse().value;
    StringTable st = lexer.getStringTable();

    ast.accept(new SemanticVisitor(st));
    ast.accept(new ScopingVisitor(st));
    ast.accept(new TypeCheckVisitor(st));
    String generatedC = ast.accept(new GenCVisitor(st));

    String fileName = Paths.get(args[0]).getFileName().toString().split("\\.")[0];
    try (FileWriter fileWriter = new FileWriter(
        "test_files" + File.separator + "c_out" + File.separator + fileName + ".c")) {
      fileWriter.write(generatedC);
    }
  }

}
