import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PostfixGenerator {

    private final List<Token> infija;
    private final Stack<Token> stack;
    private final List<Token> postfija;

    public PostfixGenerator(List<Token> infija) {
        this.infija = infija;
        this.stack = new Stack<>();
        this.postfija = new ArrayList<>();
    }

    public List<Token> convertir() {
        boolean controlStructure = false;
        Stack<Token> stackControlStructure = new Stack<>();

        for (int i = 0; i < infija.size(); i++) {
            Token t = infija.get(i);

            if (t.type == TokenType.EOF) {
                break;
            }

            if (t.isReservedWord()) {
                postfija.add(t);
                if (t.isControlStructure()) {
                    controlStructure = true;
                    stackControlStructure.push(t);
                }
            }
            else if (t.isOperating()) {
                postfija.add(t);
            }
            else if (t.type == TokenType.LPAR) {
                stack.push(t);
            }
            else if (t.type == TokenType.RPAR) {
                while (!stack.isEmpty() && stack.peek().type != TokenType.LPAR) {
                    Token temp = stack.pop();
                    postfija.add(temp);
                }
                if (!stack.isEmpty()) {
                    if (stack.peek().type == TokenType.LPAR) {
                        stack.pop();
                    }
                }
                if (controlStructure && infija.get(i + 1).type == TokenType.LBRA) {
                    postfija.add(new Token(TokenType.SC, ";"));
                }
            }
            else if (t.isOperator()) {
                while (!stack.isEmpty() && stack.peek().isPrecedenceGTHANE(t)) {
                    Token temp = stack.pop();
                    postfija.add(temp);
                }
                stack.push(t);
            }
            else if (t.type == TokenType.SC) {
                while (!stack.isEmpty() && stack.peek().type != TokenType.LBRA) {
                    Token temp = stack.pop();
                    postfija.add(temp);
                }
                postfija.add(t);
            }
            else if (t.type == TokenType.LBRA) {
                // Se mete a la pila, tal como el parentesis. Este paso
                // pudiera omitirse, sólo hay que tener cuidado en el manejo
                // del "}".
                stack.push(t);
            }
            else if (t.type == TokenType.RBRA && controlStructure) {

                // Primero verificar si hay un else:
                if (infija.get(i + 1).type == TokenType.ELSE) {
                    // Sacar el "{" de la pila
                    stack.pop();
                }
                else {
                    // En este punto, en la pila sólo hay un token: "{"
                    // El cual se extrae y se añade un ";" a cadena postfija,
                    // El cual servirá para indicar que se finaliza la estructura
                    // de control.
                    if (!stack.isEmpty()) {
                        stack.pop();
                    }
                    postfija.add(new Token(TokenType.SC, ";"));

                    // Se extrae de la pila de estrucuras de control, el elemento en el tope
                    Token aux = stackControlStructure.pop();

                    if (aux.type == TokenType.ELSE) {
                        stackControlStructure.pop();
                        postfija.add(new Token(TokenType.SC, ";"));
                    }

                    if (stackControlStructure.isEmpty()) {
                        controlStructure = false;
                    }
                }


            }
        }
        while (!stack.isEmpty()) {
            Token temp = stack.pop();
            postfija.add(temp);
        }

        while (!stackControlStructure.isEmpty()) {
            stackControlStructure.pop();
            postfija.add(new Token(TokenType.SC, ";"));
        }
        return postfija;
    }

}