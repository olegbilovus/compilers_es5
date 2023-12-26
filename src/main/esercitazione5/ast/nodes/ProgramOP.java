package main.esercitazione5.ast.nodes;


import java.util.ArrayList;
import java.util.List;
import main.esercitazione5.visitors.Visitor;

public class ProgramOP extends Node {

  private final List<VarDeclOP> varDeclOPList;
  private final List<FunOP> funOPList;
  private final List<ProcOP> procOPList;

  public ProgramOP(ProcOP procOP) {
    procOPList = new ArrayList<>();
    procOPList.add(procOP);
    varDeclOPList = null;
    funOPList = null;
  }

  public ProgramOP(List<VarDeclOP> varDeclOPList, List<FunOP> funOPList, List<ProcOP> procOPList) {
    this.varDeclOPList = varDeclOPList;
    this.funOPList = funOPList;
    this.procOPList = procOPList;
  }

  public List<VarDeclOP> getVarDeclOPList() {
    return varDeclOPList;
  }

  public List<FunOP> getFunOPList() {
    return funOPList;
  }

  public List<ProcOP> getProcOPList() {
    return procOPList;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
