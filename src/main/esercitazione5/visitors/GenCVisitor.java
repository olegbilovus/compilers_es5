package main.esercitazione5.visitors;

import java.util.List;
import main.esercitazione5.StringTable;
import main.esercitazione5.Utility;
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
    toReturn.append("#include <stdio.h>\n#include <stdlib.h>\n#include <string.h>\n\n");
    toReturn.append(STRUCTS).append("\n\n");

    toReturn.append("// VAR DECLS\n");
    genList(toReturn, v.getVarDeclOPList(), "");
    toReturn.append('\n');

    // prototypes
    toReturn.append("// PROTOTYPES\n");
    for (ProcOP procOP : v.getProcOPList()) {
      genPrototype(toReturn, null, procOP.getId(), procOP.getProcFunParamOPList());
      toReturn.append("\n");
    }

    for (FunOP funOP : v.getFunOPList()) {
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
    }
    return "F_" + toyTypeToCType(returnTypes.get(0));

  }

  @Override public String visit(VarDeclOP v) {
    StringBuilder toReturn = new StringBuilder();

    List<Type> types = v.getTypeList();
    List<IdNode> ids = v.getIdList();

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
        if (types.get(i) == Type.STRING) {
          toReturn.append(getStringConst(constValues.get(i)));
        } else {
          toReturn.append(constValues.get(i));
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
    if (v.getParamAccess() == ParamAccess.OUT) {
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
    return binaryOP(v, "=");
  }

  @Override public String visit(NEOP v) {
    return binaryOP(v, "<>");
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

    genList(toReturn, v.getParams(), COMMA_SEP);
    toReturn.append(");");

    return toReturn.toString();
  }

  @Override public String visit(IdNode v) {
    return Boolean.TRUE.equals(v.isRef()) ? "@" + st(v.getId()) : st(v.getId());
  }

  @Override public String visit(ReturnOP v) {
    StringBuilder toReturn = new StringBuilder("return ");

    genList(toReturn, v.getExprList(), COMMA_SEP);
    toReturn.append(";");

    return toReturn.toString();
  }

  @Override public String visit(AssignOP v) {
    StringBuilder toReturn = new StringBuilder();

    genList(toReturn, v.getIdNodeList(), COMMA_SEP);
    toReturn.append(" ^= ");

    genList(toReturn, v.getExprList(), COMMA_SEP);
    toReturn.append(";");

    return toReturn.toString();
  }

  @Override public String visit(WriteOP v) {
    StringBuilder toReturn = new StringBuilder();
    if (Boolean.TRUE.equals(v.hasNewline())) {
      toReturn.append("-->! ");
    } else {
      toReturn.append("--> ");
    }

    if (!Utility.isListEmpty(v.getExprList())) {
      for (Expr e : v.getExprList()) {
        if (e instanceof StringConstExpr || (e instanceof AddOP
            && e.getExprRight() instanceof StringConstExpr)) {
          toReturn.append(e.accept(this));
        } else {
          toReturn.append("$(").append(e.accept(this)).append(")");
        }
      }
    }

    toReturn.append(";");

    return toReturn.toString();
  }

  @Override public String visit(ReadOP v) {
    StringBuilder toReturn = new StringBuilder("<--");

    if (!Utility.isListEmpty(v.getExprList())) {
      for (Expr e : v.getExprList()) {
        if (e instanceof StringConstExpr || (e instanceof AddOP
            && e.getExprRight() instanceof StringConstExpr)) {
          toReturn.append(e.accept(this));
        } else {
          toReturn.append("$(").append(e.accept(this)).append(")");
        }
      }
    }

    toReturn.append(";");

    return toReturn.toString();
  }

  @Override public String visit(WhileOP v) {
    StringBuilder toReturn = new StringBuilder("while ");
    toReturn.append(v.getCondition().accept(this)).append(" do\n");

    genNode(toReturn, v.getBody());
    toReturn.append("endwhile;");

    return toReturn.toString();
  }

  @Override public String visit(IfOP v) {
    StringBuilder toReturn = new StringBuilder("if ");
    toReturn.append(v.getCondition().accept(this)).append(" then\n");

    genNode(toReturn, v.getBody());
    genList(toReturn, v.getElifOPList(), "");
    genNode(toReturn, v.getElse());
    toReturn.append("endif;");

    return toReturn.toString();
  }

  @Override public String visit(ElifOP v) {
    StringBuilder toReturn = new StringBuilder("elseif ");
    toReturn.append(v.getCondition().accept(this)).append(" then\n");

    genNode(toReturn, v.getBody());

    return toReturn.toString();
  }

  @Override public String visit(ElseOP v) {
    StringBuilder toReturn = new StringBuilder("else\n ");

    genNode(toReturn, v.getBody());

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
      case INTEGER, BOOLEAN -> "int";
      case REAL -> "double";
      case STRING -> "char *";
    };
  }

  private static final String STRUCTS = """
      typedef struct F_int{
      int v;
      struct F_int * n_int;
      struct F_double * n_double;
      struct F_char * n_char;
      } F_int;
            
      typedef struct F_double{
      double v;
      struct F_int * n_int;
      struct F_double * n_double;
      struct F_char * n_char;
      } F_double;
            
      typedef struct F_char{
      char * v;
      struct F_int * n_int;
      struct F_double * n_double;
      struct F_char * n_char;
      } F_char;
      """;
}
