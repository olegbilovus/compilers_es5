## Modifiche alla grammatica

[Immagine diagramma di stati LALR della grammatica](src/main/esercitazione5/images/LALR.svg)

Dopo le seguenti modifiche, la grammatica non presenta più alcun conflitto.

- Sono state rimosse tutte le produzione empty. Questo aggiunge un po' di verbosità alla grammatica
  ma
  ne facilità la lettura, sopratutto per sapere subito come è formato un costrutto della grammatica.

- Il controllo che il Program abbia almeno una Procedura è stato rimandato alla fase semantica,
  nella
  quale comunque si sarebbe dovuto controllare che la procedura si chiamasse "main". Questa modifica
  ha
  permesso l'eliminazione di alcuni conflitti.

- _IOArgs_ diviso in _OArgs_ per gli argomenti di Write e _IArgs_ per gli argomenti di Read. Questo
  permette di anticipare controlli della semantica e a usare generalizzazioni dei nodi senza la
  necessità
  di aggiungere flags.

- In _OArgs_ originalmente si sarebbe dovuto controllare nella fase semantica che le _Expr_ senza
  dollaro siano delle concatenazioni di Stringhe. Questo controllo è stato anticipato nella
  grammatica
  stessa. Questo ha permesso di eliminare molti conflitti.

- In _IArgs_ originalmente si sarebbe dovuto controllare nella fase semantica che le _Expr_ nel
  dollaro siano solamente singoli Identificatori. Questo controllo è stato anticipato nella
  grammatica
  stessa. Questo ha permesso di usare più generalizzazione nel codice.

- In _Function_ non è permesso avere un _Body_ vuoto. Questo perché Function per definizione ritorna
  una o più espressioni. Nella fase semantica si controllerà che Body abbia una ReturnOP.

- Sono state aggiunte le precedenze alle operazioni delle Expr per risolvere i conflitti.

- È stato aggiunto il nodo ElseOP in modo tale da poter accedere alla sua tabella di scope più
  facilmente.

## Testing

In [src/test/java](src/test/java) sono stati scritti gli Unit Test sia per il Lexer, Parser,
Scoping.
Tra le configurazioni di Run, c'è una che avvia tutti gli Unit Test.

Nelle configurazioni del Run ci sono anche quelle per fare il testing dei file di input nelle varie
fasi del compilatore.

## Visitors

### Parser

Nel Run di [ParserTester](src/main/esercitazione5/ParserTester.java) sarà possibile scegliere uno
dei seguenti visitor.

1. **DebugVisitor**: dopo aver fatto il parsing del source, visita l'AST e riscrive dall'
   AST il source in Toy2.
   Il source riscritto sarà equivalente nel significato ma alcune cose cambieranno come ad esempio
   ogni VarDecl inizierà con _var_ mentre in Toy2 possiamo scrivere la keyword una volta e poi
   dichiarare
   più VarDecl.
   Le dichiarazioni di variabili verranno messe sempre all'inizio di ogni scope.

2. **GraphvizASTVisitor** genera codice
   _[dot lang](https://graphviz.org/doc/info/lang.html)_, il
   quale è usato da [Graphviz](https://graphviz.org/) per generare graficamente l'AST.
   Sarà stampato
   a video il _dot lang_, è possibile copiarlo e incollarlo
   su [questo sito](http://magjac.com/graphviz-visual-editor/) per visualizzare l'AST.
   Dal sito è
   possibile esplorare l'AST, per muoversi nell'albero basta tenere premuto CTRL e usare il mouse
   per trascinare..

Qui sotto un esempio di AST sull'[input1.txt](/src/test_files/input1.txt), ossia il codice di
esempio presente su e-learning.
[Link all'immagine.](src/main/esercitazione5/images/AST_input1.svg)

![AST_input1.png](src/main/esercitazione5/images/AST_input1.png)

### Scoping

Nel Run di [ScopingTester](src/main/esercitazione5/ScopingTester.java) sarà possibile scegliere uno
dei seguenti visitor.

1. **ScopingVisitor**: dopo aver fatto il parsing del source, visita l'AST e crea le tabelle di
   scope.
2. **GraphvizScopeTablesVisitor** genera codice
   _[dot lang](https://graphviz.org/doc/info/lang.html)_, il
   quale è usato da [Graphviz](https://graphviz.org/) per generare graficamente le tabelle di scoping. Sarà stampato
   a video il _dot lang_, è possibile copiarlo e incollarlo
   su [questo sito](http://magjac.com/graphviz-visual-editor/) per visualizzare le Tabelle di
   Scoping. Dal sito è
   possibile esplorare le tabelle, per muoversi tenere premuto CTRL e usare il mouse
   per trascinare.

Qui sotto un esempio di tabelle di scoping sull'[input1.txt](/src/test_files/input1.txt), ossia il
codice di esempio presente su e-learning.
[Link all'immagine.](src/main/esercitazione5/images/Scope_input1.svg)

![AST_input1.png](src/main/esercitazione5/images/Scope_input1.png)

## Gestione errori

- In caso ci sia un errore del Lexer si continua l'analisi emettendo un token _\<error\>_.
- In caso ci sia un errore del Parser, questo viene gestito da CUP.
- Nel caso ci sia un errore nello Scoping, viene segnalato nella console su quale simbolo è l'errore
  e quale è il source code che lo compone. Gli errori di Scoping considerati sono:
    - [AlreadyDeclared](src/main/esercitazione5/scope/exceptions/AlreadyDeclaredScopeException.java)
    - [Undeclared](src/main/esercitazione5/scope/exceptions/UndeclaredScopeException.java)
    - [VarDecl](src/main/esercitazione5/scope/exceptions/VarDeclOPScopeException.java) viene
      utilizzato quando in una initialization il numero di variabili è diverso dal numero di
      costanti.
      È possibile vedere degli esempi in [VarDeclOPTest](src/test/java/scoping/VarDeclOPTest.java)
      nel metodo di testing di input invalido.

## Programma Math in Toy2

Così come richiesto dal punto #1 dell'esercitazione4, è stato scritto un programma in Toy2 per fare
operazioni
aritmetiche prendendo l'input dall'utente. Sono stati usati quasi tutti i costrutti della
grammatica.

In [src/test_files/math.txt](src/test_files/math.txt) c'è il source code di Math in Toy2.

In [src/main/esercitazione4/images/AST_math.svg](src/main/esercitazione5/images/AST_math.svg) c'è il
suo AST.

In [src/main/esercitazione4/images/Scope_math.svg](src/main/esercitazione5/images/Scope_math.svg) ci
sono le sue tabelle di scoping.
