package main.esercitazione5.visitors;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import main.esercitazione5.StringTable;
import main.esercitazione5.Utility;
import main.esercitazione5.ast.Const;
import main.esercitazione5.ast.ParamAccess;
import main.esercitazione5.ast.Type;
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
import main.esercitazione5.ast.nodes.stat.WhileOP;
import main.esercitazione5.ast.nodes.stat.WriteOP;
import main.esercitazione5.scope.ScopeEntry;
import main.esercitazione5.scope.ScopeKind;
import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.ScopeType;
import main.esercitazione5.scope.exceptions.VarDeclOPScopeException;

public class ScopingVisitor extends Visitor<ScopeTable> {

  private final Deque<ScopeTable> stack;
  private final DebugVisitor debugVisitor;

  public ScopingVisitor(StringTable stringTable) {
    super(stringTable);
    stack = new ArrayDeque<>();
    debugVisitor = new DebugVisitor(stringTable);
  }

  @Override public ScopeTable visit(IdNode v) {
    // IdNode is used only for lookups. For adding to the table, other nodes have to do it
    stack.getFirst().lookup(v.getId(), stringTable);

    return null;
  }

  @Override public ScopeTable visit(ProgramOP v) {
    ScopeTable scopeTable = new ScopeTable(null);
    v.setScopeTable(scopeTable);

    nodeTableList(scopeTable, v.getVarDeclOPList());
    nodeTableList(scopeTable, v.getFunOPList());
    nodeTableList(scopeTable, v.getProcOPList());

    return scopeTable;
  }

  @Override public ScopeTable visit(VarDeclOP v) {
    ScopeTable currentTable = stack.getFirst();

    if (v.getType() != null) {
      for (IdNode id : v.getIdList()) {
        ScopeEntry entry =
            new ScopeEntry(ScopeKind.VAR, new ScopeType(v.getType(), ParamAccess.IN));
        currentTable.add(id.getId(), entry, stringTable, v);
      }
    } else {
      if (v.getIdList().size() != v.getConstValueList().size()) {
        throw new VarDeclOPScopeException(v.accept(debugVisitor));
      } else {
        int size = v.getIdList().size();
        for (int i = 0; i < size; i++) {
          int id = v.getIdList().get(i).getId();
          Type type = constTypeToType(v.getConstValueList().get(i).constType());
          ScopeEntry entry = new ScopeEntry(ScopeKind.VAR, new ScopeType(type, ParamAccess.IN));
          currentTable.add(id, entry, stringTable, v);
        }
      }
    }

    return null;
  }


  @Override public ScopeTable visit(FunOP v) {

    addSignatureToGlobals(v, ScopeKind.FUN, v.getId(), stack.getFirst(), v.getProcFunParamOPList(),
        v.getReturnTypes());

    ScopeTable scopeTable = new ScopeTable(stack.getFirst());
    v.setScopeTable(scopeTable);
    nodeTableList(scopeTable, v.getProcFunParamOPList());
    nodeTable(scopeTable, v.getBodyOP());

    return scopeTable;
  }

  @Override public ScopeTable visit(ProcOP v) {
    addSignatureToGlobals(v, ScopeKind.PROC, v.getId(), stack.getFirst(), v.getProcFunParamOPList(),
        null);

    ScopeTable scopeTable = new ScopeTable(stack.getFirst());
    v.setScopeTable(scopeTable);
    nodeTableList(scopeTable, v.getProcFunParamOPList());
    nodeTable(scopeTable, v.getBodyOP());

    return scopeTable;
  }

  private void addSignatureToGlobals(Node node, ScopeKind scopeKind, IdNode id,
      ScopeTable scopeTable,
      List<ProcFunParamOP> procFunParamOPList, List<Type> typeList) {

    List<ScopeType> scopeTypesParam = new ArrayList<>();
    if (!Utility.isListEmpty(procFunParamOPList)) {
      for (ProcFunParamOP paramOP : procFunParamOPList) {
        scopeTypesParam.add(new ScopeType(paramOP.getType(), paramOP.getParamAccess()));
      }
    }
    ScopeEntry scopeEntry = new ScopeEntry(scopeKind, scopeTypesParam, typeList);
    scopeTable.add(id.getId(), scopeEntry, stringTable, node);

  }

  @Override public ScopeTable visit(ProcFunParamOP v) {
    stack.getFirst().add(v.getId().getId(),
        new ScopeEntry(ScopeKind.VAR, new ScopeType(v.getType(), v.getParamAccess())), stringTable,
        v);

    return null;
  }

  @Override public ScopeTable visit(BodyOP v) {
    // no need to create a table for BodyOP because entries need to be added to the parent table
    visitNodeList(v.getVarDeclOPList());
    visitNodeList(v.getStatList());

    return null;
  }

  private ScopeTable binaryOP(Expr v) {
    visitNode(v.getExprLeft());
    visitNode(v.getExprRight());

    return null;
  }

  @Override public ScopeTable visit(AddOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(MulOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(DiffOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(DivOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(AndOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(OrOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(GTOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(GEOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(LTOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(LEOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(EQOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(NEOP v) {
    return binaryOP(v);
  }

  @Override public ScopeTable visit(UminusOP v) {
    visitNode(v.getExprLeft());

    return null;
  }

  @Override public ScopeTable visit(NotOP v) {
    visitNode(v.getExprLeft());

    return null;
  }

  @Override public ScopeTable visit(IntegerConstExpr v) {
    return null;
  }

  @Override public ScopeTable visit(RealConstExpr v) {
    return null;
  }

  @Override public ScopeTable visit(StringConstExpr v) {
    return null;
  }

  @Override public ScopeTable visit(TrueConstExpr v) {
    return null;
  }

  @Override public ScopeTable visit(FalseConstExpr v) {
    return null;
  }

  @Override public ScopeTable visit(CallFunOP v) {
    visitNodeList(v.getExprList());

    return null;
  }

  @Override public ScopeTable visit(CallProcOP v) {
    visitNodeList(v.getParams());

    return null;
  }

  @Override public ScopeTable visit(IdNodeExpr v) {
    visitNode(v.getId());

    return null;
  }

  @Override public ScopeTable visit(ReturnOP v) {
    visitNodeList(v.getExprList());

    return null;
  }

  @Override public ScopeTable visit(AssignOP v) {

    visitNodeList(v.getIdNodeList());

    visitNodeList(v.getExprList());

    return null;
  }

  @Override public ScopeTable visit(WriteOP v) {
    visitNodeList(v.getExprList());

    return null;
  }

  @Override public ScopeTable visit(ReadOP v) {
    visitNodeList(v.getExprList());

    return null;
  }

  @Override public ScopeTable visit(WhileOP v) {
    v.getCondition().accept(this);

    ScopeTable scopeTable = new ScopeTable(stack.getFirst());
    v.setScopeTable(scopeTable);
    nodeTable(scopeTable, v.getBody());

    return scopeTable;
  }

  @Override public ScopeTable visit(IfOP v) {
    v.getCondition().accept(this);

    ScopeTable scopeTable = new ScopeTable(stack.getFirst());
    v.setScopeTable(scopeTable);
    nodeTable(scopeTable, v.getBody());

    // ELIF and ELSE have not the If table among their active tables
    visitNodeList(v.getElifOPList());
    visitNode(v.getElse());

    return scopeTable;
  }

  @Override public ScopeTable visit(ElifOP v) {
    v.getCondition().accept(this);

    ScopeTable scopeTable = new ScopeTable(stack.getFirst());
    v.setScopeTable(scopeTable);
    nodeTable(scopeTable, v.getBody());

    return scopeTable;
  }

  @Override public ScopeTable visit(ElseOP v) {
    ScopeTable scopeTable = new ScopeTable(stack.getFirst());
    v.setScopeTable(scopeTable);
    nodeTable(scopeTable, v.getBody());

    return scopeTable;
  }

  private Type constTypeToType(Const constType) {
    return switch (constType) {
      case REAL_CONST -> Type.REAL;
      case INTEGER_CONST -> Type.INTEGER;
      case STRING_CONST -> Type.STRING;
      case TRUE, FALSE -> Type.BOOLEAN;
    };
  }

  private <T extends Node> void nodeTable(ScopeTable scopeTable, T node) {
    stack.push(scopeTable);
    visitNode(node);
    stack.pop();
  }

  private <T extends Node> void nodeTableList(ScopeTable scopeTable, List<T> nodeList) {
    stack.push(scopeTable);
    visitNodeList(nodeList);
    stack.pop();
  }

  private <T extends Node> void visitNode(T node) {
    if (node != null) {
      node.accept(this);
    }
  }

  private <T extends Node> void visitNodeList(List<T> nodeList) {
    if (!Utility.isListEmpty(nodeList)) {
      for (Node node : nodeList) {
        node.accept(this);
      }
    }
  }

  // this is added just to have every Visitor return a String
  @Override public String toString() {
    return "Scoping succeeded";
  }
}
