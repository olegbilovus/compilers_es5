package main.esercitazione5.visitors;

import java.util.List;
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
import main.esercitazione5.ast.nodes.stat.IfOP;
import main.esercitazione5.ast.nodes.stat.ReadOP;
import main.esercitazione5.ast.nodes.stat.ReturnOP;
import main.esercitazione5.ast.nodes.stat.WhileOP;
import main.esercitazione5.ast.nodes.stat.WriteOP;

public interface Visitor<T> {

  T visit(IdNode v);

  T visit(ProgramOP v);

  T visit(VarDeclOP v);

  T visit(FunOP v);

  T visit(ProcOP v);

  T visit(ProcFunParamOP v);

  T visit(BodyOP v);

  T visit(AddOP v);

  T visit(MulOP v);

  T visit(DiffOP v);

  T visit(DivOP v);

  T visit(AndOP v);

  T visit(OrOP v);

  T visit(GTOP v);

  T visit(GEOP v);

  T visit(LTOP v);

  T visit(LEOP v);

  T visit(EQOP v);

  T visit(NEOP v);

  T visit(UminusOP v);

  T visit(NotOP v);

  T visit(IntegerConstExpr v);

  T visit(RealConstExpr v);

  T visit(StringConstExpr v);

  T visit(TrueConstExpr v);

  T visit(FalseConstExpr v);

  T visit(CallFunOP v);

  T visit(CallProcOP v);

  T visit(IdNodeExpr v);

  T visit(ReturnOP v);

  T visit(AssignOP v);

  T visit(WriteOP v);

  T visit(ReadOP v);

  T visit(WhileOP v);

  T visit(IfOP v);

  T visit(ElifOP v);

  T st(IdNode v);

  T st(Integer v);

  T st(List<IdNode> ids);

}
