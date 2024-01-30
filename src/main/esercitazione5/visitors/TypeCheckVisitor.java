package main.esercitazione5.visitors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import main.esercitazione5.StringTable;
import main.esercitazione5.Utility;
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
import main.esercitazione5.ast.nodes.stat.LetLoopOP;
import main.esercitazione5.ast.nodes.stat.OtherwiseOP;
import main.esercitazione5.ast.nodes.stat.ReadOP;
import main.esercitazione5.ast.nodes.stat.ReturnOP;
import main.esercitazione5.ast.nodes.stat.Stat;
import main.esercitazione5.ast.nodes.stat.WhenOP;
import main.esercitazione5.ast.nodes.stat.WhileOP;
import main.esercitazione5.ast.nodes.stat.WriteOP;
import main.esercitazione5.scope.ScopeEntry;
import main.esercitazione5.scope.ScopeType;
import main.esercitazione5.typecheck.exceptions.ArithmeticTypeCheckException;
import main.esercitazione5.typecheck.exceptions.CompareTypeCheckException;
import main.esercitazione5.typecheck.exceptions.ConditionNotABooleanTypeCheckException;
import main.esercitazione5.typecheck.exceptions.LogicTypeCheckException;
import main.esercitazione5.typecheck.exceptions.NotTypeCheckException;
import main.esercitazione5.typecheck.exceptions.TypeArgsExprIncorrectTypeCheckException;
import main.esercitazione5.typecheck.exceptions.UMinusTypeCheckException;

public class TypeCheckVisitor extends Visitor<List<Type>> {

  private final DebugVisitor debugVisitor;

  public TypeCheckVisitor(StringTable stringTable) {
    super(stringTable);
    debugVisitor = new DebugVisitor(stringTable);
  }

  @Override public List<Type> visit(ProgramOP v) {

    visitNodeList(v.getVarDeclOPList());
    visitNodeList(v.getFunOPList());
    visitNodeList(v.getProcOPList());

    return Collections.emptyList();
  }

  @Override public List<Type> visit(VarDeclOP v) {
    List<Type> typeList;
    if (v.getType() != null) {
      typeList = List.of(v.getType());
    } else {
      typeList = nodeTypeList(v.getIdList());
    }

    // this is set because it makes easier to write the generator of the target language later
    v.setTypeList(typeList);

    return Collections.emptyList();
  }

  @Override public List<Type> visit(FunOP v) {
    visitNodeList(v.getProcFunParamOPList());
    visitNode(v.getBodyOP());

    // check that the ReturnOPs return the same Type of Expr as in the FunOp signature
    for (Stat stat : v.getBodyOP().getStatList()) {
      if (stat instanceof ReturnOP returnOP) {
        checkSameTypeExprList(v.getReturnTypes(), returnOP.getTypeList(), null, st(v.getId()),
            returnOP);
      }
    }

    return Collections.emptyList();
  }

  @Override public List<Type> visit(ProcOP v) {
    visitNodeList(v.getProcFunParamOPList());
    visitNode(v.getBodyOP());

    return Collections.emptyList();
  }

  @Override public List<Type> visit(ProcFunParamOP v) {
    v.getId().setTypeList(List.of(v.getType()));
    v.setTypeList(List.of(v.getType()));
    return Collections.emptyList();
  }

  @Override public List<Type> visit(BodyOP v) {

    visitNodeList(v.getVarDeclOPList());
    visitNodeList(v.getStatList());

    return Collections.emptyList();
  }

  private Type binaryOPArithmetic(Expr v) {
    Type leftExprType = nodeType(v.getExprLeft()).get(0);
    Type rightExprType = nodeType(v.getExprRight()).get(0);

    Type nodeType = switch (leftExprType) {
      case INTEGER -> switch (rightExprType) {
        case INTEGER -> Type.INTEGER;
        case REAL -> Type.REAL;
        default -> null;
      };
      case REAL -> switch (rightExprType) {
        case INTEGER, REAL -> Type.REAL;
        default -> null;
      };
      default -> null;
    };

    if (nodeType == null) {
      throw new ArithmeticTypeCheckException(leftExprType, rightExprType, v.getClass(),
          v.accept(debugVisitor));
    }

    return nodeType;
  }

  @Override public List<Type> visit(AddOP v) {
    Type leftExprType = nodeType(v.getExprLeft()).get(0);
    Type rightExprType = nodeType(v.getExprRight()).get(0);

    Type nodeType;
    // String concat
    if (leftExprType == Type.STRING || rightExprType == Type.STRING) {
      nodeType = Type.STRING;
    } else {
      nodeType = binaryOPArithmetic(v);
    }

    v.setTypeList(List.of(nodeType));
    return v.getTypeList();
  }

  @Override public List<Type> visit(MulOP v) {
    Type nodeType = binaryOPArithmetic(v);
    v.setTypeList(List.of(nodeType));

    return v.getTypeList();
  }

  @Override public List<Type> visit(DiffOP v) {
    Type nodeType = binaryOPArithmetic(v);
    v.setTypeList(List.of(nodeType));

    return v.getTypeList();
  }

  @Override public List<Type> visit(DivOP v) {
    Type nodeType = binaryOPArithmetic(v);
    v.setTypeList(List.of(nodeType));

    return v.getTypeList();
  }

  private Type compareOPArithmetic(Expr v) {
    Type leftExprType = nodeType(v.getExprLeft()).get(0);
    Type rightExprType = nodeType(v.getExprRight()).get(0);

    Type nodeType = switch (leftExprType) {
      case INTEGER, REAL -> switch (rightExprType) {
        case INTEGER, REAL -> Type.BOOLEAN;
        default -> null;
      };
      default -> null;
    };

    if (nodeType == null) {
      throw new CompareTypeCheckException(leftExprType, rightExprType, v.getClass(),
          v.accept(debugVisitor));
    }

    return nodeType;
  }

  @Override public List<Type> visit(GTOP v) {
    Type nodeType = compareOPArithmetic(v);
    v.setTypeList(List.of(nodeType));

    return v.getTypeList();
  }

  @Override public List<Type> visit(GEOP v) {
    Type nodeType = compareOPArithmetic(v);
    v.setTypeList(List.of(nodeType));

    return v.getTypeList();
  }

  @Override public List<Type> visit(LTOP v) {
    Type nodeType = compareOPArithmetic(v);
    v.setTypeList(List.of(nodeType));

    return v.getTypeList();
  }

  @Override public List<Type> visit(LEOP v) {
    Type nodeType = compareOPArithmetic(v);
    v.setTypeList(List.of(nodeType));

    return v.getTypeList();
  }

  @Override public List<Type> visit(EQOP v) {
    return equalNotEqual(v);
  }

  @Override public List<Type> visit(NEOP v) {
    return equalNotEqual(v);
  }

  private List<Type> equalNotEqual(Expr v) {
    Type leftExprType = nodeType(v.getExprLeft()).get(0);
    Type rightExprType = nodeType(v.getExprRight()).get(0);

    Type nodeType;
    // String and Boolean compare
    if (leftExprType == rightExprType && (leftExprType == Type.STRING
        || leftExprType == Type.BOOLEAN)) {
      nodeType = Type.BOOLEAN;
    } else {
      nodeType = compareOPArithmetic(v);
    }

    v.setTypeList(List.of(nodeType));
    return v.getTypeList();
  }

  @Override public List<Type> visit(UminusOP v) {
    Type exprType = nodeType(v.getExprLeft()).get(0);
    Type nodeType = switch (exprType) {
      case INTEGER -> Type.INTEGER;
      case REAL -> Type.REAL;
      default -> null;
    };

    if (nodeType == null) {
      throw new UMinusTypeCheckException(exprType, v.accept(debugVisitor));
    }

    v.setTypeList(List.of(nodeType));
    return v.getTypeList();
  }

  private Type logicOP(Expr v) {
    Type leftExprType = nodeType(v.getExprLeft()).get(0);
    Type rightExprType = nodeType(v.getExprRight()).get(0);

    Type nodeType;

    if (leftExprType == rightExprType && leftExprType == Type.BOOLEAN) {
      nodeType = Type.BOOLEAN;
    } else {
      throw new LogicTypeCheckException(leftExprType, rightExprType, v.getClass(),
          v.accept(debugVisitor));
    }

    return nodeType;
  }

  @Override public List<Type> visit(AndOP v) {
    Type nodeType = logicOP(v);
    v.setTypeList(List.of(nodeType));

    return v.getTypeList();
  }

  @Override public List<Type> visit(OrOP v) {
    Type nodeType = logicOP(v);
    v.setTypeList(List.of(nodeType));

    return v.getTypeList();
  }

  @Override public List<Type> visit(NotOP v) {
    Type exprType = nodeType(v.getExprLeft()).get(0);
    Type nodeType;
    if (exprType == Type.BOOLEAN) {
      nodeType = Type.BOOLEAN;
    } else {
      throw new NotTypeCheckException(exprType, v.accept(debugVisitor));
    }

    v.setTypeList(List.of(nodeType));
    return v.getTypeList();
  }

  @Override public List<Type> visit(IntegerConstExpr v) {
    v.setTypeList(List.of(Type.INTEGER));
    return List.of(Type.INTEGER);
  }

  @Override public List<Type> visit(RealConstExpr v) {
    v.setTypeList(List.of(Type.REAL));
    return List.of(Type.REAL);
  }

  @Override public List<Type> visit(StringConstExpr v) {
    v.setTypeList(List.of(Type.STRING));
    return List.of(Type.STRING);
  }

  @Override public List<Type> visit(TrueConstExpr v) {
    v.setTypeList(List.of(Type.BOOLEAN));
    return List.of(Type.BOOLEAN);
  }

  @Override public List<Type> visit(FalseConstExpr v) {
    v.setTypeList(List.of(Type.BOOLEAN));
    return List.of(Type.BOOLEAN);
  }

  @Override public List<Type> visit(CallFunOP v) {
    List<Type> typeListExpr = nodeTypeList(v.getExprList());
    ScopeEntry fun = v.getScopeTable().lookup(v.getId().getId(), stringTable);
    List<ScopeType> paramsFun = fun.getListType1();
    List<ParamAccess> paramAccessList = paramsFun.stream().map(ScopeType::paramAccess).toList();
    List<Type> typeListFun = paramsFun.stream().map(ScopeType::type).toList();

    checkSameTypeExprList(typeListFun, typeListExpr, paramAccessList, st(v.getId()), v);

    // this node returns the Fun's return type, not the parameters
    v.setTypeList(fun.getListType2());

    return v.getTypeList();
  }

  @Override public List<Type> visit(CallProcOP v) {
    List<Type> typeListExpr = nodeTypeList(v.getParams());
    List<ScopeType> paramsProc =
        v.getScopeTable().lookup(v.getId().getId(), stringTable).getListType1();
    List<ParamAccess> paramAccessList = paramsProc.stream().map(ScopeType::paramAccess).toList();
    List<Type> typeListProc = paramsProc.stream().map(ScopeType::type).toList();

    checkSameTypeExprList(typeListProc, typeListExpr, paramAccessList, st(v.getId()), v);

    // proc has not a return
    return Collections.emptyList();
  }

  private void checkSameTypeExprList(List<Type> expectedTypes, List<Type> givenTypes,
      List<ParamAccess> paramAccessList, String where, Node v) {
    if (!Utility.isListEmpty(expectedTypes)) {
      for (int i = 0; i < expectedTypes.size(); i++) {
        Type expected = expectedTypes.get(i);
        Type given = givenTypes.get(i);
        boolean out = paramAccessList != null && paramAccessList.get(i) == ParamAccess.OUT;
        if ((expected != given && !(expected == Type.REAL && given == Type.INTEGER && !out /*
        allow int to double but no OUT */))) {
          throw new TypeArgsExprIncorrectTypeCheckException(v.accept(debugVisitor), where, i + 1,
              expected, out, given);
        }
      }
    }
  }

  @Override public List<Type> visit(IdNode v) {
    List<Type> typeList =
        List.of(v.getScopeTable().lookup(v.getId(), stringTable).getType().type());
    v.setTypeList(typeList);

    return typeList;
  }

  @Override public List<Type> visit(ReturnOP v) {
    List<Type> typeList = nodeTypeList(v.getExprList());
    v.setTypeList(typeList);

    return typeList;
  }

  @Override public List<Type> visit(AssignOP v) {
    List<Type> typeListIDs = nodeTypeList(v.getIdNodeList());
    List<Type> typeListExpr = nodeTypeList(v.getExprList());

    checkSameTypeExprList(typeListIDs, typeListExpr, null, AssignOP.class.getSimpleName(), v);

    return Collections.emptyList();
  }

  @Override public List<Type> visit(WriteOP v) {
    // this is set because it makes easier to write the generator of the target language later
    List<Type> typeList = nodeTypeList(v.getExprList());
    v.setTypeList(typeList);

    return Collections.emptyList();
  }

  @Override public List<Type> visit(ReadOP v) {
    // this is set because it makes easier to write the generator of the target language later
    List<Type> typeList = nodeTypeList(v.getExprList());
    v.setTypeList(typeList);

    return Collections.emptyList();
  }

  @Override public List<Type> visit(WhileOP v) {
    Type conditionType = nodeType(v.getCondition()).get(0);
    if (conditionType != Type.BOOLEAN) {
      throw new ConditionNotABooleanTypeCheckException(conditionType, WhileOP.class,
          v.accept(debugVisitor));
    }

    visitNode(v.getBody());

    return Collections.emptyList();
  }

  @Override public List<Type> visit(IfOP v) {
    Type conditionType = nodeType(v.getCondition()).get(0);
    if (conditionType != Type.BOOLEAN) {
      throw new ConditionNotABooleanTypeCheckException(conditionType, IfOP.class,
          v.accept(debugVisitor));
    }

    visitNode(v.getBody());
    visitNodeList(v.getElifOPList());
    visitNode(v.getElse());

    return Collections.emptyList();
  }

  @Override public List<Type> visit(ElifOP v) {
    Type conditionType = nodeType(v.getCondition()).get(0);
    if (conditionType != Type.BOOLEAN) {
      throw new ConditionNotABooleanTypeCheckException(conditionType, ElifOP.class,
          v.accept(debugVisitor));
    }

    visitNode(v.getBody());

    return Collections.emptyList();
  }

  @Override public List<Type> visit(ElseOP v) {
    visitNode(v.getBody());

    return Collections.emptyList();
  }

  @Override public List<Type> visit(LetLoopOP v) {
    visitNodeList(v.getDeclOP());
    visitNodeList(v.getWhenOPList());
    visitNode(v.getOtherwiseOP());

    return Collections.emptyList();
  }

  @Override public List<Type> visit(WhenOP v) {
    Type conditionType = nodeType(v.getCondition()).get(0);
    if (conditionType != Type.BOOLEAN) {
      throw new ConditionNotABooleanTypeCheckException(conditionType, WhenOP.class,
          v.accept(debugVisitor));
    }

    visitNode(v.getBody());

    return Collections.emptyList();
  }

  @Override public List<Type> visit(OtherwiseOP v) {
    visitNode(v.getBody());

    return Collections.emptyList();
  }

  private <T extends Node> void visitNode(T node) {
    if (node != null) {
      node.accept(this);
    }
  }

  private <T extends Node> List<Type> nodeType(T node) {
    List<Type> typeList = new ArrayList<>();
    if (node != null) {
      typeList.addAll(node.accept(this));
    }

    return typeList;
  }

  private <T extends Node> void visitNodeList(List<T> nodeList) {
    if (!Utility.isListEmpty(nodeList)) {
      for (Node node : nodeList) {
        node.accept(this);
      }
    }
  }

  private <T extends Node> List<Type> nodeTypeList(List<T> nodeList) {
    List<Type> typeList = new ArrayList<>();
    if (!Utility.isListEmpty(nodeList)) {
      for (Node node : nodeList) {
        typeList.addAll(node.accept(this));
      }
    }

    return typeList;
  }

  // this is added just to have every Visitor return a String
  public static final String SUCCESS = "TypeCheck succeeded";
}
