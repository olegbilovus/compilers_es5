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
    return null;
  }

  @Override public ScopeTable visit(ProgramOP v) {
    ScopeTable scopeTable = new ScopeTable(null);
    v.setScopeTable(scopeTable);
    stack.push(scopeTable);

    if (!Utility.isListEmpty(v.getVarDeclOPList())) {
      for (VarDeclOP varDeclOP : v.getVarDeclOPList()) {
        varDeclOP.accept(this);
      }
    }

    if (!Utility.isListEmpty(v.getFunOPList())) {
      for (FunOP funOP : v.getFunOPList()) {
        scopeTable.getNexts().add(funOP.accept(this));
      }
    }

    if (!Utility.isListEmpty(v.getProcOPList())) {
      for (ProcOP procOP : v.getProcOPList()) {
        scopeTable.getNexts().add(procOP.accept(this));
      }
    }

    return scopeTable;
  }

  @Override public ScopeTable visit(VarDeclOP v) {
    if (v.getType() != null) {
      for (IdNode id : v.getIdList()) {
        ScopeEntry entry =
            new ScopeEntry(ScopeKind.VAR, new ScopeType(v.getType(), ParamAccess.IN));
        stack.getFirst().add(id.getId(), entry, stringTable);
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
          stack.getFirst().add(id, entry, stringTable);
        }
      }
    }

    return null;
  }

  @Override public ScopeTable visit(FunOP v) {
    // add the function to the GLOBALS
    List<ScopeType> scopeTypesParam = new ArrayList<>();
    if (!Utility.isListEmpty(v.getProcFunParamOPList())) {
      for (ProcFunParamOP paramOP : v.getProcFunParamOPList()) {
        scopeTypesParam.add(new ScopeType(paramOP.getType(), paramOP.getParamAccess()));
      }
    }
    ScopeEntry funEntry = new ScopeEntry(ScopeKind.FUN, scopeTypesParam, v.getReturnTypes());
    stack.getFirst().add(v.getId().getId(), funEntry, stringTable);

    // create new scope for the function
    ScopeTable scopeTable = new ScopeTable(stack.getFirst());
    v.setScopeTable(scopeTable);
    stack.getFirst().getNexts().add(scopeTable);
    stack.push(scopeTable);

    if (!Utility.isListEmpty(v.getProcFunParamOPList())) {
      for (ProcFunParamOP paramOP : v.getProcFunParamOPList()) {
        paramOP.accept(this);
      }
    }

    v.getBodyOP().accept(this);
    stack.pop();

    return scopeTable;
  }

  @Override public ScopeTable visit(ProcOP v) {
    // add the procedure to the GLOBALS
    List<ScopeType> scopeTypesParam = new ArrayList<>();
    if (!Utility.isListEmpty(v.getProcFunParamOPList())) {
      for (ProcFunParamOP paramOP : v.getProcFunParamOPList()) {
        scopeTypesParam.add(new ScopeType(paramOP.getType(), paramOP.getParamAccess()));
      }
    }
    ScopeEntry funEntry = new ScopeEntry(ScopeKind.PROC, scopeTypesParam, null);
    stack.getFirst().add(v.getId().getId(), funEntry, stringTable);

    // create new scope for the function
    ScopeTable scopeTable = new ScopeTable(stack.getFirst());
    v.setScopeTable(scopeTable);
    stack.getFirst().getNexts().add(scopeTable);
    stack.push(scopeTable);

    if (!Utility.isListEmpty(v.getProcFunParamOPList())) {
      for (ProcFunParamOP paramOP : v.getProcFunParamOPList()) {
        paramOP.accept(this);
      }
    }

    v.getBodyOP().accept(this);
    stack.pop();

    return scopeTable;
  }

  @Override public ScopeTable visit(ProcFunParamOP v) {
    stack.getFirst().add(v.getId().getId(),
        new ScopeEntry(ScopeKind.VAR, new ScopeType(v.getType(), v.getParamAccess())), stringTable);

    return null;
  }

  @Override public ScopeTable visit(BodyOP v) {

    if (!Utility.isListEmpty(v.getVarDeclOPList())) {
      for (VarDeclOP varDeclOP : v.getVarDeclOPList()) {
        varDeclOP.accept(this);
      }
    }

    return null;
  }

  @Override public ScopeTable visit(AddOP v) {
    return null;
  }

  @Override public ScopeTable visit(MulOP v) {
    return null;
  }

  @Override public ScopeTable visit(DiffOP v) {
    return null;
  }

  @Override public ScopeTable visit(DivOP v) {
    return null;
  }

  @Override public ScopeTable visit(AndOP v) {
    return null;
  }

  @Override public ScopeTable visit(OrOP v) {
    return null;
  }

  @Override public ScopeTable visit(GTOP v) {
    return null;
  }

  @Override public ScopeTable visit(GEOP v) {
    return null;
  }

  @Override public ScopeTable visit(LTOP v) {
    return null;
  }

  @Override public ScopeTable visit(LEOP v) {
    return null;
  }

  @Override public ScopeTable visit(EQOP v) {
    return null;
  }

  @Override public ScopeTable visit(NEOP v) {
    return null;
  }

  @Override public ScopeTable visit(UminusOP v) {
    return null;
  }

  @Override public ScopeTable visit(NotOP v) {
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
    return null;
  }

  @Override public ScopeTable visit(CallProcOP v) {
    return null;
  }

  @Override public ScopeTable visit(IdNodeExpr v) {
    return null;
  }

  @Override public ScopeTable visit(ReturnOP v) {
    return null;
  }

  @Override public ScopeTable visit(AssignOP v) {
    return null;
  }

  @Override public ScopeTable visit(WriteOP v) {
    return null;
  }

  @Override public ScopeTable visit(ReadOP v) {
    return null;
  }

  @Override public ScopeTable visit(WhileOP v) {
    return null;
  }

  @Override public ScopeTable visit(IfOP v) {
    return null;
  }

  @Override public ScopeTable visit(ElifOP v) {
    return null;
  }

  private Type constTypeToType(Const constType) {
    return switch (constType) {
      case REAL_CONST -> Type.REAL;
      case INTEGER_CONST -> Type.INTEGER;
      case STRING_CONST -> Type.STRING;
      case TRUE, FALSE -> Type.BOOLEAN;
    };
  }
}
