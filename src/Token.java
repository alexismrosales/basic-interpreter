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
}
