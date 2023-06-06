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
        //Special words
        reservedWords.put("fun", TokenType.FUN);
        reservedWords.put("class", TokenType.CLASS);
        reservedWords.put("if", TokenType.IF);
        reservedWords.put("else", TokenType.ELSE);
        reservedWords.put("for", TokenType.FOR);
        reservedWords.put("while", TokenType.WHILE);
        reservedWords.put("true", TokenType.TRUE);
        reservedWords.put("false", TokenType.FALSE);
        reservedWords.put("return", TokenType.RETURN);
        reservedWords.put("print",TokenType.PRINT);
        reservedWords.put("null", TokenType.NULL);
        reservedWords.put("super", TokenType.SUPER);
        reservedWords.put("var", TokenType.VAR);
        reservedWords.put("this", TokenType.THIS);
    }

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        //Instance of class
        ParserAutomata parserAutomata = new ParserAutomata(source,line);
        return parserAutomata.mainAutomata(source,line);
    }

}
