package main.esercitazione5.scope;

import java.util.List;
import main.esercitazione5.ast.Type;

public class ScopeEntry {

  private final ScopeKind kind;

  // for FUN or PROC args or VAR decl
  private final List<ScopeType> listType1;

  // for return types of FUN
  private final List<Type> listType2;


  public ScopeEntry(ScopeKind kind, List<ScopeType> listType1, List<Type> listType2) {
    this.kind = kind;
    this.listType1 = listType1;
    this.listType2 = listType2;
  }

  public ScopeEntry(ScopeKind kind, ScopeType entry) {
    this.kind = kind;
    this.listType1 = List.of(entry);
    listType2 = null;
  }

  public ScopeKind getKind() {
    return kind;
  }

  public List<ScopeType> getListType1() {
    return listType1;
  }

  public List<Type> getListType2() {
    return listType2;
  }

  // used for VAR only
  public ScopeType getType() {
    return listType1 != null ? listType1.get(0) : null;
  }
}
