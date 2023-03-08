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
        reservedWords.put("OR", TokenType.OR);
        reservedWords.put("CLASS", TokenType.CLASS);
    }

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){

        int state = 0;
        int beginning_lexeme = 0;

        for(int i=0; i< source.length(); i++){
            char c = source.charAt(i);

            switch (state){
                case 0:
                    if(c=='{'){
                        // Crear token y agregarlo a la lista

                    }

                    if(c=='1'|| c=='2'|| c=='3'){

                        state = 13;
                        beginning_lexeme = i;

                    }


                    break;
                case 13:
                    break;

            }
        }
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }
}
