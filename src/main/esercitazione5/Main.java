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

    MutuallyExclusiveGroup visitorGroup =
        parser.addMutuallyExclusiveGroup("Visitor type");
    visitorGroup.addArgument("--graphviz").action(Arguments.storeTrue())
        .help("Create a Graphviz AST diagram in dot language. (Default)");
    visitorGroup.addArgument("--debug").action(Arguments.storeTrue())
        .help("Debug the Lexer and Parser. The input will run on both "
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

    Visitor<String> visitor;

    if (Boolean.TRUE.equals(ns.getBoolean("debug"))) {
      visitor = new DebugVisitor(st);
    } else {
      visitor = new GraphvizASTVisitor(st);
    }

    String visitorRes = visitor.visit(ast);
    File fileOutput = ns.get("o");

    if (fileOutput == null) {
      System.out.println(visitorRes);
    } else {
      try (FileWriter fileWriter = new FileWriter(fileOutput)) {
        fileWriter.write(visitorRes);
        System.out.println(
            "dot -Tsvg \"" + fileOutput.getAbsolutePath() + "\" -o " + fileOutput.getName()
                .split("\\.")[0] + ".svg");
      }
    }


  }
}
