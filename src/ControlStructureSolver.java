import java.util.List;

public class ControlStructureSolver {
    private final Node node;
    private final SymbolTable table;

    public ControlStructureSolver(Node node, SymbolTable table) {
        this.node = node;
        this.table = table;
    }
    public void solve() {
        TokenType controlType = node.getValue().type;
        switch (controlType) {
            case IF:
                solveIf();
                break;
            case WHILE:
                solveWhile();
                break;
            case FOR:
                solveFor();
                break;
            default:
                throw new RuntimeException("Error: Tipo de estructura de control no válido.");
        }
    }
    private void solveIf() {
        List<Node> children = node.getChild();
        Node conditionNode = children.get(0);
        if (evaluateCondition(conditionNode)) {
            for (int i = 1; i < children.size(); i++) {
                Node instructionNode = children.get(i);
                if (isInstruction(instructionNode)) {
                    executeInstruction(instructionNode);
                } else {
                    break;
                }
            }
        }
        else {
            Node elseNode = children.get(children.size()-1);
            if(elseNode != null)
            {
                List<Node> childrenElse = elseNode.getChild();
                for (int i = 0; i < childrenElse.size(); i++) {
                    Node instructionNode = childrenElse.get(i);

                    if (isInstruction(instructionNode)) {
                        executeInstruction(instructionNode);
                    } else {
                        break;
                    }
                }
            }
        }
    }
    private void solveWhile() {
        List<Node> children = node.getChild();
        Node conditionNode = children.get(0);

        while (evaluateCondition(conditionNode)) {
            for (int i = 1; i < children.size(); i++) {
                Node instructionNode = children.get(i);
                if (isInstruction(instructionNode)) {
                    executeInstruction(instructionNode);
                }
            }
        }
    }

    private void solveFor() {
        List<Node> children = node.getChild();
        Node initializationNode = children.get(0);
        Node conditionNode = children.get(1);
        for (executeInstruction(initializationNode); evaluateCondition(conditionNode); ) {
            for (int i = 1; i < children.size(); i++) {
                Node instructionNode = children.get(i);
                if (isInstruction(instructionNode)) {
                    executeInstruction(instructionNode);
                }
            }
        }
    }
    private boolean evaluateCondition(Node conditionNode) {
        // Evaluar la expresión de la condición y devolver el resultado
        Token token = conditionNode.getValue();
        List<Node> children = conditionNode.getChild();
        Node leftNode = children.get(0);
        Node rightNode = children.get(1);
        double leftValue = (leftNode.getValue().type == TokenType.ID) ? (double) table.Get(leftNode.getValue().lexeme) : Double.parseDouble(leftNode.getValue().literal.toString());
        double rightValue = (rightNode.getValue().type == TokenType.ID) ? (double) table.Get(rightNode.getValue().lexeme) : Double.parseDouble(rightNode.getValue().literal.toString());

        switch (token.type) {
            case GTHAN:
                return leftValue > rightValue;
            case LTHAN:
                return leftValue < rightValue;
            case EQUAL:
                return leftValue == rightValue;
            case NEQUAL:
                return leftValue != rightValue;
            case GTHANE:
                return leftValue >= rightValue;
            case LTHANE:
                return leftValue <= rightValue;
            default:
                throw new RuntimeException("Error: Tipo de condición no válido.");
        }
    }

    private boolean isInstruction(Node node) {
        TokenType type = node.getValue().type;
        return type == TokenType.EQUAL || type == TokenType.VAR || type == TokenType.PRINT || type == TokenType.IF || type == TokenType.WHILE || type == TokenType.FOR;
    }
    private void executeInstruction(Node instructionNode) {
        // Ejecutar la instrucción correspondiente al nodo
        Token token = instructionNode.getValue();
        switch (token.type) {
            case EQUAL:
                AssignationSolver assignationSolver = new AssignationSolver(instructionNode,table);
                assignationSolver.solve();
                break;
            case VAR:
                VarSolver varSolver = new VarSolver(instructionNode, table);
                varSolver.solve();
                break;
            case PRINT:
                PrintSolver printSolver = new PrintSolver(instructionNode,table);
                printSolver.solve();
                break;
            case IF:
            case WHILE:
            case FOR:
                ControlStructureSolver controlStructureSolver = new ControlStructureSolver(instructionNode,table);
                controlStructureSolver.solve();
                break;
            default:
                throw new RuntimeException("Error: Tipo de instrucción no válido.");
        }
    }


}
