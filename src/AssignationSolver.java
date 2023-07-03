public class AssignationSolver {
    private final Node node;
    private final SymbolTable table;
    public AssignationSolver(Node node, SymbolTable table){this.node = node; this.table = table;}
    public void solve() {
        Token varNameToken = node.getChild().get(0).getValue();
        String varName = varNameToken.lexeme;
        if (!table.idExist(varName)) {
            throw new RuntimeException("Error: El ID no existe.");
        }
        else {
            assignValue(varName, node, table);
            //System.out.println("VARIABLE AGREGADA EXITOSAMENTE");
        }
    }

    public static void assignValue(String varName, Node node, SymbolTable table) {
        Node valueNode = node.getChild().get(1);
        Object value = null;
        if (valueNode.getValue().type == TokenType.ID) {
            // Caso de asignación de otra variable
            String sourceVarName = valueNode.getValue().lexeme;
            if (!table.idExist(sourceVarName)) {
                throw new RuntimeException("Error: Variable no encontrada.");
            }
            value = table.Get(sourceVarName);
            table.assign(varName,value);
        }else if (valueNode.getValue().type == TokenType.NUMBER || valueNode.getValue().type == TokenType.STR) {
            // Caso de asignación de valor literal
            value = valueNode.getValue().literal;
            table.assign(varName,value);
        }else {
            // Caso de asignación de expresión aritmética
            ArithmeticSolver arithmeticSolver = new ArithmeticSolver(valueNode, table);
            value = arithmeticSolver.solve();
            table.assign(varName, value);
        }
    }
}
