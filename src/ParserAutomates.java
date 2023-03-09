import java.util.ArrayList;
import java.util.List;

public class ParserAutomates {
    private final String source;//String to evaluate in automata

    public ParserAutomates(String source) { this.source = source; }

    public List<String> mainAutomata(String source)
    {
        //Here begins the automata
        int state = 0;
        int start_lexeme = 0;
        int i = 0;
        List<String> tokenNames =new ArrayList<String>();
        while(i < source.length())
        {
            char character = source.charAt(i);
            switch(state)
            {
                case 0: //State 0 define the kind of token was introduced
                    start_lexeme = i;
                    if(character == '<' || character == '>' || character == '=')
                    {
                        state = 1;
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


}
