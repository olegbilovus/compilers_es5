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
import main.esercitazione5.visitors.GenCVisitor;
import main.esercitazione5.visitors.GraphvizASTVisitor;
import main.esercitazione5.visitors.GraphvizScopeTablesVisitor;
import main.esercitazione5.visitors.ScopingVisitor;
import main.esercitazione5.visitors.SemanticVisitor;
import main.esercitazione5.visitors.TypeCheckVisitor;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;

public class Main {

  public static void main(String[] args) throws Exception {
    ArgumentParser parser = ArgumentParsers.newFor("Toy2 to stages of a compiler ").build()
        .description("Translate a Toy2 source to different stages of a compiler.");
    parser.addArgument("-i").type(Arguments.fileType().verifyCanRead())
        .help("Take the input from a file.");
    parser.addArgument("-o").type(Arguments.fileType().verifyCanCreate()).help("Output to a file.");
    parser.addArgument("-v").action(Arguments.storeTrue()).help("Parser verbose.");

    final String[] availableVisitors =
        {"gen_c", "type_check", "graphviz_scope", "scope_check", "semantic_check", "graphviz_ast",
            "debug"};

    MutuallyExclusiveGroup visitorGroup = parser.addMutuallyExclusiveGroup("Visitor type");
    visitorGroup.addArgument("--" + availableVisitors[0]).action(Arguments.storeTrue())
        .help("(default) Generate the C code.");
    visitorGroup.addArgument("--" + availableVisitors[1]).action(Arguments.storeTrue())
        .help("Check that there is no type errors.");
    visitorGroup.addArgument("--" + availableVisitors[2]).action(Arguments.storeTrue()).help(
        "Create a Graphviz Tables diagram in dot language which shows all the scoping tables.");
    visitorGroup.addArgument("--" + availableVisitors[3]).action(Arguments.storeTrue())
        .help("Check that there is no scoping errors.");
    visitorGroup.addArgument("--" + availableVisitors[4]).action(Arguments.storeTrue())
        .help("Check that there is no semantic errors.");
    visitorGroup.addArgument("--" + availableVisitors[5]).action(Arguments.storeTrue())
        .help("Create a Graphviz AST diagram in dot language.");
    visitorGroup.addArgument("--" + availableVisitors[6]).action(Arguments.storeTrue()).help(
        "Debug the Lexer and Parser. The input will run on both "
            + "and produce the equivalent of source in Toy2.");

    Namespace ns = parser.parseArgsOrFail(args);

    File fileInput = ns.get("i");
    Reader reader;

    if (fileInput == null) {
      System.out.println(
          "Paste the Toy2 source, hit Return, then Cmd-D (in MacOs) " + "or Ctrl-Z (in Windows)");
      InputStreamReader inp = new InputStreamReader(System.in);
      reader = new BufferedReader(inp);
    } else {
      reader = new StringReader(Files.readString(fileInput.toPath()));
    }

    Yylex lexer = new Yylex(reader);
    parser p = new parser(lexer);
    ProgramOP ast;
    if (Boolean.TRUE.equals(ns.getBoolean("v"))) {
      ast = (ProgramOP) p.debug_parse().value;
    } else {
      ast = (ProgramOP) p.parse().value;
    }
    StringTable st = lexer.getStringTable();

    final boolean[] chosenVisitor = new boolean[availableVisitors.length];
    for (int i = 0; i < availableVisitors.length; i++) {
      Boolean val = ns.getBoolean(availableVisitors[i]);
      chosenVisitor[i] = val != null && val;
    }

    String visitorRes;

    if (chosenVisitor[1]) {
      ast.accept(new SemanticVisitor(st));
      ast.accept(new ScopingVisitor(st));
      ast.accept(new TypeCheckVisitor(st));
      visitorRes = TypeCheckVisitor.SUCCESS;
    } else if (chosenVisitor[2]) {
      ast.accept(new SemanticVisitor(st));
      ast.accept(new ScopingVisitor(st));
      visitorRes = ast.accept(new GraphvizScopeTablesVisitor(st));
    } else if (chosenVisitor[3]) {
      ast.accept(new SemanticVisitor(st));
      ast.accept(new ScopingVisitor(st));
      visitorRes = ScopingVisitor.SUCCESS;

    } else if (chosenVisitor[4]) {
      ast.accept(new SemanticVisitor(st));
      visitorRes = SemanticVisitor.SUCCESS;
    } else if (chosenVisitor[5]) {
      visitorRes = ast.accept(new GraphvizASTVisitor(st));
    } else if (chosenVisitor[6]) {
      visitorRes = ast.accept(new DebugVisitor(st));
    } else {
      ast.accept(new SemanticVisitor(st));
      ast.accept(new ScopingVisitor(st));
      ast.accept(new TypeCheckVisitor(st));
      visitorRes = ast.accept(new GenCVisitor(st));
    }

    File fileOutput = ns.get("o");

    if (fileOutput == null) {
      System.out.println(visitorRes);
    } else {
      try (FileWriter fileWriter = new FileWriter(fileOutput)) {
        fileWriter.write(visitorRes);
        if (chosenVisitor[2] || chosenVisitor[5]) {
          System.out.println(
              "dot -Tsvg \"" + fileOutput.getAbsolutePath() + "\" -o " + fileOutput.getName()
                  .split("\\.")[0] + ".svg");
        }
      }
    }
  }
}
