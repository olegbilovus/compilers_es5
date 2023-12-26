package main.esercitazione5;
import java_cup.runtime.Symbol;

%%

%public
%unicode
%cup
%line
%column

%{
  private StringBuffer stringBuff = new StringBuffer();
  
    private StringTable stringTable = new StringTable();
    public StringTable getStringTable(){
      return stringTable;
    }
  
    private Symbol symbol(int type) {
      return new Symbol(type);
    }
  
    private Symbol symbol(int type, Object value) {
      return new Symbol(type, value);
    }
  
    private Symbol installID(int type, String key){
      stringTable.put(key);
      return symbol(type, stringTable.get(key));
    }
  
    private String errorMsg(String ch){
      return "'" + ch + "'" + "@" + yyline + ":" + yycolumn;
    }
%}


LineTerminator = \r|\n|\r\n
WhiteSpace     = {LineTerminator} | [ \t\f]
Identifier = [:jletter:][:jletterdigit:]*
Digit = [:digit:]
Digits = {Digit}+

%state STRING
%state COMMENT

%%

<YYINITIAL> {WhiteSpace}         { /* ignore */ }

/* keywords */
<YYINITIAL> {
  "var"                          { return symbol(sym.VAR); }
  "true"                         { return symbol(sym.TRUE); }
  "false"                        { return symbol(sym.FALSE); }
  "real"                         { return symbol(sym.REAL); }
  "integer"                      { return symbol(sym.INTEGER); }
  "string"                       { return symbol(sym.STRING); }
  "boolean"                      { return symbol(sym.BOOLEAN); }
  "return"                       { return symbol(sym.RETURN); }
  "func"                         { return symbol(sym.FUNCTION); }
  "endfunc"                      { return symbol(sym.ENDFUNCTION); }
  "proc"                         { return symbol(sym.PROCEDURE); }
  "endproc"                      { return symbol(sym.ENDPROCEDURE); }
  "out"                          { return symbol(sym.OUT); }
  "if"                           { return symbol(sym.IF); }
  "then"                         { return symbol(sym.THEN); }
  "else"                         { return symbol(sym.ELSE); }
  "endif"                        { return symbol(sym.ENDIF); }
  "elseif"                       { return symbol(sym.ELIF); }
  "while"                        { return symbol(sym.WHILE); }
  "do"                           { return symbol(sym.DO); }
  "endwhile"                     { return symbol(sym.ENDWHILE); }
}

/* tokens */
<YYINITIAL> {
  \$                             { return symbol(sym.DOLLARSIGN); }
  \\                             { return symbol(sym.ENDVAR); }
  "@"                            { return symbol(sym.REF); }
  ":"                            { return symbol(sym.COLON); }
  "^="                           { return symbol(sym.ASSIGN); }
  ";"                            { return symbol(sym.SEMI); }
  {Identifier}                   { return installID(sym.ID, yytext()); }
  ","                            { return symbol(sym.COMMA); }
  {Digits}\.{Digits}             { return symbol(sym.REAL_CONST, yytext()); }
  {Digits}                       { return symbol(sym.INTEGER_CONST, yytext()); }
  // String begin
  \"                             { yybegin(STRING);
                                   stringBuff.setLength(0); }
// Comment begin
  "%"                            { yybegin(COMMENT); }
  "->"                           { return symbol(sym.TYPERETURN); }
  "("                            { return symbol(sym.LPAR); }
  ")"                            { return symbol(sym.RPAR); }
  "-->"                          { return symbol(sym.WRITE); }
  "-->!"                         { return symbol(sym.WRITERETURN); }
  "<--"                          { return symbol(sym.READ); }
  "+"                            { return symbol(sym.PLUS); }
  "-"                            { return symbol(sym.MINUS); }
  "*"                            { return symbol(sym.TIMES); }
  "/"                            { return symbol(sym.DIV); }
  "="                            { return symbol(sym.EQ); }
  "<>"                           { return symbol(sym.NE); }
  "<"                            { return symbol(sym.LT); }
  "<="                           { return symbol(sym.LE); }
  ">"                            { return symbol(sym.GT); }
  ">="                           { return symbol(sym.GE); }
  "&&"                           { return symbol(sym.AND); }
  "||"                           { return symbol(sym.OR); }
  "!"                            { return symbol(sym.NOT); }
}


<STRING> {
  \"                             { yybegin(YYINITIAL);
                                   return installID(sym.STRING_CONST,
                                     stringBuff.toString()); }
  <<EOF>>                        { return symbol(sym.error, "Constant String not completed"); }
  [^\"]+                         { stringBuff.append( yytext() ); }
}

<COMMENT> {
  "%"                            { yybegin(YYINITIAL);}
  <<EOF>>                        { return symbol(sym.error, "Comment not closed"); }
  [^%]+                          { /* ignore */ }
}


/* error fallback */
[^]                              { return symbol(sym.error, errorMsg(yytext())); }
