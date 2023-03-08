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
        reservedWords.put("AND", TokenType.AND);
        reservedWords.put("CLASS", TokenType.CLASS);
    }

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }
}
