import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Scanner {
    private final String source;
    private List<Token> tokens = new ArrayList<>();
    private int line = 1;

    public static Map<String, TokenType> reservedWords;
    static {
        reservedWords = new HashMap<>();
        //Logic operators
        reservedWords.put("and", TokenType.AND);
        reservedWords.put("or", TokenType.OR);
        //Identifier
        reservedWords.put("id",TokenType.ID);
        //Data type
        reservedWords.put("number", TokenType.NUMBER);
        reservedWords.put("string",TokenType.STR);
        //Special words
        reservedWords.put("class", TokenType.CLASS);
        reservedWords.put("if", TokenType.IF);
        reservedWords.put("else", TokenType.ELSE);
        reservedWords.put("for", TokenType.FOR);
        reservedWords.put("while", TokenType.WHILE);
        reservedWords.put("true", TokenType.TRUE);
        reservedWords.put("false", TokenType.FALSE);
        reservedWords.put("return", TokenType.RETURN);
        reservedWords.put("print",TokenType.PRINT);

    }

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        //Instance of class
        ParserAutomata parserAutomata = new ParserAutomata(source,line);
        tokens = parserAutomata.mainAutomata(source,line);
        return tokens;
    }

}
