package main.esercitazione5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.visitors.DebugVisitor;
import main.esercitazione5.visitors.GraphvizASTVisitor;
import main.esercitazione5.visitors.GraphvizScopeTablesVisitor;
import main.esercitazione5.visitors.ScopingVisitor;
import main.esercitazione5.visitors.SemanticVisitor;
import main.esercitazione5.visitors.Visitor;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;

public class Main {

  public static void main(String[] args) throws Exception {
    ArgumentParser parser = ArgumentParsers.newFor("Toy2 To AST").build().description(
        "Translate a Toy2 source to an AST.");
    parser.addArgument("-i").type(Arguments.fileType().verifyCanRead())
        .help("Take the input from a file.");
    parser.addArgument("-o").type(Arguments.fileType().verifyCanCreate())
        .help("Output to a file.");

    final String[] availableVisitors =
        {"graphviz_scope", "scope_check", "semantic_check", "graphviz_ast", "debug"};

    MutuallyExclusiveGroup visitorGroup =
        parser.addMutuallyExclusiveGroup("Visitor type");
    visitorGroup.addArgument("--" + availableVisitors[0]).action(Arguments.storeTrue())
        .help(
            "Create a Graphviz Tables diagram in dot language which shows all the scoping tables.");
    visitorGroup.addArgument("--" + availableVisitors[1]).action(Arguments.storeTrue())
        .help("Check that there is no scoping errors.");
    visitorGroup.addArgument("--" + availableVisitors[2]).action(Arguments.storeTrue())
        .help("Check that there is no semantic errors.");
    visitorGroup.addArgument("--" + availableVisitors[3]).action(Arguments.storeTrue())
        .help("Create a Graphviz AST diagram in dot language.");
    visitorGroup.addArgument("--" + availableVisitors[4]).action(Arguments.storeTrue())
        .help("(default) Debug the Lexer and Parser. The input will run on both "
            + "and produce the equivalent of source in Toy2.");

    Namespace ns = parser.parseArgsOrFail(args);

    File fileInput = ns.get("i");
    Reader reader;

    if (fileInput == null) {
      System.out.println("Paste the Toy2 source, hit Return, then Cmd-D (in MacOs) "
          + "or Ctrl-D (in Windows)");
      InputStreamReader inp = new InputStreamReader(System.in);
      reader = new BufferedReader(inp);
    } else {
      reader = new StringReader(Files.readString(fileInput.toPath()));
    }

    Yylex lexer = new Yylex(reader);
    parser p = new parser(lexer);
    ProgramOP ast = (ProgramOP) p.parse().value;
    StringTable st = lexer.getStringTable();

    final boolean[] chosenVisitor = new boolean[availableVisitors.length];
    for (int i = 0; i < availableVisitors.length; i++) {
      Boolean val = ns.getBoolean(availableVisitors[i]);
      chosenVisitor[i] = val != null && val;
    }

    Visitor visitor;

    if (chosenVisitor[0]) {
      ast.accept(new SemanticVisitor(st));
      ast.accept(new ScopingVisitor(st));
      visitor = new GraphvizScopeTablesVisitor(st);
    } else if (chosenVisitor[1]) {
      ast.accept(new SemanticVisitor(st));
      visitor = new ScopingVisitor(st);

    } else if (chosenVisitor[2]) {
      visitor = new SemanticVisitor(st);
    } else if (chosenVisitor[3]) {
      visitor = new GraphvizASTVisitor(st);
    } else {
      visitor = new DebugVisitor(st);
    }

    String visitorRes = visitor.visit(ast).toString();
    File fileOutput = ns.get("o");

    if (fileOutput == null) {
      System.out.println(visitorRes);
    } else {
      try (FileWriter fileWriter = new FileWriter(fileOutput)) {
        fileWriter.write(visitorRes);
        if (chosenVisitor[0] || chosenVisitor[3]) {
          System.out.println(
              "dot -Tsvg \"" + fileOutput.getAbsolutePath() + "\" -o " + fileOutput.getName()
                  .split("\\.")[0] + ".svg");
        }
      }
    }


  }
}
