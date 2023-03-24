import java.util.ArrayList;
import java.util.List;

public class ParserAutomata
{
    private final String source;//String to evaluate in automata
    private final int line;
    public ParserAutomata(String source, int line) { this.source = source; this.line = line; }
    List<Token> tokenNames =new ArrayList<>();
    int state = 0;
    int index_aux = 0;
    public List<Token> mainAutomata(String source, int line)
    {
        source = source + "$";
        //Here begins the automata
        int start_lexeme = 0; // Pointer to character in source
        char character;

        //List of every type of token (characters included in alphabet)
        char[] symbols = {'(', ')', '{', '}', '.', ',', ';'};
        char[] letter = new char[100];
        // Adding the alphabet accept to letter
        char c;
        int k = 0;
        for (c = '0'; c <= '9'; c++, k++)
            letter[k] = c;
        for (c = 'A'; c <= 'Z'; c++, k++)
            letter[k] = c;
        for (c = 'a'; c <= 'z'; c++, k++)
            letter[k] = c;

        ArrayList<Integer> list = new ArrayList<>(); // list with the state and index lexeme when we have numbers (digits)
        StringBuilder str = new StringBuilder(); //  Save strings to print in the token
        StringBuilder num = new StringBuilder(); // Save digits to print in the token

        int sLexeme = 0; // pointer to know where the lexeme starts when printing
        int end_lexeme = start_lexeme; // pointer to know where the lexeme ends when printing

        // We go through the string, position by position.
        while (start_lexeme < source.length()-1)
        {
            // state = 0, we start in Q0
            switch (state)
            {
                case 0:
                    character = nextCharacter(start_lexeme);
                    // If the first character of source is a symbol
                    if (isSymbol(symbols, character))
                    {
                        state = 1;
                        break;
                    }
                    //If the first character of source is a number
                    else if (Character.isDigit(character)) {
                        state = 9;  //Beginning of automata that evaluates the digits
                        sLexeme = start_lexeme;
                        System.out.println("state: " + state);
                        System.out.println("index: " + start_lexeme);
                        break;
                    }
                    //If the character is a letter
                    else if (Character.isAlphabetic(character)) {
                        state = 18;
                        break;
                    }
                    // ------- IN CASE OF SYMBOLS -------
                case 1:
                    character = nextCharacter(start_lexeme);
                    state = symbol_automata(character);
                    break;
                case 2: // final state
                    tokenNames.add(new Token(TokenType.LPAR, "(", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 3: // final state
                    tokenNames.add(new Token(TokenType.RPAR, ")", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 4: // final state
                    tokenNames.add(new Token(TokenType.LBRA, "{", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 5: // final state
                    tokenNames.add(new Token(TokenType.RBRA, "}", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 6: // final state
                    tokenNames.add(new Token(TokenType.FS, ".", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 7: // final state
                    tokenNames.add(new Token(TokenType.COMMA, ",", null, line));
                    state = 0;
                    start_lexeme+=1;
                    break;
                case 8: // final state
                    tokenNames.add(new Token(TokenType.SC, ";", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                // ------- IN CASE OF DIGITS -------
                case 9:
                    list = digit_Q9(source, start_lexeme, end_lexeme, list);
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    /*System.out.println("state: " + state);
                    System.out.println("index: " + sLexeme);
                    System.out.println("end index: " + list.get(2));*/
                    break;
                case 10:
                    list = digit_Q10(source, start_lexeme, end_lexeme, list);
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    /*System.out.println("state: " + state);
                    System.out.println("index: " + sLexeme);
                    System.out.println("end index: " + list.get(2));*/
                    break;
                case 11:
                    list = digit_Q11(source, start_lexeme, end_lexeme, list);
                    state = list.get(0);
                    start_lexeme = list.get(1);

                    System.out.println("state: " + state);
                    System.out.println("index: " + start_lexeme);
                    System.out.println("end index: " + list.get(2));
                    break;
                case 12:
                    list = digit_Q12(source, start_lexeme, list);
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    break;
                case 13:
                    list = digit_Q13(source, start_lexeme, list);
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    break;
                case 14:
                    list = digit_Q14(source, start_lexeme, list);
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    break;
                // final states of "digits"
                case 15:
                case 16:
                case 17:

                    for (int i = sLexeme; i <= list.get(2); i++)
                    {
                       num.append(source.charAt(i));
                    }
                    String Num = num.toString();

                    tokenNames.add(new Token(TokenType.NUMBER,Num, null, line));
                    state = 0;
                    start_lexeme++;
                    System.out.println("state: " + state);
                    System.out.println("index: " + start_lexeme);
                    break;
                // ------- IN CASE OF ID's OR STRINGS -------
                case 18:
                    list = ID_ReservedWord(source,start_lexeme, end_lexeme, list);
                    // Get the final string or id
                    for (int i = start_lexeme; i <= list.get(2); i++)
                    {
                        str.append(source.charAt(i));
                    }
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    break;
                case 19:
                    // Call the function to find out if the input is a string or an identifier
                    String idReword = str.toString();
                    if(isInHashMap(idReword)) // Is a reserved word
                        state = 20;
                    else // Is an id
                        state = 21;
                    break;
                case 20: // final state
                    //tokenNames.add(new Token(TokenType.RWORD, , null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 21: // final state
                    tokenNames.add(new Token(TokenType.ID,"id" , null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                // ------- IN CASE OF DELIM (BLANK SPACES) -------
                case 22:


            }
        }

        return tokenNames;
    }
    // Return the character in start_lexeme position
    public char nextCharacter(int start_lexeme)
    {
        char character;
        character = source.charAt(start_lexeme);
        return character;
    }

    // Determine if a character is a symbol
    public boolean isSymbol(char[] array, char character)
    {
        for (char c : array) {
            if (character == c)
                return true;
        }
        return false;
    }
    // Return the state of the symbol introduced

    public int symbol_automata(char character)
    {

        //Language symbols
        if(character == '(') //Q2
            state = 2;
        else if(character == ')') //Q3
            state = 3;
        else if(character == '{') //Q4
            state = 4;
        else if(character == '}') //Q5
            state = 5;
        else if(character == '.') //Q6
            state = 6;
        else if(character == ',') //Q7
            state = 7;
        else if(character == ';') //Q8
            state = 8;
        return state;
    }

    // Return the state and index of the next character when we are in state 9 (the first character is a digit) in a list
    public ArrayList<Integer> digit_Q9(String source, int index_initial, int index_final, ArrayList<Integer> list)
    {
        char character;
        for (int i = index_initial + 1; i < source.length(); i++) {
            character = source.charAt(i); // assigning to 'character' the character of source in position i
            if (Character.isDigit(character))
                index_final++;
            else if (character == '.') // Q10
            {
                state = 10;
                index_aux = i;
                index_final++;
                break;
            } else if (character == 'E') // Q12
            {
                state = 12;
                index_aux = i;
                index_final++;
                break;
            } else // final state Q16
            {
                state = 16;
                index_aux = i - 1;
                break;
            }
        }
        list.add(0,state);
        list.add(1,index_aux);
        list.add(2, index_final);
        return list;
    }
    // Return the state and index of the next character when we are in state 10 in a list
    public ArrayList<Integer> digit_Q10(String source, int index_initial, int index_final,ArrayList<Integer> list)
    {
        char character;

        character = source.charAt(index_initial + 1); // assigning to 'character' the character of source

        if (Character.isDigit(character)) { // Q11
            state = 11;
            index_aux = index_initial + 1 ;
            index_final++;
        }
        else // lexical error
        {
            state = -1;
            index_aux = -1;
        }
        list.add(0, state);
        list.add(1, index_aux);
        list.add(2, index_final);
        return list;
    }
    // Return the state and index of the next character when we are in state 11 in a list
    public ArrayList<Integer> digit_Q11(String source, int index_initial, int index_final, ArrayList<Integer> list)
    {
        char character;
        for (int i = index_initial ; i < source.length(); i++)
        {
            character = source.charAt(i+1); // assigning to 'character' the character of source in position i
            if (Character.isDigit(character))
                index_final++;
            else if (character == 'E') //Q12
            {
                state = 12;
                index_aux = i;
                index_final++;
                break;
            }
            else
            {
                state = 17;
                index_aux = i ;
                break;
            }
        }
        list.add(0,state);
        list.add(1,index_aux);
        list.add(2,index_final);
        return list;
    }
    // Return the state and index of the next character when we are in state 12 in a list
    public ArrayList<Integer> digit_Q12(String source, int index, ArrayList<Integer> list)
    {
        char character = source.charAt(index + 1);
        if (character == '+' || character == '-') // Q13
        {
            state = 13;
            index_aux = index + 1;
        }
        else if (Character.isDigit(character)) // Q14
        {
            state = 14;
            index_aux = index +1;
        }
        else // lexical error
        {
            state = -1;
            index_aux = -1;
        }
        list.add(0,state);
        list.add(1,index_aux);
        return list;
    }
    // Return the state and index of the next character when we are in state 13 in a list
    public ArrayList<Integer> digit_Q13(String source, int index, ArrayList<Integer> list)
    {
        char character;

        character = source.charAt(index + 1); // assigning to 'character' the character of source

        if (Character.isDigit(character)) { // Q14
            state = 14;
            index_aux = index;
        }
        else // lexical error
        {
            state = -1;
            index_aux = -1;
        }
        list.add(0, state);
        list.add(1, index_aux);
        return list;
    }
    // Return the state and index of the next character when we are in state 14 in a list
    public ArrayList<Integer> digit_Q14(String source, int index, ArrayList<Integer> list)
    {
        char character;
        for (int i = index + 1; i <= source.length(); i++)
        {
            character = source.charAt(i); // assigning to 'character' the character of source in position i
            if (!Character.isDigit(character)) // still state 9
            {
                state = 15;
                index_aux = i - 1;
                break;
            }
        }
        list.add(0,state);
        list.add(1,index_aux);
        return list;
    }
    public ArrayList<Integer> ID_ReservedWord(String source, int index_initial, int index_final, ArrayList<Integer> list)
    {
        char character;
        character = source.charAt(index_initial + 1);
        for (int i = index_initial + 1 ; i <= source.length(); i++)
        {
            if (Character.isLetterOrDigit(character))
                index_final++;
            else
            {
                state = 19;
                index_aux = i - 1;
                break;
            }
        }
        list.add(0,state);
        list.add(1,index_aux);
        list.add(2, index_final);
        return list;
    }
    // Determine if the input is an id
    public boolean isInHashMap(String idRword)
    {
        for(TokenType tokenType : TokenType.values())
        {
            if(tokenType.name().equals(idRword))
            {
                return true;
            }
        }
        return false;
    }
}
