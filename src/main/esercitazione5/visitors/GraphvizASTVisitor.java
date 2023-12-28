package main.esercitazione5.visitors;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import main.esercitazione5.StringTable;
import main.esercitazione5.Utility;
import main.esercitazione5.ast.ConstValue;
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

public class GraphvizASTVisitor extends Visitor<String> {

  private int nodeCount = 0;
  private final Deque<Integer> stackParent;

  public GraphvizASTVisitor(StringTable stringTable) {
    super(stringTable);
    stackParent = new ArrayDeque<>();
  }

  @Override public String visit(IdNode v) {
    return st(v);
  }

  @Override public String visit(ProgramOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append("digraph {\n").append(node(ProgramOP.class));
    stackParent.push(nodeCount);

    genList(toReturn, v.getVarDeclOPList(), VarDeclOP.class);
    genList(toReturn, v.getProcOPList(), ProcOP.class);
    genList(toReturn, v.getFunOPList(), FunOP.class);
    toReturn.append("}\n");

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(VarDeclOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(VarDeclOP.class)).append(edge());
    stackParent.push(nodeCount);

    toReturn.append(node("ID", true)).append(edge());
    stackParent.push(nodeCount);
    for (IdNode id : v.getIdList()) {
      toReturn.append(node(st(id))).append(edge(stackParent.getFirst()));
    }
    stackParent.pop();

    if (v.getType() != null) {
      toReturn.append(node(v.getType().name().toLowerCase())).append(edge());
    } else {
      toReturn.append(node(ConstValue.class, true)).append(edge());
      stackParent.push(nodeCount);

      for (ConstValue cv : v.getConstValueList()) {
        switch (cv.constType()) {
          case STRING_CONST ->
              toReturn.append(node("\\\"" + st(Integer.valueOf(cv.value())) + "\\\""))
                  .append(edge());
          case TRUE, FALSE ->
              toReturn.append(node(cv.constType().name().toLowerCase())).append(edge());
          default -> toReturn.append(node(cv.value())).append(edge());
        }
      }
      stackParent.pop();
    }

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(FunOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(FunOP.class)).append(edge());
    stackParent.push(nodeCount);
    toReturn.append(node(st(v.getId()))).append(edge());

    genList(toReturn, v.getProcFunParamOPList(), ProcFunParamOP.class);

    toReturn.append(node("ReturnTypes", true)).append(edge());
    stackParent.push(nodeCount);
    for (Type t : v.getReturnTypes()) {
      toReturn.append(node(t.name().toLowerCase())).append(edge(stackParent.getFirst()));
    }
    stackParent.pop();

    genNode(toReturn, v.getBodyOP());
    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(ProcOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(ProcOP.class)).append(edge());
    stackParent.push(nodeCount);
    toReturn.append(node(st(v.getId()))).append(edge());

    genList(toReturn, v.getProcFunParamOPList(), ProcFunParamOP.class);
    genNode(toReturn, v.getBodyOP());
    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(ProcFunParamOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(ProcFunParamOP.class)).append(edge());
    stackParent.push(nodeCount);
    toReturn.append(node(v.getParamAccess().name().toLowerCase())).append(edge());
    toReturn.append(node(st(v.getId()))).append(edge());
    toReturn.append(node(v.getType().name().toLowerCase())).append(edge());
    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(BodyOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(BodyOP.class)).append(edge());
    stackParent.push(nodeCount);

    genList(toReturn, v.getVarDeclOPList(), VarDeclOP.class);
    genList(toReturn, v.getStatList(), Stat.class);

    stackParent.pop();

    return toReturn.toString();
  }

  private String binaryOP(Expr v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(v.getClass())).append(edge());
    stackParent.push(nodeCount);

    genNode(toReturn, v.getExprLeft());
    genNode(toReturn, v.getExprRight());

    stackParent.pop();


    return toReturn.toString();
  }

  @Override public String visit(AddOP v) {
    return binaryOP(v);
  }

  @Override public String visit(MulOP v) {
    return binaryOP(v);
  }

  @Override public String visit(DiffOP v) {
    return binaryOP(v);
  }

  @Override public String visit(DivOP v) {
    return binaryOP(v);
  }

  @Override public String visit(AndOP v) {
    return binaryOP(v);
  }

  @Override public String visit(OrOP v) {
    return binaryOP(v);
  }

  @Override public String visit(GTOP v) {
    return binaryOP(v);
  }

  @Override public String visit(GEOP v) {
    return binaryOP(v);
  }

  @Override public String visit(LTOP v) {
    return binaryOP(v);
  }

  @Override public String visit(LEOP v) {
    return binaryOP(v);
  }

  @Override public String visit(EQOP v) {
    return binaryOP(v);
  }

  @Override public String visit(NEOP v) {
    return binaryOP(v);
  }

  @Override public String visit(UminusOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(UminusOP.class)).append(edge());
    stackParent.push(nodeCount);

    genNode(toReturn, v.getExprLeft());

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(NotOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(NotOP.class)).append(edge());
    stackParent.push(nodeCount);

    genNode(toReturn, v.getExprLeft());

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(IntegerConstExpr v) {
    return node(v.getConstValue().value()) + edge();
  }

  @Override public String visit(RealConstExpr v) {
    return node(v.getConstValue().value()) + edge();
  }

  @Override public String visit(StringConstExpr v) {
    return node("\\\"" + st(Integer.valueOf(v.getConstValue().value())) + "\\\"") + edge();
  }

  @Override public String visit(TrueConstExpr v) {
    return node("true") + edge();
  }

  @Override public String visit(FalseConstExpr v) {
    return node("false") + edge();
  }

  @Override public String visit(CallFunOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(CallFunOP.class)).append(edge());
    stackParent.push(nodeCount);
    toReturn.append(node(st(v.getId()))).append(edge());

    genList(toReturn, v.getExprList(), Expr.class);

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(CallProcOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(CallProcOP.class)).append(edge());
    stackParent.push(nodeCount);
    toReturn.append(node(st(v.getId()))).append(edge());

    genList(toReturn, v.getParams(), Expr.class);

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(IdNodeExpr v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(IdNodeExpr.class)).append(edge());
    stackParent.push(nodeCount);
    toReturn.append(node(Boolean.TRUE.equals(v.isRef()) ? "REF" : "NOREF")).append(edge());
    toReturn.append(node(st(v.getId()))).append(edge());

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(ReturnOP v) {
    StringBuilder toReturn = new StringBuilder();
    genList(toReturn, v.getExprList(), ReturnOP.class);
    return toReturn.toString();
  }

  @Override public String visit(AssignOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(AssignOP.class)).append(edge());
    stackParent.push(nodeCount);

    toReturn.append(node("ID", true)).append(edge());
    for (IdNode id : v.getIdNodeList()) {
      toReturn.append(node(st(id))).append(edge(stackParent.getFirst() + 1));
    }

    genList(toReturn, v.getExprList(), Expr.class);

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(WriteOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(WriteOP.class)).append(edge());
    stackParent.push(nodeCount);

    if (Boolean.TRUE.equals(v.hasNewline())) {
      toReturn.append(node("TRUE")).append(edge());
    } else {
      toReturn.append(node("FALSE")).append(edge());
    }

    genList(toReturn, v.getExprList(), Expr.class);

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(ReadOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(ReadOP.class)).append(edge());
    stackParent.push(nodeCount);

    genList(toReturn, v.getExprList(), Expr.class);

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(WhileOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(WhileOP.class)).append(edge());
    stackParent.push(nodeCount);

    genNode(toReturn, v.getCondition());
    genNode(toReturn, v.getBody());

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(IfOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(IfOP.class)).append(edge());
    stackParent.push(nodeCount);

    genNode(toReturn, v.getCondition());
    genNode(toReturn, v.getBody());
    genList(toReturn, v.getElifOPList(), ElifOP.class);
    genNode(toReturn, v.getElse());

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(ElifOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(ElifOP.class)).append(edge());
    stackParent.push(nodeCount);

    genNode(toReturn, v.getCondition());
    genNode(toReturn, v.getBody());

    stackParent.pop();

    return toReturn.toString();
  }

  @Override public String visit(ElseOP v) {
    StringBuilder toReturn = new StringBuilder();
    toReturn.append(node(ElseOP.class)).append(edge());
    stackParent.push(nodeCount);

    genNode(toReturn, v.getBody());

    stackParent.pop();

    return toReturn.toString();
  }

  private String label(String label, boolean box) {
    String toReturn = " [label=\"" + label + '"';
    if (box) {
      toReturn += " shape=\"box\"";
    }

    return toReturn + "]\n";
  }

  private String node(String label, boolean box) {
    return "node" + ++nodeCount + label(label, box);
  }

  private String node(String label) {
    return node(label, false);
  }

  private String node(Class claz, boolean box) {
    return node(claz.getSimpleName(), box);
  }

  private String node(Class claz) {
    return node(claz.getSimpleName(), false);
  }

  private String edge(int n1, int n2) {
    return "node" + n1 + " -> node" + n2 + '\n';
  }

  private String edge(int n1) {
    return edge(n1, nodeCount);
  }

  private String edge() {
    return edge(stackParent.getFirst(), nodeCount);
  }


  private <T extends Node> void genList(StringBuilder toReturn, List<T> list, Class claz) {
    if (!Utility.isListEmpty(list)) {
      toReturn.append(node(claz, true)).append(edge());
      stackParent.push(nodeCount);
      for (Node n : list) {
        toReturn.append(n.accept(this)).append("\n");
      }
      stackParent.pop();
    }
  }

  private <T extends Node> void genNode(StringBuilder toReturn, T node) {
    if (node != null) {
      toReturn.append(node.accept(this));
    }
  }
}
