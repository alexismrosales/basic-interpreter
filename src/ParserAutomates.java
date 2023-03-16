import java.util.ArrayList;
import java.util.List;

public class ParserAutomates {
    private final String source;//String to evaluate in automata

    public ParserAutomates(String source) { this.source = source; }
    List<String> tokenNames =new ArrayList<String>();
    public List<String> mainAutomata(String source)
    {
        //Here begins the automata
        int state = 0;
        int start_lexeme = 0;
        int i = 0;

        while(i < source.length())
        {
            char character = source.charAt(i);
            //List of every type of token
            char[] symbols = {'(',')','{','}'};
            switch(state)
            {
                case 0: //State 0 define the kind of token was introduced
                    start_lexeme = i;
                    if(character == '(' || character == ')' || character == '{' || character == '}')
                    {
                        state = 1;
                        state = symbol_automata(character);
                    }
                    //If the first character of source is a number
                    if(Character.isDigit(character))
                    {
                        state = 13;  //Beginning of automata that evaluates the digits
                    }
                    //If the character is a letter
                    if(Character.isAlphabetic(character))
                    {
                        state = 9;
                    }

                    break;
            }
        }
        return tokenNames;
    }
    public boolean isCharacter(char[] array, char character)
    {

    }
    public int symbol_automata(char character)
    {
        String value;
        int state = 0;
        //Language signs
        if(character == '(') { //Q2
            tokenNames.add(TokenType.LPAR.toString());
            state = 2;
        }
        if(character == ')') { //Q3
            tokenNames.add(TokenType.RPAR.toString());
            state = 3;
        }
        if(character == '{') { //Q4
            tokenNames.add(TokenType.LBRA.toString());
            state = 4;
        }
        if(character == '}') { //Q5
            tokenNames.add(TokenType.RBRA.toString());
            state = 5;
        }
        if(character == '.') { //Q6
            tokenNames.add(TokenType.FS.toString());
            state = 6;
        }
        if(character == ',') { //Q7
            tokenNames.add(TokenType.COMMA.toString());
            state = 7;
        }
        if(character == ';') { //Q8
            tokenNames.add(TokenType.SC.toString());
            state = 8;
        }
        return state;
    }

}
