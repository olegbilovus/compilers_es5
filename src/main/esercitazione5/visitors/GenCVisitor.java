package main.esercitazione5.visitors;

import java.util.ArrayList;
import java.util.List;
import main.esercitazione5.StringTable;
import main.esercitazione5.Utility;
import main.esercitazione5.ast.Const;
import main.esercitazione5.ast.ConstValue;
import main.esercitazione5.ast.ParamAccess;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.BodyOP;
import main.esercitazione5.ast.nodes.FunOP;
import main.esercitazione5.ast.nodes.Node;
import main.esercitazione5.ast.nodes.ProcFunParamOP;
import main.esercitazione5.ast.nodes.ProcOP;
import main.esercitazione5.ast.nodes.ProgramOP;
import main.esercitazione5.ast.nodes.VarDeclOP;
import main.esercitazione5.ast.nodes.expr.AddOP;
import main.esercitazione5.ast.nodes.expr.AndOP;
import main.esercitazione5.ast.nodes.expr.CallFunOP;
import main.esercitazione5.ast.nodes.expr.DiffOP;
import main.esercitazione5.ast.nodes.expr.DivOP;
import main.esercitazione5.ast.nodes.expr.EQOP;
import main.esercitazione5.ast.nodes.expr.Expr;
import main.esercitazione5.ast.nodes.expr.FalseConstExpr;
import main.esercitazione5.ast.nodes.expr.GEOP;
import main.esercitazione5.ast.nodes.expr.GTOP;
import main.esercitazione5.ast.nodes.expr.IdNode;
import main.esercitazione5.ast.nodes.expr.IntegerConstExpr;
import main.esercitazione5.ast.nodes.expr.LEOP;
import main.esercitazione5.ast.nodes.expr.LTOP;
import main.esercitazione5.ast.nodes.expr.MulOP;
import main.esercitazione5.ast.nodes.expr.NEOP;
import main.esercitazione5.ast.nodes.expr.NotOP;
import main.esercitazione5.ast.nodes.expr.OrOP;
import main.esercitazione5.ast.nodes.expr.RealConstExpr;
import main.esercitazione5.ast.nodes.expr.StringConstExpr;
import main.esercitazione5.ast.nodes.expr.TrueConstExpr;
import main.esercitazione5.ast.nodes.expr.UminusOP;
import main.esercitazione5.ast.nodes.stat.AssignOP;
import main.esercitazione5.ast.nodes.stat.CallProcOP;
import main.esercitazione5.ast.nodes.stat.ElifOP;
import main.esercitazione5.ast.nodes.stat.ElseOP;
import main.esercitazione5.ast.nodes.stat.IfOP;
import main.esercitazione5.ast.nodes.stat.ReadOP;
import main.esercitazione5.ast.nodes.stat.ReturnOP;
import main.esercitazione5.ast.nodes.stat.WhileOP;
import main.esercitazione5.ast.nodes.stat.WriteOP;

public class GenCVisitor extends Visitor<String> {

  private static final String COMMA_SEP = ", ";

  public GenCVisitor(StringTable stringTable) {
    super(stringTable);
  }

  @Override public String visit(ProgramOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(
        "#include <stdio.h>\n#include <stdlib.h>\n#include <string.h>\n#include <stdbool.h>\n\n");
    toReturn.append(STRUCTS).append("\n\n");
    toReturn.append(STRCAT).append("\n").append(SCANF_STRING).append("\n").append(RETURN)
        .append("\n\n");

    toReturn.append("// VAR DECLS\n");
    genList(toReturn, v.getVarDeclOPList(), "");
    toReturn.append('\n');

    // prototypes
    toReturn.append("// PROTOTYPES\n");
    for (ProcOP procOP : v.getProcOPList()) {
      changeStringKeywordC(procOP.getId());
      genPrototype(toReturn, null, procOP.getId(), procOP.getProcFunParamOPList());
      toReturn.append("\n");
    }

    for (FunOP funOP : v.getFunOPList()) {
      changeStringKeywordC(funOP.getId());
      genPrototype(toReturn, funOP.getReturnTypes(), funOP.getId(), funOP.getProcFunParamOPList());
      toReturn.append("\n");
    }
    toReturn.append("\n");
    // end prototypes

    toReturn.append("// PROCEDURES\n");
    genList(toReturn, v.getProcOPList(), "\n\n");
    toReturn.append('\n');
    toReturn.append("// FUNCTIONS\n");
    genList(toReturn, v.getFunOPList(), "\n\n");

    return toReturn.toString();
  }

  private void genPrototype(StringBuilder toReturn, List<Type> returnTypes, IdNode id,
      List<ProcFunParamOP> paramOPList) {

    toReturn.append(returnTypeC(returnTypes)).append(" ").append(st(id)).append("(");
    genList(toReturn, paramOPList, COMMA_SEP);
    toReturn.append(");");
  }

  // get the name of the returned type by a function/proc in C
  private String returnTypeC(List<Type> returnTypes) {
    if (Utility.isListEmpty(returnTypes)) {
      return "void";
    } else if (returnTypes.size() == 1) {
      return toyTypeToCType(returnTypes.get(0));
    }
    return "F_return";

  }

  @Override public String visit(VarDeclOP v) {
    StringBuilder toReturn = new StringBuilder();

    List<Type> types = v.getTypeList();
    List<IdNode> ids = v.getIdList();
    for (IdNode idNode : ids) {
      changeStringKeywordC(idNode);
    }

    // declaration only
    if (types.size() == 1 && Utility.isListEmpty(v.getConstValueList())) {
      toReturn.append(toyTypeToCType(types.get(0))).append(" ");
      for (IdNode idNode : ids) {
        toReturn.append(st(idNode)).append(COMMA_SEP);
      }
      Utility.deleteLastCommaSpace(toReturn);
      toReturn.append(";\n");
    } else
    // initializations only
    {
      List<String> constValues = v.getConstValueList().stream().map(ConstValue::value).toList();
      for (int i = 0; i < v.getTypeList().size(); i++) {
        toReturn.append(toyTypeToCType(types.get(i))).append(" ").append(st(ids.get(i)))
            .append(" = ");
        switch (types.get(i)) {
          case STRING -> toReturn.append(getStringConst(constValues.get(i)));
          case BOOLEAN -> {
            if (v.getConstValueList().get(i).constType() == Const.TRUE) {
              toReturn.append(visit(new TrueConstExpr(null)));
            } else {
              toReturn.append(visit(new FalseConstExpr(null)));
            }
          }
          default -> toReturn.append(constValues.get(i));
        }
        toReturn.append(";\n");
      }
    }

    return toReturn.toString();
  }

  // String arg because the id of a const string is saved as a String
  private String getStringConst(String id) {
    return '"' + st(Integer.valueOf(id)) + '"';
  }

  @Override public String visit(FunOP v) {
    StringBuilder toReturn = new StringBuilder();
    genFunctionC(toReturn, v.getProcFunParamOPList(), v.getReturnTypes(), v.getId(), v.getBodyOP());

    return toReturn.toString();
  }

  @Override public String visit(ProcOP v) {
    StringBuilder toReturn = new StringBuilder();
    genFunctionC(toReturn, v.getProcFunParamOPList(), null, v.getId(), v.getBodyOP());

    return toReturn.toString();
  }

  private void genFunctionC(StringBuilder toReturn, List<ProcFunParamOP> paramOPList,
      List<Type> returnTypes, IdNode id, BodyOP bodyOP) {
    // signature
    toReturn.append(returnTypeC(returnTypes));
    toReturn.append(" ").append(st(id)).append("(");
    genList(toReturn, paramOPList, COMMA_SEP);
    toReturn.append("){\n");

    genNode(toReturn, bodyOP);

    toReturn.append("}");
  }

  @Override public String visit(ProcFunParamOP v) {
    StringBuilder toReturn = new StringBuilder();

    toReturn.append(toyTypeToCType(v.getType())).append(" ");
    if (v.getParamAccess() == ParamAccess.OUT && v.getType() != Type.STRING) {
      toReturn.append("* ");
    }
    toReturn.append(st(v.getId()));

    return toReturn.toString();
  }

  @Override public String visit(BodyOP v) {
    StringBuilder toReturn = new StringBuilder();

    genList(toReturn, v.getVarDeclOPList(), "\n");
    genList(toReturn, v.getStatList(), "\n");

    return toReturn.toString();
  }

  private String binaryOP(Expr v, String sym) {
    StringBuilder toReturn = new StringBuilder();

    if (Boolean.TRUE.equals(v.isInPar())) {
      toReturn.append("(");
    }

    genNode(toReturn, v.getExprLeft());
    toReturn.append(" ").append(sym).append(" ");
    genNode(toReturn, v.getExprRight());

    if (Boolean.TRUE.equals(v.isInPar())) {
      toReturn.append(")");
    }

    return toReturn.toString();
  }

  @Override public String visit(AddOP v) {
    if (v.getNodeType() == Type.STRING) {
      return "_strcat(" + v.getExprLeft().accept(this) + ", " + v.getExprRight().accept(this) + ")";
    }
    return binaryOP(v, "+");
  }

  @Override public String visit(MulOP v) {
    return binaryOP(v, "*");
  }

  @Override public String visit(DiffOP v) {
    return binaryOP(v, "-");
  }

  @Override public String visit(DivOP v) {
    return binaryOP(v, "/");
  }

  @Override public String visit(AndOP v) {
    return binaryOP(v, "&&");
  }

  @Override public String visit(OrOP v) {
    return binaryOP(v, "||");
  }

  @Override public String visit(GTOP v) {
    return binaryOP(v, ">");
  }

  @Override public String visit(GEOP v) {
    return binaryOP(v, ">=");
  }

  @Override public String visit(LTOP v) {
    return binaryOP(v, "<");
  }

  @Override public String visit(LEOP v) {
    return binaryOP(v, "<=");
  }

  @Override public String visit(EQOP v) {
    if (v.getExprLeft().getNodeType() == v.getExprRight().getNodeType()
        && v.getExprLeft().getNodeType() == Type.STRING) {
      return "strcmp(" + v.getExprLeft().accept(this) + ", " + v.getExprRight().accept(this)
          + ") == 0";
    }
    return binaryOP(v, "==");
  }

  @Override public String visit(NEOP v) {
    if (v.getExprLeft().getNodeType() == v.getExprRight().getNodeType()
        && v.getExprLeft().getNodeType() == Type.STRING) {
      return "strcmp(" + v.getExprLeft().accept(this) + ", " + v.getExprRight().accept(this)
          + ") != 0";
    }
    return binaryOP(v, "!=");
  }

  @Override public String visit(UminusOP v) {
    return "-" + v.getExprLeft().accept(this);
  }

  @Override public String visit(NotOP v) {
    return "!" + v.getExprLeft().accept(this);
  }

  @Override public String visit(IntegerConstExpr v) {
    return v.getConstValue().value();
  }

  @Override public String visit(RealConstExpr v) {
    return v.getConstValue().value();
  }

  @Override public String visit(StringConstExpr v) {
    return '"' + st(Integer.valueOf(v.getConstValue().value())) + '"';
  }

  @Override public String visit(TrueConstExpr v) {
    return "true";
  }

  @Override public String visit(FalseConstExpr v) {
    return "false";
  }

  @Override public String visit(CallFunOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(st(v.getId())).append("(");

    genList(toReturn, v.getExprList(), COMMA_SEP);
    toReturn.append(")");

    return toReturn.toString();
  }

  @Override public String visit(CallProcOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(st(v.getId())).append("(");
    // if the param is OUT, the & is added in the IdNode visit
    genList(toReturn, v.getParams(), COMMA_SEP);
    toReturn.append(");");

    return toReturn.toString();
  }

  @Override public String visit(IdNode v) {
    return Boolean.TRUE.equals(v.isRef()) && v.getNodeType() != Type.STRING
        && v.getNodeType() != Type.BOOLEAN ? "&" + st(v.getId()) : st(v.getId());
  }

  @Override public String visit(ReturnOP v) {
    StringBuilder toReturn = new StringBuilder();

    int exprSize = v.getExprList().size();
    List<Expr> exprs = v.getExprList();
    if (exprSize == 1) {
      toReturn.append("return ");
      genNode(toReturn, exprs.get(0));
      toReturn.append(";");
    } else {
      // temp variables closed in an inner scope
      toReturn.append("// return\n{\n");
      StringBuilder returnsArray = new StringBuilder("{");
      for (int i = 0; i < exprSize; i++) {
        toReturn.append(toyTypeToCType(exprs.get(i).getNodeType())).append(" $t").append(i)
            .append(" = ").append(exprs.get(i).accept(this)).append(" ;\n");

        toReturn.append("F_return $r").append(i).append(" = { ");
        if (exprs.get(i).getNodeType() != Type.STRING) {
          toReturn.append("&");
        }
        toReturn.append("$t").append(i).append(" };\n");
        returnsArray.append("$r").append(i).append(", ");
      }
      Utility.deleteLastCommaSpace(returnsArray);
      returnsArray.append("}");

      toReturn.append("return f_return((F_return[])").append(returnsArray).append(", ")
          .append(exprSize).append(");\n");
      toReturn.append("}\n");
    }

    return toReturn.toString();
  }

  @Override public String visit(AssignOP v) {
    StringBuilder toReturn = new StringBuilder();
    List<IdNode> ids = v.getIdNodeList();
    List<Expr> exprs = v.getExprList();

    for (int i = 0, e = 0; i < ids.size(); i++, e++) {
      if (exprs.get(e) instanceof CallFunOP callFunOP) {
        int returns = callFunOP.getTypeList().size();
        assignMultiReturnFunc(toReturn, ids.subList(i, i + returns), callFunOP);
        i += returns;
      } else {
        Type idNodeType = ids.get(i).getNodeType();
        if (v.getScopeTable().lookup(ids.get(i).getId(), stringTable).getType().paramAccess()
            == ParamAccess.OUT && idNodeType != Type.STRING && idNodeType != Type.BOOLEAN) {
          toReturn.append("*");
        }
        toReturn.append(ids.get(i).accept(this)).append(" = ").append(exprs.get(i).accept(this))
            .append(";\n");
      }
    }

    return toReturn.toString();
  }

  private void assignMultiReturnFunc(StringBuilder toReturn, List<IdNode> ids,
      CallFunOP callFunOP) {
    // inner block for temp variables
    toReturn.append("// assign multi return function\n{\n").append("F_return $r = ")
        .append(callFunOP.accept(this)).append(";\n");

    for (int i = 0; i < ids.size(); i++) {
      toReturn.append(ids.get(i).accept(this)).append(" = ")
          .append(castVoidPointer(ids.get(i).getNodeType())).append("$r.")
          .append("next->".repeat(i)).append("val;\n");
    }
    toReturn.append("}\n");

  }

  private String castVoidPointer(Type type) {
    return switch (type) {
      case INTEGER -> "*(int *)";
      case REAL -> "*(double *)";
      case BOOLEAN -> "(bool *)";
      case STRING -> "(char *)";
    };
  }

  @Override public String visit(WriteOP v) {
    StringBuilder toReturn = new StringBuilder("printf(");
    StringBuilder placeHolders = new StringBuilder();
    StringBuilder exprs = new StringBuilder();

    if (!Utility.isListEmpty(v.getExprList())) {
      for (Expr e : v.getExprList()) {
        placeHolders.append(typePlaceholder(e.getNodeType()));
        exprs.append(e.accept(this)).append(COMMA_SEP);
      }
      Utility.deleteLastCommaSpace(exprs);
    }
    if (Boolean.TRUE.equals(v.hasNewline())) {
      placeHolders.append("\\n");
    }
    toReturn.append('"').append(placeHolders).append('"').append(COMMA_SEP);
    toReturn.append(exprs).append(");");

    return toReturn.toString();
  }

  @Override public String visit(ReadOP v) {
    StringBuilder toReturn = new StringBuilder();
    List<Type> types = new ArrayList<>();
    List<Expr> vars = new ArrayList<>();

    if (!Utility.isListEmpty(v.getExprList())) {
      for (Expr e : v.getExprList()) {
        // print the strings
        if (e instanceof StringConstExpr || (e instanceof AddOP
            && e.getNodeType() == Type.STRING)) {
          // create the prev scanf if needed
          if (!types.isEmpty()) {
            toReturn.append(scanf(types, vars));
          }
          toReturn.append("printf(\"%s\\n\", ").append(e.accept(this)).append(");\n");
        } else {
          // scanf
          if (e instanceof IdNode && e.getNodeType() == Type.STRING) {
            // create the prev scanf if needed
            if (!types.isEmpty()) {
              toReturn.append(scanf(types, vars));
            }
            // scanf a string
            toReturn.append("_scanf_string(&").append(e.accept(this)).append(");\n");
          } else {
            // scanf the other types of input
            types.add(e.getNodeType());
            vars.add(e);
          }
        }
      }
    }

    if (!types.isEmpty()) {
      toReturn.append(scanf(types, vars));
    }
    return toReturn.toString();
  }

  private String scanf(List<Type> types, List<Expr> vars) {
    String toReturn =
        "scanf(\"" + String.join("", types.stream().map(this::typePlaceholder).toList()) + "\", "
            + String.join(", ", vars.stream().map(expr -> '&' + expr.accept(this)).toList())
            + ");\n";

    types.clear();
    vars.clear();

    return toReturn;
  }

  private String typePlaceholder(Type type) {
    return switch (type) {
      case STRING -> "%s";
      case INTEGER, BOOLEAN -> "%d";
      case REAL -> "%lf";
    };
  }

  @Override public String visit(WhileOP v) {
    StringBuilder toReturn = new StringBuilder("while(");
    toReturn.append(v.getCondition().accept(this)).append("){\n");

    genNode(toReturn, v.getBody());
    toReturn.append("}");

    return toReturn.toString();
  }

  @Override public String visit(IfOP v) {
    StringBuilder toReturn = new StringBuilder("if(");
    toReturn.append(v.getCondition().accept(this)).append("){\n");

    genNode(toReturn, v.getBody());
    toReturn.append("}");
    genList(toReturn, v.getElifOPList(), "");
    genNode(toReturn, v.getElse());

    return toReturn.toString();
  }

  @Override public String visit(ElifOP v) {
    StringBuilder toReturn = new StringBuilder("else if(");
    toReturn.append(v.getCondition().accept(this)).append("){\n");

    genNode(toReturn, v.getBody());
    toReturn.append("}");

    return toReturn.toString();
  }

  @Override public String visit(ElseOP v) {
    StringBuilder toReturn = new StringBuilder("else {\n ");

    genNode(toReturn, v.getBody());
    toReturn.append("}");

    return toReturn.toString();
  }

  private <T extends Node> void genNode(StringBuilder toReturn, T node) {
    if (node != null) {
      toReturn.append(node.accept(this));
    }
  }

  private <T extends Node> void genList(StringBuilder toReturn, List<T> nodeList,
      String appendEnd) {
    if (!Utility.isListEmpty(nodeList)) {
      for (T node : nodeList) {
        toReturn.append(node.accept(this)).append(appendEnd);
      }
      if (appendEnd.equals(COMMA_SEP)) {
        Utility.deleteLastCommaSpace(toReturn);
      }
    }
  }

  private String toyTypeToCType(Type type) {
    return switch (type) {
      case INTEGER -> "int";
      case BOOLEAN -> "bool";
      case REAL -> "double";
      case STRING -> "char *";
    };
  }

  private static final String RETURN = """
      F_return f_return(F_return returns[], int len){
        for(int i = 0; i < len - 1; i++){
          returns[i].next = &returns[i + 1];
        }
        return returns[0];
      }
      """;

  private static final String STRUCTS = """
      typedef struct F_return{
        void * val;
        struct F_return *next;
      } F_return;
      """;
  private static final String STRCAT = """
      char * _strcat(char *s1, char *s2){
        char *res = malloc((strlen(s1) + strlen(s2) + 1) * sizeof(char));
        strcat(res, s1);
        strcat(res, s2);
        return res;
      }
      """;

  private static final String SCANF_STRING = """
      void _scanf_string(char **p){
        char c;
        char *str = malloc(1 * sizeof(char));
        int len = 0;
        for(; (c = getc(stdin)) != '\\n'; len++){
          str = realloc(str, sizeof(char) * len + 1);
          str[len] = c;
        }
        str = realloc(str, sizeof(char) * len);
        str[len] = '\\0';
        *p = str;
      }
      """;

  // if a string is a keyword in C, it will be replaced
  private void changeStringKeywordC(IdNode idNode) {
    String str = st(idNode);
    if (KEYWORDS_C.contains(str)) {
      stringTable.replace(idNode.getId(), "repl_" + str);
    }
  }

  // https://en.cppreference.com/w/c/keyword
  private static final List<String> KEYWORDS_C =
      List.of("alignas", "alignof", "auto", "bool", "break", "case", "char", "const", "constexpr",
          "continue", "default", "do", "double", "else", "enum", "extern", "false", "float", "for",
          "goto", "if", "inline", "int", "long", "nullptr", "register", "restrict", "return",
          "short", "signed", "sizeof", "static", "static_assert", "struct", "switch",
          "thread_local", "true", "typedef", "typeof", "typeof_unqual", "union", "unsigned", "void",
          "volatile", "while", "_Alignas", "_Alignof", "_Atomic", "_BitInt", "_Bool", "_Complex",
          "_Decimal128", "_Decimal32", "_Decimal64", "_Generic", "_Imaginary", "_Noreturn",
          "_Static_assert", "_Thread_local", /* from here keywords used for GenC */ "_scanf_string",
          "_strcat", "F_return", "f_return", "printf", "scanf", "bool", "malloc", "realloc", "getc",
          "sizeof", "strlen", "strcat");

}
