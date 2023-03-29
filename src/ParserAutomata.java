import java.util.*;

public class ParserAutomata
{
    private final String source;//String to evaluate in automata
    private final int line;
    public ParserAutomata(String source, int line) { this.source = source; this.line = line; }
    List<Token> tokenNames =new ArrayList<>();
    int state = 0;
    int index_aux = 0;
    int start_lexeme = 0; // Pointer to character in source
    int sLexeme = 0; // Pointer to know where the lexeme starts when printing
    int end_lexeme = 0; // Pointer to know where the lexeme ends when printing
    int get_line = 0; //Get the line of start of the start of a string
    public List<Token> mainAutomata(String source, int line)
    {
        source = source + "$"; // "$" is the character to indicate end of line

        char character = 0;

        //List of every type of token (characters included in alphabet)
        char[] symbols = {'(', ')', '{', '}', '.', ',', ';'};
        char[] operators = {'-','+','/','*','!','=','<','>'};
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

        ArrayList<Integer> list = new ArrayList<>(); // list with the state, starting index and final index of lexeme when we have numbers (digits)
        StringBuilder str = new StringBuilder(); //  Save strings to print in the token
        StringBuilder strType = new StringBuilder();
        StringBuilder num = new StringBuilder(); // Save digits to print in the token
        String idReword = ""; // Save ID or reserved word to print in the token

        // Here begins the automata
        // We go through the string, position by position.
        while (start_lexeme < source.length() - 1)
        {
            // state = 0, we start in Q0
            switch (state)
            {
                case 0:
                    character = source.charAt(start_lexeme);
                    sLexeme = start_lexeme;
                    end_lexeme = start_lexeme;
                    // If the first character of source is a symbol
                    if (isinArray(symbols, character))
                    {
                        state = 1;
                        break;
                    }
                    //If the first character of source is a number
                    else if (Character.isDigit(character)) {
                        state = 9;  // Beginning of automata that evaluates the digits
                        break;
                    }
                    //If the character is a letter
                    else if (Character.isAlphabetic(character)) {
                        state = 18;
                        break;
                    }
                    //If the character is a space
                    else if(character == ' ' || character == '\n' || character == '\t')
                    {
                        state = 22;
                        break;
                    }
                    //If the character is a string
                    else if(character == '"')
                    {
                        state = 24;
                        get_line = line;
                        break;
                    }
                    //If the character is an operator
                    else if (isinArray(operators, character))
                    {
                        state = 26;
                        break;
                    }
                    else{
                        state = -1;
                    }
                    // ------- IN CASE OF SYMBOLS -------
                case 1:
                    character = source.charAt(start_lexeme);
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
                    end_lexeme = list.get(2);
                    break;
                case 10:
                    list = digit_Q10(source, start_lexeme, end_lexeme, list);
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    end_lexeme = list.get(2);
                    break;
                case 11:
                    list = digit_Q11(source, start_lexeme, end_lexeme, list);
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    end_lexeme = list.get(2);
                    break;
                case 12:
                    list = digit_Q12(source, start_lexeme, end_lexeme, list);
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    end_lexeme = list.get(2);
                    break;
                case 13:
                    list = digit_Q13(source, start_lexeme, end_lexeme, list);
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    end_lexeme = list.get(2);
                    break;
                case 14:
                    list = digit_Q14(source, start_lexeme, end_lexeme, list);
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    end_lexeme = list.get(2);
                    break;
                // final states of "digits"
                case 15:
                case 16:
                case 17:
                    num.setLength(0);
                    for (int i = sLexeme; i <= end_lexeme ; i++) // Adding the corresponding characters to "num"
                    {
                        if(source.charAt(i) == '$')
                            break;
                       num.append(source.charAt(i));
                    }
                    String Num = num.toString(); // Converting num to a String

                    tokenNames.add(new Token(TokenType.NUMBER,Num, Float.parseFloat(Num), line));
                    // We restart the values
                    state = 0;
                    start_lexeme++;
                    break;
                // ------- IN CASE OF ID's OR STRINGS -------
                case 18:
                    list = ID_ReservedWord(source,start_lexeme, end_lexeme, list);
                    str.setLength(0);
                    // Get the final string or id
                    for (int i = sLexeme; i <= list.get(2); i++)
                        str.append(source.charAt(i));
                    state = list.get(0);
                    start_lexeme = list.get(1);
                    end_lexeme = list.get(2);
                    break;
                case 19:
                    // Call the function to find out if the input is a string or an identifier (id)
                    idReword = str.toString();
                    if(isInHashMap(idReword))
                        state = 20; // Is a reserved word
                    else
                        state = 21; // Is an id
                    break;
                case 20: // final state

                    tokenNames.add(new Token(getTokenType(idReword),idReword , null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 21: // final state
                    tokenNames.add(new Token(TokenType.ID,idReword, null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                // ------- IN CASE OF DELIM (BLANK SPACES) -------
                case 22:
                    if(source.charAt(start_lexeme) == '\n')
                        line++;
                    //This is Q23 // final state
                    if(source.charAt(start_lexeme++) != ' ' || source.charAt(start_lexeme) != '\n' || source.charAt(start_lexeme)== '\t')
                    {
                        state = 0;
                    }
                    break;
                // ------- IN CASE OF STRING---------------------
                case 24:
                    if(source.charAt(start_lexeme+1) != '"'){
                        if(source.charAt(start_lexeme+1) == '\n')
                            line++;
                        if(source.charAt(start_lexeme+1) == '$')
                            tokenNames = Error("STRING IS NOT CLOSED IN LINE:"+get_line++,tokenNames);
                        strType.append(source.charAt(++start_lexeme));
                    }
                    else {
                        state = 25;
                        start_lexeme++;
                    }
                    break;
                case 25: // final state
                    tokenNames.add(new Token(TokenType.STR, "\""+ strType.toString()+"\"", strType.toString(), line));
                    strType.setLength(0);
                    state = 0;
                    start_lexeme++;
                    break;
                // ------- IN CASE OF RELATIONAL OPERATORS-------
                case 26: //Start of operators automata
                    character = source.charAt(start_lexeme);
                    state = oprel_automata(character);
                    break;
                case 27: // final state
                    tokenNames.add(new Token(TokenType.HYPHEN, "-", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 28: // final state
                    tokenNames.add(new Token(TokenType.PLUS, "+", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 29://Slash case /
                    start_lexeme++;
                    character = source.charAt(start_lexeme);
                    if(character == '/') {
                        state = 30;
                        break;
                    }
                    else if(character == '*')
                    {
                        get_line = line;
                        state = 31;
                        break;
                    }
                    else{ //IF THERE IS OTHER CHARACTER
                        // We restart the values
                        state = 35;
                        start_lexeme--;
                    }
                    break;
                case 30:
                    if(source.charAt(start_lexeme++) == '\n')
                        state = 0;
                    break;
                case 31:
                    start_lexeme++;
                    while(true) {
                        if(source.charAt(start_lexeme) == '$'){
                            tokenNames=Error("COMMENT IS NOT CLOSED IN LINE: "+ get_line++, tokenNames);
                            return tokenNames;
                        }
                        if (start_lexeme <= source.length() - 1) {
                            if (source.charAt(start_lexeme) == '*' && source.charAt(start_lexeme + 1) == '/') {
                                state = 0;
                                start_lexeme += 2;
                                break;
                            } else //Q34 // final state
                            {
                                if(source.charAt(start_lexeme) == '\n')
                                    line++;
                                start_lexeme++;
                            }
                        }
                    }
                    break;
                case 35: // final state
                    tokenNames.add(new Token(TokenType.SLASH, "/", null, line));
                    state = 0;
                    start_lexeme++;
                    break;

                case 36: // final state
                    tokenNames.add(new Token(TokenType.AST, "*", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 38:
                    if(source.charAt(start_lexeme+1) == '=')
                    {
                        state = 39;
                        start_lexeme++;
                    }
                    else{
                        state = 40;
                    }
                    break;
                case 39: // final state
                    tokenNames.add(new Token(TokenType.NEQUAL, "!=", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 40: // final state
                    tokenNames.add(new Token(TokenType.EXMA, "!", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 41:
                    if(source.charAt(start_lexeme+1) == '=')
                    {

                        state = 42;
                        start_lexeme++;
                    }
                    else
                        state = 43;

                    break;
                case 42: // final state
                    tokenNames.add(new Token(TokenType.EQUALS, "==", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 43: // final state
                    tokenNames.add(new Token(TokenType.EQUAL, "=", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 44:
                    if(source.charAt(start_lexeme+1)=='=') {
                        state = 45;
                        start_lexeme++;
                    }else
                        state = 46;
                    break;
                case 45: // final state
                    tokenNames.add(new Token(TokenType.LTHANE, "<=", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 46: // final state
                    tokenNames.add(new Token(TokenType.LTHAN, "<", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 47:
                    if(source.charAt(start_lexeme+1)=='=') {
                        state = 48;
                        start_lexeme++;
                    }else
                        state = 49;
                    break;
                case 48: // final state
                    tokenNames.add(new Token(TokenType.GTHANE, ">=", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case 49: // final state
                    tokenNames.add(new Token(TokenType.GTHAN, ">", null, line));
                    state = 0;
                    start_lexeme++;
                    break;
                case -1: // final state
                    tokenNames = Error("INVALID SYNTAX, "+ source.charAt(start_lexeme)+ " IS NOT VALID IN LINE: "+line, tokenNames);
                    return tokenNames;
            }
        }
        System.out.println("TOTAL LINES: "+line);
        //End of line is added
        tokenNames.add(new Token(TokenType.EOF, "$", null, line));
        //List of tokens is returned to class Scanner
        return tokenNames;
    }
    // Determine if a character is a list
    public boolean isinArray(char[] array, char character)
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
        if(character == '(') // Q2
            state = 2;
        else if(character == ')') // Q3
            state = 3;
        else if(character == '{') // Q4
            state = 4;
        else if(character == '}') // Q5
            state = 5;
        else if(character == '.') // Q6
            state = 6;
        else if(character == ',') // Q7
            state = 7;
        else if(character == ';') // Q8
            state = 8;
        return state;
    }

    // Return the state and index of the next character when we are in state 9 (the first character is a digit) in a list
    public ArrayList<Integer> digit_Q9(String source, int index_initial, int index_final, ArrayList<Integer> list)
    {
        char character;
        for (int i = index_initial + 1; i < source.length(); i++) {
            character = source.charAt(i); // Assigning to 'character' the character of source in position i
            if (Character.isDigit(character))
                index_final++;
            else if (character == '.') // Q10
            {
                state = 10;
                index_aux = i;
                index_final++;
                break;
            }
            else if (character == 'E') // Q12
            {
                state = 12;
                index_aux = i;
                index_final++;
                break;
            }
            else // Final state Q16
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

        character = source.charAt(index_initial + 1); // Assigning to 'character' the character of source

        if (Character.isDigit(character)) { // Q11
            state = 11;
            index_aux = index_initial + 1 ;
            index_final++;
        }
        else // Lexical error
        {
            state = -1;
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
        for (int i = index_initial + 1 ; i < source.length(); i++)
        {
            character = source.charAt(i); // Assigning to 'character' the character of source in position i
            if (Character.isDigit(character))
                index_final++;
            else if (character == 'E') // Q12
            {
                state = 12;
                index_aux = i;
                index_final++;
                break;
            }
            else // Final state Q17
            {
                state = 17;
                index_aux = i -1 ;
                break;
            }
        }
        list.add(0,state);
        list.add(1,index_aux);
        list.add(2,index_final);
        return list;
    }
    // Return the state and index of the next character when we are in state 12 in a list
    public ArrayList<Integer> digit_Q12(String source, int index_initial, int index_final, ArrayList<Integer> list)
    {
        char character = source.charAt(index_initial + 1);
        if (character == '+' || character == '-') // Q13
        {
            state = 13;
            index_aux = index_initial + 1;
            index_final++;
        }
        else if (Character.isDigit(character)) // Q14
        {
            state = 14;
            index_aux = index_initial+1;
            index_final++;
        }
        else // Lexical error
        {
            state = -1;
            index_aux = -1;
        }
        list.add(0, state);
        list.add(1, index_aux);
        list.add(2, index_final);
        return list;
    }
    // Return the state and index of the next character when we are in state 13 in a list
    public ArrayList<Integer> digit_Q13(String source, int index_initial, int index_final,  ArrayList<Integer> list)
    {
        char character;

        character = source.charAt(index_initial + 1); // Assigning to 'character' the character of source

        if (Character.isDigit(character)) { // Q14
            state = 14;
            index_aux = index_initial + 1;
            index_final++;
        }
        else // Lexical error
        {
            state = -1;
        }
        list.add(0, state);
        list.add(1, index_aux);
        list.add(2, index_final);
        return list;
    }
    // Return the state and index of the next character when we are in state 14 in a list
    public ArrayList<Integer> digit_Q14(String source, int index_initial, int index_final, ArrayList<Integer> list)
    {
        char character;

        for (int i = index_initial + 1; i <= source.length(); i++)
        {
            character = source.charAt(i); // Assigning to 'character' the character of source in position i
            if (Character.isDigit(character)) // Still Q14
            {
                index_final++;
                if(Character.toString(character).equals("$"))
                    index_final--;
            }

            else // Final state Q15
            {
                state = 15;
                index_aux = i - 1;
                break;
            }
        }
        list.add(0,state);
        list.add(1,index_aux);
        list.add(2, index_final);
        return list;
    }
    public ArrayList<Integer> ID_ReservedWord(String source, int index_initial, int index_final, ArrayList<Integer> list)
    {
        char character;

        for (int i = index_initial + 1 ; i <= source.length(); i++)
        {
            character = source.charAt(i);
            if (Character.isLetterOrDigit(character)) {
                index_final++;
                if(Character.toString(character).equals("$"))
                    index_final--;
            }
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
        TokenType tokenType = Scanner.reservedWords.get(idRword);
        return tokenType != null;
    }
    public int oprel_automata(char character)
    {
        //Relational operators
        if(character == '-') // Q27
            state = 27;
        else if(character == '+') // 28
            state = 28;
        else if(character == '/') // 29
            state = 29;
        else if(character == '*') // 36
            state = 36;
        else if(character == '!') // Q38
            state = 38;
        else if(character == '=') // Q41
            state = 41;
        else if(character == '<') // Q44
            state = 44;
        else if(character == '>') // Q47
            state = 47;
        return state;
    }
    private List<Token> Error(String messageError, List<Token> L)
    {
        List<Token> tokenError = L;
        tokenError.add(new Token(TokenType.ERROR,messageError,null,line));
        return tokenError;
    }
    public TokenType getTokenType(String idRword) {
        return Scanner.reservedWords.get(idRword);
    }

}
