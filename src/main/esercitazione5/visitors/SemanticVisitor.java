package main.esercitazione5.visitors;

import java.util.List;
import java.util.Objects;
import main.esercitazione5.StringTable;
import main.esercitazione5.Utility;
import main.esercitazione5.ast.nodes.BodyOP;
import main.esercitazione5.ast.nodes.FunOP;
import main.esercitazione5.ast.nodes.IdNode;
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
import main.esercitazione5.semantic.exceptions.MissingMainProcSemanticException;
import main.esercitazione5.semantic.exceptions.MissingReturnInFuncSemanticException;
import main.esercitazione5.semantic.exceptions.NumIdsNumConstsDiffSemanticException;
import main.esercitazione5.semantic.exceptions.ReturnInProcSemanticException;

public class SemanticVisitor extends Visitor<Void> {

  private final DebugVisitor debugVisitor;

  public SemanticVisitor(StringTable stringTable) {
    super(stringTable);
    debugVisitor = new DebugVisitor(stringTable);
  }

  @Override public Void visit(IdNode v) {
    return null;
  }

  @Override public Void visit(ProgramOP v) {
    Integer mainId = stringTable.get("main");
    if (mainId == null) {
      throw new MissingMainProcSemanticException();
    }
    boolean foundMain = false;
    for (ProcOP procOP : v.getProcOPList()) {
      if (Objects.equals(procOP.getId().getId(), mainId)) {
        foundMain = true;
        break;
      }
    }
    if (!foundMain) {
      throw new MissingMainProcSemanticException();
    }

    visitNodeList(v.getVarDeclOPList());
    visitNodeList(v.getFunOPList());
    visitNodeList(v.getProcOPList());

    return null;
  }

  @Override public Void visit(VarDeclOP v) {
    if (v.getType() == null && v.getIdList().size() != v.getConstValueList().size()) {
      throw new NumIdsNumConstsDiffSemanticException(v.accept(debugVisitor));
    }
    return null;
  }

  @Override public Void visit(FunOP v) {

    List<Stat> statList = v.getBodyOP().getStatList();
    if (Utility.isListEmpty(statList)) {
      throw new MissingReturnInFuncSemanticException(st(v.getId()));
    }
    Stat lastStat = statList.get(statList.size() - 1);
    if (!(lastStat instanceof ReturnOP)) {
      throw new MissingReturnInFuncSemanticException(st(v.getId()));
    }

    return null;
  }

  @Override public Void visit(ProcOP v) {
    if (v.getBodyOP() != null && !Utility.isListEmpty(v.getBodyOP().getStatList())) {
      for (Stat stat : v.getBodyOP().getStatList()) {
        if (stat instanceof ReturnOP) {
          throw new ReturnInProcSemanticException(st(v.getId()));
        }
      }
    }
    return null;
  }

  @Override public Void visit(ProcFunParamOP v) {
    return null;
  }

  @Override public Void visit(BodyOP v) {
    return null;
  }

  @Override public Void visit(AddOP v) {
    return null;
  }

  @Override public Void visit(MulOP v) {
    return null;
  }

  @Override public Void visit(DiffOP v) {
    return null;
  }

  @Override public Void visit(DivOP v) {
    return null;
  }

  @Override public Void visit(AndOP v) {
    return null;
  }

  @Override public Void visit(OrOP v) {
    return null;
  }

  @Override public Void visit(GTOP v) {
    return null;
  }

  @Override public Void visit(GEOP v) {
    return null;
  }

  @Override public Void visit(LTOP v) {
    return null;
  }

  @Override public Void visit(LEOP v) {
    return null;
  }

  @Override public Void visit(EQOP v) {
    return null;
  }

  @Override public Void visit(NEOP v) {
    return null;
  }

  @Override public Void visit(UminusOP v) {
    return null;
  }

  @Override public Void visit(NotOP v) {
    return null;
  }

  @Override public Void visit(IntegerConstExpr v) {
    return null;
  }

  @Override public Void visit(RealConstExpr v) {
    return null;
  }

  @Override public Void visit(StringConstExpr v) {
    return null;
  }

  @Override public Void visit(TrueConstExpr v) {
    return null;
  }

  @Override public Void visit(FalseConstExpr v) {
    return null;
  }

  @Override public Void visit(CallFunOP v) {
    return null;
  }

  @Override public Void visit(CallProcOP v) {
    return null;
  }

  @Override public Void visit(IdNodeExpr v) {
    return null;
  }

  @Override public Void visit(ReturnOP v) {
    return null;
  }

  @Override public Void visit(AssignOP v) {
    return null;
  }

  @Override public Void visit(WriteOP v) {
    return null;
  }

  @Override public Void visit(ReadOP v) {
    return null;
  }

  @Override public Void visit(WhileOP v) {
    return null;
  }

  @Override public Void visit(IfOP v) {
    return null;
  }

  @Override public Void visit(ElifOP v) {
    return null;
  }

  @Override public Void visit(ElseOP v) {
    return null;
  }

  private <T extends Node> void visitNodeList(List<T> nodeList) {
    if (!Utility.isListEmpty(nodeList)) {
      for (Node node : nodeList) {
        node.accept(this);
      }
    }
  }

  // this is added just to have every Visitor return a String
  public static final String SUCCESS = "Semantic succeeded";
}
