import java.util.List;
import java.util.Stack;

public class ASTGenerator {
    private final List<Token> postfix;
    private final Stack<Node> stack;

    public ASTGenerator(List<Token> postfix){
        this.postfix = postfix;
        this.stack = new Stack<>();
    }

    public Tree generateAST() {
        Stack<Node> stackParent = new Stack<>();
        Node root = new Node(null);
        stackParent.push(root);

        Node parent = root;

        for(Token t : postfix){

            if(t.type == TokenType.EOF){
                break;
            }

            if(t.isReservedWord()){
                Node n = new Node(t);

                parent = stackParent.peek();
                parent.insertNextChild(n);

                stackParent.push(n);
                parent = n;

            }
            else if(t.isOperating()){
                Node n = new Node(t);
                stack.push(n);
            }
            else if(t.isOperator() ){
                int arity = t.arity();
                Node n = new Node(t);
                for(int i=1; i<=arity; i++){
                    Node auxNode = stack.pop();
                    n.insertChild(auxNode);
                }
                stack.push(n);
            }
            else if(t.type == TokenType.SC){

                if (stack.isEmpty()){
                    /*
                    Si la pila esta vacía es porque t es un punto y coma
                    que cierra una estructura de control
                     */
                    stackParent.pop();
                    parent = stackParent.peek();
                }
                else{
                    Node n = stack.pop();

                    if(parent.getValue() != null && parent.getValue().type == TokenType.VAR){
                        /*
                        En el caso del VAR, es necesario eliminar el igual que
                        pudiera aparecer en la raíz del nodo n.
                         */
                        if(n.getValue().type == TokenType.EQUAL){
                            parent.insertChild(n.getChild());
                        }
                        else{
                            parent.insertNextChild(n);
                        }
                        stackParent.pop();
                        parent = stackParent.peek();
                    }
                    else if(parent.getValue() != null && parent.getValue().type == TokenType.PRINT){
                        parent.insertNextChild(n);
                        stackParent.pop();
                        parent = stackParent.peek();
                    }
                    else {
                        parent.insertNextChild(n);
                    }
                }
            }
        }

        // Suponiendo que en la pila sólamente queda un nodo
        // Nodo nodoAux = pila.pop();
        Tree program = new Tree(root);

        return program;
    }
}
