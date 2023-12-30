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
import main.esercitazione5.ast.nodes.stat.Stat;
import main.esercitazione5.ast.nodes.stat.WhileOP;
import main.esercitazione5.ast.nodes.stat.WriteOP;
import main.esercitazione5.scope.ScopeEntry;
import main.esercitazione5.scope.ScopeKind;
import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.ScopeType;
import main.esercitazione5.scope.exceptions.FuncMultReturnValScopeException;
import main.esercitazione5.scope.exceptions.NotAFuncScopeException;
import main.esercitazione5.scope.exceptions.NotAProcScopeException;
import main.esercitazione5.scope.exceptions.NumArgsExprIncorrectScopeException;
import main.esercitazione5.scope.exceptions.NumAssignExprIncorrectScopeException;
import main.esercitazione5.scope.exceptions.NumReturnExprIncorrectScopeException;

public class ScopingVisitor extends Visitor<ScopeTable> {

  private final Deque<ScopeTable> stack;
  private final ReturnedNumExprVisitor returnedNumExprVisitor;
  private final DebugVisitor debugVisitor;

  public ScopingVisitor(StringTable stringTable) {
    super(stringTable);
    stack = new ArrayDeque<>();
    returnedNumExprVisitor = new ReturnedNumExprVisitor(stringTable);
    debugVisitor = new DebugVisitor(stringTable);
  }

  @Override public ScopeTable visit(IdNode v) {
    v.setScopeTable(stack.getFirst());
    // IdNode is used only for lookups. For adding to the table, other nodes have to do it
    stack.getFirst().lookup(v.getId(), stringTable);

    return null;
  }

  @Override public ScopeTable visit(ProgramOP v) {
    ScopeTable scopeTable = new ScopeTable(null);
    v.setScopeTable(scopeTable);

    nodeTableList(scopeTable, v.getVarDeclOPList());

    /* Because a depth-first visit is done, a func/proc may call another func/proc still not in the
      GLOBAL scoping table.
      For this reason, Func and Proc are added in the GLOBAL table at this depth.
     */
    if (!Utility.isListEmpty(v.getFunOPList())) {
      for (FunOP funOP : v.getFunOPList()) {
        addSignatureToGlobals(funOP, ScopeKind.FUN, funOP.getId(), scopeTable,
            funOP.getProcFunParamOPList(), funOP.getReturnTypes());
      }
    }
    if (!Utility.isListEmpty(v.getProcOPList())) {
      for (ProcOP procOP : v.getProcOPList()) {
        addSignatureToGlobals(procOP, ScopeKind.PROC, procOP.getId(), scopeTable,
            procOP.getProcFunParamOPList(), null);
      }
    }

    nodeTableList(scopeTable, v.getFunOPList());
    nodeTableList(scopeTable, v.getProcOPList());

    return scopeTable;
  }

  @Override public ScopeTable visit(VarDeclOP v) {
    v.setScopeTable(stack.getFirst());
    ScopeTable currentTable = stack.getFirst();

    if (v.getType() != null) {
      for (IdNode id : v.getIdList()) {
        ScopeEntry entry =
            new ScopeEntry(ScopeKind.VAR, new ScopeType(v.getType(), ParamAccess.IN));
        currentTable.add(id.getId(), entry, stringTable, v);
      }
    } else {
      int size = v.getIdList().size();
      for (int i = 0; i < size; i++) {
        int id = v.getIdList().get(i).getId();
        Type type = constTypeToType(v.getConstValueList().get(i).constType());
        ScopeEntry entry = new ScopeEntry(ScopeKind.VAR, new ScopeType(type, ParamAccess.IN));
        currentTable.add(id, entry, stringTable, v);
      }
    }

    return null;
  }


  @Override public ScopeTable visit(FunOP v) {
    ScopeTable scopeTable = new ScopeTable(stack.getFirst());
    v.setScopeTable(scopeTable);
    nodeTableList(scopeTable, v.getProcFunParamOPList());
    nodeTable(scopeTable, v.getBodyOP());

    // check that the ReturnOPs return the same number of Expr as in the FunOp signature
    for (Stat stat : v.getBodyOP().getStatList()) {
      if (stat instanceof ReturnOP) {
        int numExpr = stat.accept(returnedNumExprVisitor);
        if (numExpr != v.getReturnTypes().size()) {
          throw new NumReturnExprIncorrectScopeException(st(v.getId()), v.getReturnTypes().size(),
              numExpr);
        }
      }
    }

    return scopeTable;
  }

  @Override public ScopeTable visit(ProcOP v) {
    ScopeTable scopeTable = new ScopeTable(stack.getFirst());
    v.setScopeTable(scopeTable);
    nodeTableList(scopeTable, v.getProcFunParamOPList());
    nodeTable(scopeTable, v.getBodyOP());

    return scopeTable;
  }

  private void addSignatureToGlobals(Node node, ScopeKind scopeKind, IdNode id,
      ScopeTable scopeTable, List<ProcFunParamOP> procFunParamOPList, List<Type> typeList) {

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
    v.setScopeTable(stack.getFirst());
    stack.getFirst().add(v.getId().getId(),
        new ScopeEntry(ScopeKind.VAR, new ScopeType(v.getType(), v.getParamAccess())), stringTable,
        v);

    return null;
  }

  @Override public ScopeTable visit(BodyOP v) {
    v.setScopeTable(stack.getFirst());
    // no need to create a table for BodyOP because entries need to be added to the parent table
    visitNodeList(v.getVarDeclOPList());
    visitNodeList(v.getStatList());

    return null;
  }

  private ScopeTable binaryOP(Expr v) {
    v.setScopeTable(stack.getFirst());
    visitNode(v.getExprLeft());
    visitNode(v.getExprRight());

    checkFuncMultReturnVal(v.getExprLeft(), v);
    checkFuncMultReturnVal(v.getExprRight(), v);

    return null;
  }

  private void checkFuncMultReturnVal(Expr expr, Node v) {
    if (expr instanceof CallFunOP callFunOP) {
      List<Type> typeReturnList =
          v.getScopeTable().lookup(callFunOP.getId().getId(), stringTable).getListType2();
      if (!Utility.isListEmpty(typeReturnList) && typeReturnList.size() != 1) {
        throw new FuncMultReturnValScopeException(st(callFunOP.getId()), v.accept(debugVisitor),
            typeReturnList.size());
      }
    }
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
    v.setScopeTable(stack.getFirst());
    visitNode(v.getExprLeft());

    checkFuncMultReturnVal(v.getExprLeft(), v);

    return null;
  }

  @Override public ScopeTable visit(NotOP v) {
    v.setScopeTable(stack.getFirst());
    visitNode(v.getExprLeft());

    checkFuncMultReturnVal(v.getExprLeft(), v);

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
    v.setScopeTable(stack.getFirst());
    visitNodeList(v.getExprList());

    if (v.getScopeTable().lookup(v.getId().getId(), stringTable).getKind() != ScopeKind.FUN) {
      throw new NotAFuncScopeException(st(v.getId()), v.accept(debugVisitor));
    }

    checkCallFunStats(v.getExprList(), v.getId().getId(), v);

    return null;
  }

  @Override public ScopeTable visit(CallProcOP v) {
    v.setScopeTable(stack.getFirst());
    visitNodeList(v.getParams());

    if (v.getScopeTable().lookup(v.getId().getId(), stringTable).getKind() != ScopeKind.PROC) {
      throw new NotAProcScopeException(st(v.getId()), v.accept(debugVisitor));
    }

    checkCallFunStats(v.getParams(), v.getId().getId(), v);

    return null;
  }

  private void checkCallFunStats(List<Expr> exprList, int id, Node v) {
    if (!Utility.isListEmpty(exprList)) {
      int numExpr = 0;
      for (Expr expr : exprList) {
        if (expr instanceof CallFunOP callFunOP) {
          int numVarReturn = numValReturnFunc(v.getScopeTable(), callFunOP.getId().getId());
          if (numVarReturn != 1) {
            throw new FuncMultReturnValScopeException(st(callFunOP.getId()), v.accept(debugVisitor),
                numVarReturn);
          }
        }
        numExpr += expr.accept(returnedNumExprVisitor);
      }

      List<ScopeType> params = v.getScopeTable().lookup(id, stringTable).getListType1();
      int expected = Utility.isListEmpty(params) ? 0 : params.size();
      if (expected != numExpr) {
        throw new NumArgsExprIncorrectScopeException(v.accept(debugVisitor), st(id), expected,
            numExpr);
      }
    }
  }

  @Override public ScopeTable visit(IdNodeExpr v) {
    v.setScopeTable(stack.getFirst());
    visitNode(v.getId());

    return null;
  }

  @Override public ScopeTable visit(ReturnOP v) {
    v.setScopeTable(stack.getFirst());
    visitNodeList(v.getExprList());

    return null;
  }

  @Override public ScopeTable visit(AssignOP v) {
    v.setScopeTable(stack.getFirst());

    visitNodeList(v.getIdNodeList());

    visitNodeList(v.getExprList());

    int countExprReturned = 0;
    for (Expr expr : v.getExprList()) {
      countExprReturned += expr.accept(returnedNumExprVisitor);
    }

    if (v.getIdNodeList().size() != countExprReturned) {
      throw new NumAssignExprIncorrectScopeException(v.accept(debugVisitor),
          v.getIdNodeList().size(), countExprReturned);
    }

    return null;
  }

  @Override public ScopeTable visit(WriteOP v) {
    v.setScopeTable(stack.getFirst());
    visitNodeList(v.getExprList());

    return null;
  }

  @Override public ScopeTable visit(ReadOP v) {
    v.setScopeTable(stack.getFirst());
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

  private int numValReturnFunc(ScopeTable scopeTable, Integer id) {
    return scopeTable.lookup(id, stringTable).getListType2().size();
  }

  // this is added just to have every Visitor return a String
  @Override public String toString() {
    return "Scoping succeeded";
  }
}
