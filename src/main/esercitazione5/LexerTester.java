package main.esercitazione5;

import java.io.StringReader;
import java_cup.runtime.Symbol;

public class LexerTester {

  public static void main(String[] args)
      throws Exception {
    StringReader in = Utility.readFile(args[0], args[1]);
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
