package main.esercitazione5.ast.nodes;


import java.util.ArrayList;
import java.util.List;
import main.esercitazione5.ast.nodes.stat.Stat;
import main.esercitazione5.visitors.Visitor;

public class BodyOP extends Node {

  private final List<VarDeclOP> varDeclOPList = new ArrayList<>();

  private final List<Stat> statList = new ArrayList<>();

  public BodyOP(List<VarDeclOP> varDeclOPList, List<Stat> statList) {
    this.varDeclOPList.addAll(varDeclOPList);
    this.statList.addAll(statList);
  }

  public BodyOP(List<VarDeclOP> varDeclOPList) {
    this.varDeclOPList.addAll(varDeclOPList);
  }

  public BodyOP(Stat stat) {
    this.statList.add(stat);
  }

  public List<VarDeclOP> getVarDeclOPList() {
    return varDeclOPList;
  }

  public List<Stat> getStatList() {
    return statList;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
