package main.esercitazione5.visitors;

import main.esercitazione5.StringTable;
import main.esercitazione5.Utility;
import main.esercitazione5.ast.ConstValue;
import main.esercitazione5.ast.ParamAccess;
import main.esercitazione5.ast.Type;
import main.esercitazione5.ast.nodes.BodyOP;
import main.esercitazione5.ast.nodes.FunOP;
import main.esercitazione5.ast.nodes.IdNode;
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
import main.esercitazione5.ast.nodes.expr.IdNodeExpr;
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
import main.esercitazione5.ast.nodes.stat.Stat;
import main.esercitazione5.ast.nodes.stat.WhileOP;
import main.esercitazione5.ast.nodes.stat.WriteOP;

public class DebugVisitor extends Visitor<String> {


  public DebugVisitor(StringTable stringTable) {
    super(stringTable);
  }

  @Override public String visit(IdNode v) {
    return st(v);
  }

  @Override public String visit(ProgramOP v) {
    StringBuilder toReturn = new StringBuilder();

    for (VarDeclOP vd : v.getVarDeclOPList()) {
      toReturn.append(vd.accept(this)).append('\n');
    }
    toReturn.append('\n');

    for (ProcOP p : v.getProcOPList()) {
      toReturn.append(p.accept(this)).append("\n\n");
    }

    for (FunOP f : v.getFunOPList()) {
      toReturn.append(f.accept(this)).append("\n\n");
    }
    toReturn.append('\n');

    return toReturn.toString();
  }

  @Override public String visit(VarDeclOP v) {
    StringBuilder toReturn = new StringBuilder("var ");

    for (IdNode id : v.getIdList()) {
      toReturn.append(st(id)).append(", ");
    }
    Utility.deleteLastCommaSpace(toReturn);

    if (v.getType() != null) {
      toReturn.append(": ").append(v.getType().name().toLowerCase());
    } else {
      toReturn.append(" ^= ");
      for (ConstValue cv : v.getConstValueList()) {
        switch (cv.constType()) {
          case STRING_CONST ->
              toReturn.append('"').append(st(Integer.valueOf(cv.value()))).append('"');
          case TRUE, FALSE -> toReturn.append(cv.constType().name().toLowerCase());
          default -> toReturn.append(cv.value());
        }
        toReturn.append(", ");
      }
      Utility.deleteLastCommaSpace(toReturn);
    }

    toReturn.append(";\\");

    return toReturn.toString();
  }

  @Override public String visit(FunOP v) {
    StringBuilder toReturn = new StringBuilder("func ");
    toReturn.append(st(v.getId())).append("(");

    if (!Utility.isListEmpty(v.getProcFunParamOPList())) {
      for (ProcFunParamOP p : v.getProcFunParamOPList()) {
        toReturn.append(p.accept(this)).append(", ");
      }
      Utility.deleteLastCommaSpace(toReturn);
    }
    toReturn.append(") -> ");

    for (Type t : v.getReturnTypes()) {
      toReturn.append(t.name().toLowerCase()).append(", ");
    }
    Utility.deleteLastCommaSpace(toReturn);
    toReturn.append(":\n");

    if (v.getBodyOP() != null) {
      toReturn.append(v.getBodyOP().accept(this));
    }

    toReturn.append("endfunc");

    return toReturn.toString();
  }

  @Override public String visit(ProcOP v) {
    StringBuilder toReturn = new StringBuilder("proc ");
    toReturn.append(st(v.getId())).append("(");

    if (!Utility.isListEmpty(v.getProcFunParamOPList())) {
      for (ProcFunParamOP p : v.getProcFunParamOPList()) {
        toReturn.append(p.accept(this)).append(", ");
      }
      Utility.deleteLastCommaSpace(toReturn);
    }
    toReturn.append("):\n");

    if (v.getBodyOP() != null) {
      toReturn.append(v.getBodyOP().accept(this));
    }

    toReturn.append("endproc");

    return toReturn.toString();
  }

  @Override public String visit(ProcFunParamOP v) {
    StringBuilder toReturn = new StringBuilder();

    if (v.getParamAccess() == ParamAccess.OUT) {
      toReturn.append("out ");
    }
    toReturn.append(st(v.getId())).append(": ").append(v.getType().name().toLowerCase());

    return toReturn.toString();
  }

  @Override public String visit(BodyOP v) {
    StringBuilder toReturn = new StringBuilder();

    if (!Utility.isListEmpty(v.getVarDeclOPList())) {
      for (VarDeclOP vd : v.getVarDeclOPList()) {
        toReturn.append(vd.accept(this)).append('\n');
      }
    }

    if (!Utility.isListEmpty(v.getStatList())) {
      for (Stat s : v.getStatList()) {
        toReturn.append(s.accept(this)).append('\n');
      }
    }

    return toReturn.toString();
  }

  private String binaryOP(Expr v, String sym) {
    StringBuilder toReturn = new StringBuilder();

    if (Boolean.TRUE.equals(v.isInPar())) {
      toReturn.append("(");
    }

    toReturn.append(v.getExprLeft().accept(this)).append(" ").append(sym).append(" ");
    toReturn.append(v.getExprRight().accept(this));

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

    if (!Utility.isListEmpty(v.getExprList())) {
      for (Expr e : v.getExprList()) {
        toReturn.append(e.accept(this)).append(", ");
      }
      Utility.deleteLastCommaSpace(toReturn);
    }

    toReturn.append(")");

    return toReturn.toString();
  }

  @Override public String visit(CallProcOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(st(v.getId())).append("(");

    if (!Utility.isListEmpty(v.getParams())) {
      for (Expr e : v.getParams()) {
        toReturn.append(e.accept(this)).append(", ");
      }
      Utility.deleteLastCommaSpace(toReturn);
    }

    toReturn.append(");");

    return toReturn.toString();
  }

  @Override public String visit(IdNodeExpr v) {
    return Boolean.TRUE.equals(v.isRef()) ? "@" + st(v.getId()) : st(v.getId());
  }

  @Override public String visit(ReturnOP v) {
    StringBuilder toReturn = new StringBuilder("return ");

    for (Expr e : v.getExprList()) {
      toReturn.append(e.accept(this)).append(", ");
    }
    Utility.deleteLastCommaSpace(toReturn);
    toReturn.append(";");

    return toReturn.toString();
  }

  @Override public String visit(AssignOP v) {
    StringBuilder toReturn = new StringBuilder();

    for (IdNode id : v.getIdNodeList()) {
      toReturn.append(st(id)).append(", ");
    }
    Utility.deleteLastCommaSpace(toReturn);
    toReturn.append(" ^= ");

    for (Expr e : v.getExprList()) {
      toReturn.append(e.accept(this)).append(", ");
    }
    Utility.deleteLastCommaSpace(toReturn);

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

    if (v.getBody() != null) {
      toReturn.append(v.getBody().accept(this));
    }

    toReturn.append("endwhile;");

    return toReturn.toString();
  }

  @Override public String visit(IfOP v) {
    StringBuilder toReturn = new StringBuilder("if ");
    toReturn.append(v.getCondition().accept(this)).append(" then\n");

    if (v.getBody() != null) {
      toReturn.append(v.getBody().accept(this));
    }

    if (v.getElifOPList() != null) {
      for (ElifOP elf : v.getElifOPList()) {
        toReturn.append(elf.accept(this));
      }
    }

    if (v.getElse() != null) {
      toReturn.append(v.getElse().accept(this));
    }

    toReturn.append("endif;");

    return toReturn.toString();
  }

  @Override public String visit(ElifOP v) {
    StringBuilder toReturn = new StringBuilder("elseif ");
    toReturn.append(v.getCondition().accept(this)).append(" then\n");

    if (v.getBody() != null) {
      toReturn.append(v.getBody().accept(this));
    }

    return toReturn.toString();
  }

  @Override public String visit(ElseOP v) {
    StringBuilder toReturn = new StringBuilder("else\n ");

    if (v.getBody() != null) {
      toReturn.append(v.getBody().accept(this));
    }

    return toReturn.toString();
  }
}
