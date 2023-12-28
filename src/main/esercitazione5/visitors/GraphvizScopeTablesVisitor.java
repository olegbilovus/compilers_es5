package main.esercitazione5.visitors;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import main.esercitazione5.StringTable;
import main.esercitazione5.Utility;
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

public class GraphvizScopeTablesVisitor extends Visitor<String> {

  private final Deque<ScopeTable> stack;
  private final Deque<HashMap<Integer, Integer>> arrows;
  private final StringBuilder arrowsToReturn = new StringBuilder();
  private final StringBuilder arrowsRankToReturn = new StringBuilder();
  private int tableCount = 0;

  public GraphvizScopeTablesVisitor(StringTable stringTable) {
    super(stringTable);
    stack = new ArrayDeque<>();
    arrows = new ArrayDeque<>();
  }

  @Override public String visit(IdNode v) {
    return "";
  }

  @Override public String visit(ProgramOP v) {
    arrows.push(new HashMap<>());
    StringBuilder toReturn =
        new StringBuilder("digraph g {\nnode[ shape = none, fontname = \"Arial\" ];\n");
    stack.push(v.getScopeTable());

    toReturn.append(genTable("GLOBALS", ProgramOP.class, null));

    genList(toReturn, v.getFunOPList());
    genList(toReturn, v.getProcOPList());

    arrowsToReturn.append("{ rank = same; ");
    for (Integer tableId : arrows.getFirst().values()) {
      arrowsToReturn.append("table").append(tableId).append(" ");
    }
    arrowsToReturn.append("}\n");

    toReturn.append(arrowsToReturn).append(arrowsRankToReturn).append("\n}");

    return toReturn.toString();
  }

  @Override public String visit(VarDeclOP v) {
    return "";
  }

  @Override public String visit(FunOP v) {
    // add params to function table
    StringBuilder toReturn = new StringBuilder();
    stack.push(v.getScopeTable());
    toReturn.append(genTable(st(v.getId()), FunOP.class, v.getId().getId()));

    // process body, arrows pushed later because need the prev arrows in the prev gen
    arrows.push(new HashMap<>());
    toReturn.append(v.getBodyOP().accept(this));
    stack.pop();
    arrows.pop();

    return toReturn.toString();
  }

  @Override public String visit(ProcOP v) {
    // add params to procedure table
    StringBuilder toReturn = new StringBuilder();
    stack.push(v.getScopeTable());
    toReturn.append(genTable(st(v.getId()), ProcOP.class, v.getId().getId()));

    // process body, arrows pushed later because need the prev arrows in the prev gen
    arrows.push(new HashMap<>());
    toReturn.append(v.getBodyOP().accept(this));
    stack.pop();
    arrows.pop();

    return toReturn.toString();
  }

  @Override public String visit(ProcFunParamOP v) {
    // it is already in the procedure table
    return "";
  }

  @Override public String visit(BodyOP v) {
    StringBuilder toReturn = new StringBuilder();
    // VarDecls are already in the parent table

    if (!Utility.isListEmpty(v.getStatList())) {
      for (Stat stat : v.getStatList()) {
        toReturn.append(stat.accept(this));
      }
    }

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

  @Override public String visit(IdNodeExpr v) {
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
    // add VarDecls to table
    StringBuilder toReturn = new StringBuilder();
    stack.push(v.getScopeTable());
    toReturn.append(genTable("", WhileOP.class, null));

    // process body, arrows pushed later because need the prev arrows in the prev gen
    arrows.push(new HashMap<>());
    if (v.getBody() != null) {
      toReturn.append(v.getBody().accept(this));
    }
    stack.pop();
    arrows.pop();

    return toReturn.toString();
  }

  @Override public String visit(IfOP v) {
    // add VarDecls to table
    StringBuilder toReturn = new StringBuilder();
    stack.push(v.getScopeTable());
    toReturn.append(genTable("", WhileOP.class, null));

    // process body, arrows pushed later because need the prev arrows in the prev gen
    arrows.push(new HashMap<>());
    if (v.getBody() != null) {
      toReturn.append(v.getBody().accept(this));
    }
    stack.pop();
    arrows.pop();

    if (!Utility.isListEmpty(v.getElifOPList())) {
      for (ElifOP elifOP : v.getElifOPList()) {
        toReturn.append(elifOP.accept(this));
      }
    }

    if (v.getElse() != null) {
      toReturn.append(v.getElse().accept(this));
    }

    return toReturn.toString();
  }

  @Override public String visit(ElifOP v) {
    // add VarDecls to table
    StringBuilder toReturn = new StringBuilder();
    stack.push(v.getScopeTable());
    toReturn.append(genTable("", ElifOP.class, null));

    // process body, arrows pushed later because need the prev arrows in the prev gen
    arrows.push(new HashMap<>());
    if (v.getBody() != null) {
      toReturn.append(v.getBody().accept(this));
    }
    stack.pop();
    arrows.pop();

    return toReturn.toString();
  }

  @Override public String visit(ElseOP v) {
    // add VarDecls to table
    StringBuilder toReturn = new StringBuilder();
    stack.push(v.getScopeTable());
    toReturn.append(genTable("", ElseOP.class, null));

    // process body, arrows pushed later because need the prev arrows in the prev gen
    arrows.push(new HashMap<>());
    if (v.getBody() != null) {
      toReturn.append(v.getBody().accept(this));
    }
    stack.pop();
    arrows.pop();

    return toReturn.toString();
  }

  private <T extends Node> void genList(StringBuilder toReturn, List<T> list) {
    if (!Utility.isListEmpty(list)) {
      for (T node : list) {
        toReturn.append(node.accept(this));
      }
    }
  }

  private String genTableName(String name, Class claz) {
    return "<b>" + name + " [" + claz.getSimpleName() + "]</b>";

  }

  private String genTableHeader(String name, Class claz, Integer id) {
    Integer tableId = id == null ? null : arrows.getFirst().get(id);
    if (tableId == null) {
      tableId = ++tableCount;
    }
    return "table" + tableId + "[ label=<\n"
        + "<table border=\"0\" cellborder=\"1\" cellspacing=\"0\" cellpadding=\"4\">\n" + "<tr>\n"
        + "<td port=\"0\" colspan=\"3\">" + genTableName(name, claz) + "</td>\n" + "</tr>\n";
  }

  private String genArrow(Integer id1, Integer entry1, Integer id2) {
    return "table" + id1 + ":" + entry1 + " -> table" + id2 + ":0;\n";
  }

  private String genTable(String name, Class claz, Integer id) {
    ScopeTable scopeTable = stack.getFirst();
    StringBuilder toReturn = new StringBuilder(genTableHeader(name, claz, id));
    int entryCounter = 0;
    int parentTableCount = tableCount;
    for (Entry<Integer, ScopeEntry> entry : scopeTable.getTable().entrySet()) {
      toReturn.append("<tr>\n").append("<td ").append("port=\"").append(++entryCounter)
          .append("\">").append(st(entry.getKey())).append("</td>\n");

      ScopeEntry scopeEntry = entry.getValue();
      switch (scopeEntry.getKind()) {
        case FUN, PROC -> {
          arrowsToReturn.append(genArrow(parentTableCount, entryCounter, ++tableCount));
          arrows.getFirst().put(entry.getKey(), tableCount);
        }
        default -> { /* do nothing */}
      }

      toReturn.append("<td ").append("port=\"").append(++entryCounter).append("\">")
          .append(scopeEntry.getKind().name()).append("</td>\n");

      boolean args = !Utility.isListEmpty(scopeEntry.getListType1());
      boolean returns = !Utility.isListEmpty(scopeEntry.getListType2());

      toReturn.append("<td ").append("port=\"").append(++entryCounter).append("\">");
      if (args || returns) {
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
      }
      toReturn.append("</td>\n");

      toReturn.append("</tr>\n");

    }

    toReturn.append("</table>>];\n\n");
    return toReturn.toString();
  }
}
