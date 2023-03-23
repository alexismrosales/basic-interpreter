import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int line = 1;

    private static final Map<String, TokenType> reservedWords;
    static {
        reservedWords = new HashMap<>();
        //Logic operators
        reservedWords.put("AND", TokenType.AND);
        reservedWords.put("OR", TokenType.OR);
        //Identifier
        reservedWords.put("id",TokenType.ID);
        //Data type
        reservedWords.put("number", TokenType.NUMBER);
        reservedWords.put("string",TokenType.STR);
        //Special words
        reservedWords.put("class", TokenType.CLASS);
        //Language signs
        reservedWords.put("(", TokenType.LPAR);
        reservedWords.put(")", TokenType.RPAR);
        reservedWords.put("{", TokenType.LBRA);
        reservedWords.put("}", TokenType.RBRA);
        reservedWords.put(".",TokenType.FS);
        reservedWords.put(",",TokenType.COMMA);
        reservedWords.put(";",TokenType.SC);
        reservedWords.put("-",TokenType.HYPHEN);
        reservedWords.put("+",TokenType.PLUS);
        reservedWords.put("*",TokenType.AST);
        reservedWords.put("/",TokenType.SLASH);
        reservedWords.put("!",TokenType.EXMA);
        reservedWords.put("=",TokenType.EQUALS);
        reservedWords.put("==",TokenType.EQUAL);
        reservedWords.put("=!",TokenType.NEQUAL);
        reservedWords.put("<",TokenType.LTHAN);
        reservedWords.put(">",TokenType.GTHAN);
        reservedWords.put("<=",TokenType.LTHANE);
        reservedWords.put(">=",TokenType.GTHANE);
    }

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        //Instance of class
        ParserAutomates parserAutomates = new ParserAutomates(source,line);
        tokens = parserAutomates.mainAutomata(source,line);
        return tokens;
    }
}
