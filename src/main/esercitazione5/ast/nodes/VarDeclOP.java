package main.esercitazione5.ast.nodes;

import java.util.List;
import main.esercitazione5.ast.ConstValue;
import main.esercitazione5.ast.Type;
import main.esercitazione5.visitors.Visitor;

public class VarDeclOP extends Node {

  private final List<IdNode> idList;
  private final Type type;
  private final List<ConstValue> constValueList;

  // var a, b : integer;\
  public VarDeclOP(List<IdNode> idList, Type type) {
    this.idList = idList;
    this.type = type;
    constValueList = null;
  }

  // var a, b = 1, 2;\
  public VarDeclOP(List<IdNode> idList, List<ConstValue> constValueList) {
    this.idList = idList;
    this.constValueList = constValueList;
    type = null;
  }

  public List<IdNode> getIdList() {
    return idList;
  }

  public Type getType() {
    return type;
  }

  public List<ConstValue> getConstValueList() {
    return constValueList;
  }

  @Override public <T> T accept(Visitor<T> v) {
    return v.visit(this);
  }
}
