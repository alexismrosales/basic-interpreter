import java.util.List;

public class ArithmeticSolver {
    private final Node node;
    private final SymbolTable table;
    public ArithmeticSolver(Node node, SymbolTable table) {
        this.node = node;

        this.table = table;
    }

    public Object solve(){
        return solve(node);
    }
    private Object solve(Node n){
        // No tiene hijos, es un operando
        if(n.getChild().isEmpty()){
            if(n.getValue().type == TokenType.NUMBER || n.getValue().type == TokenType.STR){
                return n.getValue().literal;
            }
            else if(n.getValue().type == TokenType.ID){
                String ID = n.getValue().lexeme;
                if (!table.idExist(ID)) {
                    throw new RuntimeException("Error: Variable "+ID+" no encontrada.");
                }
                return table.Get(ID);
            }
        }
        List<Node> children = n.getChild();
        TokenType operatorType = n.getValue().type;

        // Verificar que el operador tenga al menos dos hijos
        if (children.size() < 2) {
            throw new RuntimeException("Error: Operador sin suficientes operandos.");
        }
    // Resolver el primer hijo
        Object result = solve(children.get(0));

        // Recorrer los demás hijos y realizar las operaciones correspondientes
        for (int i = 1; i < children.size(); i++) {
            Node child = children.get(i);

            Object operand = solve(child);
            if (result instanceof Double && operand instanceof Double) {
                switch (operatorType) {
                    case PLUS:
                        result = (Double) result + (Double) operand;
                        break;
                    case HYPHEN:
                        result = (Double) result - (Double) operand;
                        break;
                    case AST:
                        result = (Double) result * (Double) operand;
                        break;
                    case SLASH:
                        result = (Double) result / (Double) operand;
                        break;
                    default:
                        throw new RuntimeException("Error: Operador no válido.");
                }
            } else if (result instanceof String && operand instanceof String) {
                if (operatorType == TokenType.PLUS) {
                    result = (String) result + (String) operand;
                } else {
                    throw new RuntimeException("Error: Operación no válida para cadenas.");
                }
            } else {
                throw new RuntimeException("Error: Tipos de operandos incompatibles.");
            }
        }

        return result;
    }



}
