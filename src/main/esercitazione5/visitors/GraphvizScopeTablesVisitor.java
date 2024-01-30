package main.esercitazione5.visitors;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map.Entry;
import main.esercitazione5.StringTable;
import main.esercitazione5.Utility;
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
import main.esercitazione5.scope.ScopeEntry;
import main.esercitazione5.scope.ScopeKind;
import main.esercitazione5.scope.ScopeTable;
import main.esercitazione5.scope.ScopeType;

public class GraphvizScopeTablesVisitor extends Visitor<String> {

  private final Deque<Integer> stackParentTableNum;
  private int tableCount = 0;

  public GraphvizScopeTablesVisitor(StringTable stringTable) {
    super(stringTable);
    stackParentTableNum = new ArrayDeque<>();
  }

  @Override public String visit(ProgramOP v) {
    StringBuilder toReturn =
        new StringBuilder("digraph g {\nnode[ shape = none, fontname = \"Arial\" ];\n");
    genTable(toReturn, v.getScopeTable(), "GLOBALS", ProgramOP.class);

    genList(toReturn, v.getFunOPList());
    genList(toReturn, v.getProcOPList());

    toReturn.append("\n}");

    return toReturn.toString();
  }

  @Override public String visit(VarDeclOP v) {
    return "";
  }

  @Override public String visit(FunOP v) {
    StringBuilder toReturn = new StringBuilder();
    // process FunOP table
    genTable(toReturn, v.getScopeTable(), st(v.getId()), FunOP.class);
    // process other Stats tables
    genNode(toReturn, v.getBodyOP());

    stackParentTableNum.pop();

    return toReturn.toString();
  }

  @Override public String visit(ProcOP v) {
    StringBuilder toReturn = new StringBuilder();
    // process ProcOP table
    genTable(toReturn, v.getScopeTable(), st(v.getId()), ProcOP.class);

    // process other Stats tables
    genNode(toReturn, v.getBodyOP());

    stackParentTableNum.pop();

    return toReturn.toString();
  }

  @Override public String visit(ProcFunParamOP v) {
    // it is already in the parent table
    return "";
  }

  @Override public String visit(BodyOP v) {
    StringBuilder toReturn = new StringBuilder();
    // VarDecls are already in the parent table

    genList(toReturn, v.getStatList());

    return toReturn.toString();
  }

  @Override public String visit(AddOP v) {
    return "";
  }

  @Override public String visit(MulOP v) {
    return "";
  }

  @Override public String visit(DiffOP v) {
    return "";
  }

  @Override public String visit(DivOP v) {
    return "";
  }

  @Override public String visit(AndOP v) {
    return "";
  }

  @Override public String visit(OrOP v) {
    return "";
  }

  @Override public String visit(GTOP v) {
    return "";
  }

  @Override public String visit(GEOP v) {
    return "";
  }

  @Override public String visit(LTOP v) {
    return "";
  }

  @Override public String visit(LEOP v) {
    return "";
  }

  @Override public String visit(EQOP v) {
    return "";
  }

  @Override public String visit(NEOP v) {
    return "";
  }

  @Override public String visit(UminusOP v) {
    return "";
  }

  @Override public String visit(NotOP v) {
    return "";
  }

  @Override public String visit(IntegerConstExpr v) {
    return "";
  }

  @Override public String visit(RealConstExpr v) {
    return "";
  }

  @Override public String visit(StringConstExpr v) {
    return "";
  }

  @Override public String visit(TrueConstExpr v) {
    return "";
  }

  @Override public String visit(FalseConstExpr v) {
    return "";
  }

  @Override public String visit(CallFunOP v) {
    return "";
  }

  @Override public String visit(CallProcOP v) {
    return "";
  }

  @Override public String visit(IdNode v) {
    return "";
  }

  @Override public String visit(ReturnOP v) {
    return "";
  }

  @Override public String visit(AssignOP v) {
    return "";
  }

  @Override public String visit(WriteOP v) {
    return "";
  }

  @Override public String visit(ReadOP v) {
    return "";
  }

  @Override public String visit(WhileOP v) {
    StringBuilder toReturn = new StringBuilder();
    // process WhileOP table
    genTable(toReturn, v.getScopeTable(), "", WhileOP.class);

    // process other Stats tables
    genNode(toReturn, v.getBody());

    stackParentTableNum.pop();

    return toReturn.toString();
  }

  @Override public String visit(IfOP v) {
    StringBuilder toReturn = new StringBuilder();
    // process IfOP table
    genTable(toReturn, v.getScopeTable(), "", IfOP.class);

    // process other Stats tables
    genNode(toReturn, v.getBody());

    // this is needed because Elif and Else have NOT the If table among their active tables
    stackParentTableNum.pop();
    // process ElifOP tables
    genList(toReturn, v.getElifOPList());
    // process Else table
    genNode(toReturn, v.getElse());

    return toReturn.toString();
  }

  @Override public String visit(ElifOP v) {
    StringBuilder toReturn = new StringBuilder();
    // process ElifOP table
    genTable(toReturn, v.getScopeTable(), "", ElifOP.class);

    // process other Stats tables
    genNode(toReturn, v.getBody());

    stackParentTableNum.pop();

    return toReturn.toString();
  }

  @Override public String visit(ElseOP v) {
    StringBuilder toReturn = new StringBuilder();
    // process ElseOP table
    genTable(toReturn, v.getScopeTable(), "", ElseOP.class);

    // process other Stats tables
    genNode(toReturn, v.getBody());

    stackParentTableNum.pop();

    return toReturn.toString();
  }

  @Override public String visit(LetLoopOP v) {
    StringBuilder toReturn = new StringBuilder();
    // process LetLoopOP table
    genTable(toReturn, v.getScopeTable(), "", LetLoopOP.class);

    // process other Stats tables
    genList(toReturn, v.getWhenOPList());
    genNode(toReturn, v.getOtherwiseOP());

    stackParentTableNum.pop();

    return toReturn.toString();
  }

  @Override public String visit(WhenOP v) {
    StringBuilder toReturn = new StringBuilder();
    // process other Stats tables
    genNode(toReturn, v.getBody());

    return toReturn.toString();
  }

  @Override public String visit(OtherwiseOP v) {
    StringBuilder toReturn = new StringBuilder();
    // process other Stats tables
    genNode(toReturn, v.getBody());

    return toReturn.toString();
  }


  private <T extends Node> void genList(StringBuilder toReturn, List<T> list) {
    if (!Utility.isListEmpty(list)) {
      for (T node : list) {
        toReturn.append(node.accept(this));
      }
    }
  }

  private <T extends Node> void genNode(StringBuilder toReturn, T node) {
    if (node != null) {
      toReturn.append(node.accept(this));
    }
  }

  private String genTableHeader(String name, Class claz) {
    Integer tableId = ++tableCount;
    stackParentTableNum.push(tableId);

    return "table" + tableId + "[ label=<\n"
        + "<table border=\"0\" cellborder=\"1\" cellspacing=\"0\" cellpadding=\"4\">\n" + "<tr>\n"
        + "<td colspan=\"3\">" + "<b>" + name + " [" + claz.getSimpleName() + "]</b>" + "</td>\n"
        + "</tr>\n";
  }

  private void genTable(StringBuilder toReturn, ScopeTable scopeTable, String name, Class claz) {
    if (scopeTable.getTable().isEmpty()) {
      // this push is done to avoid issues with the indexes and the stack
      stackParentTableNum.push(++tableCount);
      return;
    }
    Integer parentTableNum = stackParentTableNum.peekFirst();
    toReturn.append(genTableHeader(name, claz));
    for (Entry<Integer, ScopeEntry> entry : scopeTable.getTable().entrySet()) {
      toReturn.append("<tr>\n<td>").append(st(entry.getKey())).append("</td>\n");

      ScopeEntry scopeEntry = entry.getValue();
      toReturn.append("<td>").append(scopeEntry.getKind().name()).append("</td>\n");

      boolean args = !Utility.isListEmpty(scopeEntry.getListType1());
      boolean returns = !Utility.isListEmpty(scopeEntry.getListType2());
      toReturn.append("<td>");
      if (args) {
        for (ScopeType t : scopeEntry.getListType1()) {
          if (scopeEntry.getKind() != ScopeKind.VAR) {
            toReturn.append(t.paramAccess().name()).append(":");
          }
          toReturn.append(t.type().name()).append(", ");
        }
        Utility.deleteLastCommaSpace(toReturn);
      }
      if (returns) {
        toReturn.append(" &rarr; ");
        for (Type t : scopeEntry.getListType2()) {
          toReturn.append(t.name()).append(", ");
        }
        Utility.deleteLastCommaSpace(toReturn);
      }

      toReturn.append("</td>\n</tr>\n");
    }

    toReturn.append("</table>>];\n\n");
    toReturn.append(genEdge(parentTableNum));
  }

  private String genEdge(Integer parentTableNum) {
    if (stackParentTableNum.size() < 2) {
      return "";
    }
    return "table" + parentTableNum + " -> table" + stackParentTableNum.getFirst() + ";\n";
  }

}
