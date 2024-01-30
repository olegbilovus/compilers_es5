package main.esercitazione5.visitors;

import java.util.List;
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

public abstract class Visitor<T> {

  protected StringTable stringTable;

  Visitor(StringTable stringTable) {
    this.stringTable = stringTable;
  }

  public abstract T visit(ProgramOP v);

  public abstract T visit(VarDeclOP v);

  public abstract T visit(FunOP v);

  public abstract T visit(ProcOP v);

  public abstract T visit(ProcFunParamOP v);

  public abstract T visit(BodyOP v);

  public abstract T visit(AddOP v);

  public abstract T visit(MulOP v);

  public abstract T visit(DiffOP v);

  public abstract T visit(DivOP v);

  public abstract T visit(AndOP v);

  public abstract T visit(OrOP v);

  public abstract T visit(GTOP v);

  public abstract T visit(GEOP v);

  public abstract T visit(LTOP v);

  public abstract T visit(LEOP v);

  public abstract T visit(EQOP v);

  public abstract T visit(NEOP v);

  public abstract T visit(UminusOP v);

  public abstract T visit(NotOP v);

  public abstract T visit(IntegerConstExpr v);

  public abstract T visit(RealConstExpr v);

  public abstract T visit(StringConstExpr v);

  public abstract T visit(TrueConstExpr v);

  public abstract T visit(FalseConstExpr v);

  public abstract T visit(CallFunOP v);

  public abstract T visit(CallProcOP v);

  public abstract T visit(IdNode v);

  public abstract T visit(ReturnOP v);

  public abstract T visit(AssignOP v);

  public abstract T visit(WriteOP v);

  public abstract T visit(ReadOP v);

  public abstract T visit(WhileOP v);

  public abstract T visit(IfOP v);

  public abstract T visit(ElifOP v);

  public abstract T visit(ElseOP v);

  public abstract T visit(LetLoopOP v);

  public abstract T visit(WhenOP v);

  public abstract T visit(OtherwiseOP v);

  public String st(IdNode id) {
    return stringTable.get(id.getId());
  }

  public String st(Integer id) {
    return stringTable.get(id);
  }

  public String st(List<IdNode> ids) {
    StringBuilder toReturn = new StringBuilder();
    for (IdNode i : ids) {
      toReturn.append(stringTable.get(i.getId()));
    }
    return toReturn.toString();
  }
}
