public class PrintSolver {
    private final Node node;
    private final SymbolTable table;
    public PrintSolver(Node node, SymbolTable table){this.node = node; this.table = table;}
    public void solve()
    {
        Node expressionNode = node.getChild().get(0);
        if (expressionNode.getValue().type == TokenType.STR) {
            // Caso de impresión de cadena literal
            String literal = (String) expressionNode.getValue().literal;
            System.out.println(literal);
        }else if (expressionNode.getValue().type == TokenType.ID) {
            // Caso de suma aritmética
            String ID = expressionNode.getValue().lexeme;
            if (!table.idExist(ID)) {
                throw new RuntimeException("Error: Variable "+ID+" no encontrada.");
            }
            Object value = table.Get(ID);
            System.out.println(value.toString());
        } else {
            // Caso de impresión de expresión aritmética
            ArithmeticSolver arithmeticSolver = new ArithmeticSolver(expressionNode, table);
            Object value = arithmeticSolver.solve();
            System.out.println(value.toString());
        }

    }

}
