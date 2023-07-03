public class Token {
    final TokenType type;

    final String lexeme;
    final Object literal;
    final int line;
    final int position;

    public Token(TokenType type, String lexeme, Object literal, int line, int position){
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.position = position;
    }

    public Token(TokenType type, String lexeme) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = null;
        this.position = 0;
        this.line = 0;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Token)) {
            return false;
        }

        if(this.type == ((Token)o).type){
            return true;
        }

        return false;
    }
    public String toString(){
        return type + " " + lexeme + " " + literal;
    }

    //Auxiliar methods
    public boolean isOperating(){
        switch (this.type){
            case ID:
            case STR:
            case FALSE:
            case TRUE:
            case NUMBER:
                return true;
            default:
                return false;
        }
    }

    public boolean isOperator(){
        switch (this.type){
            case PLUS:
            case HYPHEN:
            case AST:
            case SLASH:
            case EQUAL:
            case GTHAN:
            case GTHANE:
            case LTHAN:
            case LTHANE:
            case NEQUAL:
                return true;
            default:
                return false;
        }
    }

    public boolean isReservedWord(){
        switch (this.type){
            case VAR:
            case IF:
            case PRINT:
            case ELSE:
            case FOR:
            case WHILE:
            case AND:
            case OR:
                return true;
            default:
                return false;
        }
    }

    public boolean isControlStructure(){
        switch (this.type){
            case IF:
            case ELSE:
            case FOR:
            case WHILE:
                return true;
            default:
                return false;
        }
    }
    public boolean isPrecedenceGTHANE(Token t){
        return this.getPrecedence() >= t.getPrecedence();
    }

    private int getPrecedence(){
        switch (this.type){
            case AST:
            case SLASH:
                return 3;
            case PLUS:
            case HYPHEN:
                return 2;
            case EQUALS:
            case GTHAN:
            case GTHANE:
            case LTHAN:
            case LTHANE:
            case NEQUAL:
                return 1;
        }

        return 0;
    }

    public int arity(){
        switch (this.type) {
            case AST:
            case SLASH:
            case PLUS:
            case HYPHEN:
            case EQUAL:
            case EQUALS:
            case GTHAN:
            case GTHANE:
            case LTHAN:
            case LTHANE:
            case NEQUAL:
                return 2;
        }
        return 0;
    }
}
