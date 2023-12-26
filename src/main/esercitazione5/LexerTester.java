package main.esercitazione5;

import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java_cup.runtime.Symbol;

public class LexerTester {

  public static void main(String[] args)
      throws Exception {
    String filePath =
        args[0] + File.separator + "src" + File.separator + "test_files" + File.separator + args[1];
    StringReader in = new StringReader(Files.readString(Paths.get(filePath)));
    Yylex lexer = new Yylex(in);

    System.out.println("\nTokens:");
    Symbol token;
    while ((token = lexer.next_token()).sym != sym.EOF) {
      System.out.print(tokenToString(token) + " ");
    }
    System.out.println("<" + sym.terminalNames[sym.EOF] + ">");

    System.out.println("\nString table:\n" + lexer.getStringTable());

    System.exit(0);
  }

  public static String tokenToString(Symbol symbol) {
    if (symbol.value != null) {
      return "<" + sym.terminalNames[symbol.sym] + "," + symbol.value + ">";
    }
    return "<" + sym.terminalNames[symbol.sym] + ">";
  }
}
