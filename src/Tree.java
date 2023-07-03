public class Tree {
    private final Node root;

    public Tree(Node root){
        this.root = root;
    }

    public void print() {
        print(root, 0);
    }

    private void print(Node node, int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("  ");
        }
        System.out.println(node.getValue());
        if(!node.getChild().isEmpty()) {
            for (Node child : node.getChild()) {
                print(child, depth + 1);
            }
        }
    }
    public void traverse(){
        SymbolTable table = new SymbolTable();
        for(Node n : root.getChild()){
            Token t = n.getValue();
            switch (t.type){
                // Operadores aritméticos
                case EQUAL:
                    AssignationSolver assignationSolver = new AssignationSolver(n,table);
                    assignationSolver.solve();
                    break;
                case VAR:
                    VarSolver varSolver = new VarSolver(n, table);
                    varSolver.solve();
                    break;
                case PRINT:
                    PrintSolver printSolver = new PrintSolver(n,table);
                    printSolver.solve();
                    break;
                case IF:
                case WHILE:
                case FOR:
                    ControlStructureSolver controlStructureSolver = new ControlStructureSolver(n,table);
                    controlStructureSolver.solve();
                    break;
            }
        }
    }

}
