package main.esercitazione5.visitors;

import main.esercitazione5.StringTable;
import main.esercitazione5.ast.nodes.BodyOP;
import main.esercitazione5.ast.nodes.FunOP;
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
import main.esercitazione5.ast.nodes.stat.LetLoopOP;
import main.esercitazione5.ast.nodes.stat.OtherwiseOP;
import main.esercitazione5.ast.nodes.stat.ReadOP;
import main.esercitazione5.ast.nodes.stat.ReturnOP;
import main.esercitazione5.ast.nodes.stat.WhenOP;
import main.esercitazione5.ast.nodes.stat.WhileOP;
import main.esercitazione5.ast.nodes.stat.WriteOP;

public class ReturnedNumExprVisitor extends Visitor<Integer> {

  private static final Integer BINARY_OP = 1;

  public ReturnedNumExprVisitor(StringTable stringTable) {
    super(stringTable);
  }

  @Override public Integer visit(ProgramOP v) {
    return 0;
  }

  @Override public Integer visit(VarDeclOP v) {
    return 0;
  }

  @Override public Integer visit(FunOP v) {
    return 0;
  }

  @Override public Integer visit(ProcOP v) {
    return 0;
  }

  @Override public Integer visit(ProcFunParamOP v) {
    return 0;
  }

  @Override public Integer visit(BodyOP v) {
    return 0;
  }

  @Override public Integer visit(AddOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(MulOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(DiffOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(DivOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(AndOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(OrOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(GTOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(GEOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(LTOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(LEOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(EQOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(NEOP v) {
    return BINARY_OP;
  }

  @Override public Integer visit(UminusOP v) {
    return 1;
  }

  @Override public Integer visit(NotOP v) {
    return 1;
  }

  @Override public Integer visit(IntegerConstExpr v) {
    return 1;
  }

  @Override public Integer visit(RealConstExpr v) {
    return 1;
  }

  @Override public Integer visit(StringConstExpr v) {
    return 1;
  }

  @Override public Integer visit(TrueConstExpr v) {
    return 1;
  }

  @Override public Integer visit(FalseConstExpr v) {
    return 1;
  }

  @Override public Integer visit(CallFunOP v) {
    return v.getScopeTable().lookup(v.getId().getId(), stringTable).getListType2().size();
  }

  @Override public Integer visit(CallProcOP v) {
    return 0;
  }

  @Override public Integer visit(IdNode v) {
    return 1;
  }

  @Override public Integer visit(ReturnOP v) {
    int count = 0;
    for (Expr e : v.getExprList()) {
      count += e.accept(this);
    }

    return count;
  }

  @Override public Integer visit(AssignOP v) {
    return 0;
  }

  @Override public Integer visit(WriteOP v) {
    return 0;
  }

  @Override public Integer visit(ReadOP v) {
    return 0;
  }

  @Override public Integer visit(WhileOP v) {
    return 0;
  }

  @Override public Integer visit(IfOP v) {
    return 0;
  }

  @Override public Integer visit(ElifOP v) {
    return 0;
  }

  @Override public Integer visit(ElseOP v) {
    return 0;
  }

  @Override public Integer visit(LetLoopOP v) {
    return 0;
  }

  @Override public Integer visit(WhenOP v) {
    return 0;
  }

  @Override public Integer visit(OtherwiseOP v) {
    return 0;
  }
}
