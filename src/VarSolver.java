public class VarSolver {
    private final Node node;
    private final SymbolTable table;
    public VarSolver(Node node, SymbolTable table){this.node = node; this.table = table;}
    public void solve() {
        Token varNameToken = node.getChild().get(0).getValue();
        String varName = varNameToken.lexeme;

        if (table.idExist(varName)) {
            throw new RuntimeException("Error: El ID ya existe.");
        }
        else {
            AssignationSolver.assignValue(varName, node, table);
            //System.out.println("VARIABLE AGREGADA EXITOSAMENTE");
        }
    }

}
