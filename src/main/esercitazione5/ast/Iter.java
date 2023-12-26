package main.esercitazione5.ast;


import java.util.ArrayList;
import java.util.List;
import main.esercitazione5.ast.nodes.FunOP;
import main.esercitazione5.ast.nodes.ProcOP;
import main.esercitazione5.ast.nodes.VarDeclOP;

public class Iter {


  private final List<VarDeclOP> varDeclOPList = new ArrayList<>();
  private final List<FunOP> funOPList = new ArrayList<>();
  private final List<ProcOP> procOPList = new ArrayList<>();


  public Iter(List<VarDeclOP> varDeclOPList) {
    this.varDeclOPList.addAll(varDeclOPList);
  }

  public Iter(List<VarDeclOP> varDeclOPList, Iter iter) {
    this(varDeclOPList);
    merge(iter);
  }

  public Iter(FunOP funOP) {
    funOPList.add(funOP);
  }

  public Iter(FunOP funOP, Iter iter) {
    this(funOP);
    merge(iter);
  }

  public Iter(ProcOP procOP) {
    procOPList.add(procOP);
  }

  public Iter(ProcOP procOP, Iter iter) {
    this(procOP);
    merge(iter);
  }

  public void merge(Iter iter) {
    varDeclOPList.addAll(iter.varDeclOPList);
    funOPList.addAll(iter.funOPList);
    procOPList.addAll(iter.procOPList);
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
}
