import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Parser {
    private final List<Token> tokens;
    //Terminal tokens
    private final Token CLASS = new Token(TokenType.CLASS,"class");
    private final Token FUN = new Token(TokenType.FUN,"fun");
    private final Token VAR = new Token(TokenType.VAR,"var");
    private final Token OR = new Token(TokenType.OR,"or");
    private final Token AND = new Token(TokenType.AND,"and");

    private final Token FOR = new Token(TokenType.FOR,"for");
    private final Token IF = new Token(TokenType.IF,"if");
    private final Token ELSE = new Token(TokenType.ELSE,"else");
    private final Token PRINT = new Token(TokenType.PRINT,"print");
    private final Token RETURN = new Token(TokenType.RETURN,"return");
    private final Token WHILE = new Token(TokenType.WHILE,"while");
    private final Token LBRA = new Token(TokenType.LBRA,"{");
    private final Token RBRA = new Token(TokenType.RBRA,"}");
    private final Token LTHAN = new Token(TokenType.LTHAN,"<");
    private final Token LTHANE = new Token(TokenType.LTHANE,"<=");
    private final Token GTHAN = new Token(TokenType.GTHAN,">");
    private final Token GTHANE = new Token(TokenType.GTHANE,">=");
    private final Token EXMA = new Token(TokenType.EXMA, "!");
    private final Token HYPHEN = new Token(TokenType.HYPHEN, "-");
    private final Token PLUS = new Token(TokenType.PLUS,"+");
    private final Token EQUAL = new Token(TokenType.EQUAL, "=");
    private final Token EQUALS = new Token(TokenType.EQUALS, "==");
    private final Token NEQUAL = new Token(TokenType.NEQUAL, "!=");
    private final Token COMMA = new Token(TokenType.COMMA, ",");
    private final Token SC = new Token(TokenType.SC,";");
    private final Token FS = new Token(TokenType.FS,".");
    private final Token TRUE = new Token(TokenType.TRUE, "true");
    private final Token FALSE = new Token(TokenType.FALSE, "false");
    private final Token NULL = new Token(TokenType.NULL, "null");
    private final Token THIS = new Token(TokenType.THIS, "this");
    private final Token NUMBER = new Token(TokenType.NUMBER, "");
    private final Token STRING = new Token(TokenType.STR, "");
    private final Token ID = new Token(TokenType.ID, "");
    private final Token LPAR = new Token(TokenType.LPAR, "(");
    private final Token RPAR = new Token(TokenType.RPAR, ")");
    private final Token SLASH = new Token(TokenType.SLASH,"/");
    private final Token AST = new Token(TokenType.AST,"*");
    private final Token SUPER = new Token(TokenType.SUPER, "");
    private final Token end_string = new Token(TokenType.EOF, "");

    //Class variables
    private int i = 0;
    private boolean error = false;
    private Token preanalysis;

    Parser(List<Token> tokens) { this.tokens = tokens; }

    public List<Token> parse() {
        i = 0;
        preanalysis = tokens.get(i);
        PROGRAM();
        if (!error && !preanalysis.equals(end_string)) {
            System.out.println("ERROR IN POSITION " + preanalysis.position + " WAS NOT EXPECTED TOKEN : " + preanalysis.type);
        } else if (!error && preanalysis.equals(end_string)) {
            return tokens;
        }
        return null;
    }
    void PROGRAM()
    {
       if(preanalysis.equals(CLASS) || preanalysis.equals(FUN) || preanalysis.equals(VAR) || preanalysis.equals(FOR) || preanalysis.equals(IF) || preanalysis.equals(PRINT) || preanalysis.equals(RETURN) || preanalysis.equals(WHILE) || preanalysis.equals(LBRA) || preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            DECLARATION();
        }
    }
    void DECLARATION()
    {
        if(error) return;
        if(preanalysis.equals(CLASS)) //CLASS
        {
            CLASS_DECL();
            DECLARATION();
        }
        else if(preanalysis.equals(FUN))//FUN
        {
            FUN_DECL();
            DECLARATION();
        }
        else if(preanalysis.equals(VAR))
        {
            VAR_DECL();
            DECLARATION();
        }
        else if(preanalysis.equals(FOR) || preanalysis.equals(IF) || preanalysis.equals(PRINT) || preanalysis.equals(RETURN) || preanalysis.equals(WHILE) || preanalysis.equals(LBRA) || preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) || preanalysis.equals(SUPER))
        {
            STATEMENT();
            DECLARATION();
        }

    }
    void CLASS_DECL()
    {
        if(error) return;
        if(preanalysis.equals(CLASS)) //CLASS
        {
            match(CLASS);
            if(preanalysis.equals(ID))
            {
                match(ID);
                CLASS_INNER();
                if(error) return;
                if(preanalysis.equals(LBRA))
                {
                    match(LBRA);
                    FUNCTIONS();
                    if(preanalysis.equals(RBRA)){
                        match(RBRA);
                    }
                    else{
                        error = true;
                        System.out.println("CLASS_DECL:ERROR IN POSITION: " + preanalysis.position + " | LEFT BRACKET IS NOT CLOSED");

                    }
                }
                else{
                    error = true;
                    System.out.println("CLASS_DECL:ERROR IN POSITION: " + preanalysis.position + " | RIGHT BRACKET IS NOT CLOSED");
                }
            }
            else{
                error = true;
                System.out.println("CLASS_DECL:ERROR IN POSITION: " + preanalysis.position + " | ID IS MISSING");
            }
        }
        else{
            error = true;
            System.out.println("CLASS_DECL: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void FUN_DECL()
    {
        if(error) return;
        if(preanalysis.equals(FUN)) //FUN
        {
            match(FUN);
            FUNCTION();
        }
        else{
            error = true;
            System.out.println("FUN: ERROR IN POSITION: " + preanalysis.position);
        }

    }
    void VAR_DECL()
    {
        if(error) return;
        if(preanalysis.equals(VAR))//VAR
        {
            match(VAR);
            if(preanalysis.equals(ID))
            {
                match(ID);
                VAR_INIT();
                if(preanalysis.equals(SC))
                {
                    match(SC);
                }
                else{
                    error = true;
                    System.out.println("VAR: ; IS MISING |  ERROR IN POSITION: " + preanalysis.position);
                }
            }
            else{
                error = true;
                System.out.println("VAR: ID IS MISSING |  ERROR IN POSITION: " + preanalysis.position);
            }
        }
        else{
            error = true;
            System.out.println("VAR: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void STATEMENT()
    {
        if(error) return;
        if(preanalysis.equals(ID) || preanalysis.equals(LPAR) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(SUPER))
        {
            EXPR_STMT();
        }
        else if(preanalysis.equals(FOR))
        {
            FOR_STMT();
        }
        else if(preanalysis.equals(IF))
        {
            IF_STMT();
        }
        else if(preanalysis.equals(PRINT))
        {
            PRINT_STMT();
        }
        else if(preanalysis.equals(RETURN))
        {
            RETURN_STMT();
        }
        else if(preanalysis.equals(WHILE))
        {
            WHILE_STMT();
        }
        else if(preanalysis.equals(LBRA))
        {
            BLOCK();
        }
        else{
            error = true;
            System.out.println("STATEMENT: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void EXPR_STMT()
    {
        if(error) return;
        if(preanalysis.equals(ID) || preanalysis.equals(LPAR) || preanalysis.equals(EXMA) ||  preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(SUPER))
        {
            EXPRESSION();
            if(preanalysis.equals(SC))
            {
                match(SC);
            }
            else{
                error = true;
                System.out.println("EXP_STMT: ERROR IN POSITION: " + preanalysis.position + "| ; WAS EXPECTED.");
            }
        }
        else{
            error = true;
            System.out.println("EXPR_STMT: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void FOR_STMT()
    {
        if(error) return;
        if(preanalysis.equals(FOR))
        {
            match(FOR);
            if(preanalysis.equals(LPAR))
            {
                match(LPAR);
                FOR_STMT_1();
                FOR_STMT_2();
                FOR_STMT_3();
                if(preanalysis.equals(RPAR))
                {
                    match(RPAR);
                    STATEMENT();
                }
                else {
                    error = true;
                    System.out.println("FOR_STMT: ERROR IN POSITION: " + preanalysis.position + " | PARENTHESIS IS NOT CLOSED");
                }
            }
        }
        else{
            error = true;
            System.out.println("FOR_STMT: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void FOR_STMT_1()
    {
        if(error) return;
        if(preanalysis.equals(VAR))
        {
            VAR_DECL();
        }
        else if(preanalysis.equals(ID) || preanalysis.equals(LPAR) || preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(SUPER)) {
            EXPR_STMT();
        }
        else if(preanalysis.equals(SC))
        {
            match(SC);
        }
        else{
            error = true;
            System.out.println("FOR_STMT_1: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void FOR_STMT_2()
    {
        if(error) return;

        if(preanalysis.equals(ID) || preanalysis.equals(LPAR) || preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(SUPER)) {
            EXPRESSION();
            if(preanalysis.equals(SC))
            {
                match(SC);
            }
            else {
                error = true;
                System.out.println("FOR_STMT_2: ERROR IN POSITION: " + preanalysis.position + " | ; WAS EXPECTED IN EXPRESSION 2");
            }
        }
        else if(preanalysis.equals(SC))
        {
            match(SC);
        }
        else{
            error = true;
            System.out.println("FOR_STMT_2: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void FOR_STMT_3()
    {
        if(error) return;
        if(preanalysis.equals(ID) || preanalysis.equals(LPAR) || preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(SUPER)) {
            EXPRESSION();
        }
    }
    void IF_STMT()
    {
        if(error) return;
        if(preanalysis.equals(IF))
        {
            match(IF);
            if(preanalysis.equals(LPAR))
            {
                match(LPAR);
                EXPRESSION();
                if(preanalysis.equals(RPAR))
                {
                    match(RPAR);
                    STATEMENT();
                    ELSE_STATEMENT();
                }
            }
        }
        else{
            error = true;
            System.out.println("IF_STMT: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void ELSE_STATEMENT()
    {
        if(error) return;
        if(preanalysis.equals(ELSE))
        {
            match(ELSE);
            STATEMENT();
        }
    }
    void PRINT_STMT()
    {
        if(error) return;
        if(preanalysis.equals(PRINT))
        {
            match(PRINT);
            EXPRESSION();
            if(preanalysis.equals(SC))
            {
                match(SC);
            }
            else {
                error = true;
                System.out.println("PRINT_STMT: ERROR IN POSITION: " + preanalysis.position + " | ; WAS EXPECTED");
            }
        }
        else {
            error = true;
            System.out.println("PRINT_STMT: ERROR IN POSITION: " + preanalysis.position );
        }
    }
    void RETURN_STMT()
    {
        if(error) return;
        if(preanalysis.equals(RETURN))
        {
            match(RETURN);
            FOR_STMT_3(); //DUPLICADO DE RETURN_EXP_OPC
            if(preanalysis.equals(SC))
            {
                match(SC);
            }
            else {
                error = true;
                System.out.println("RETURN_STMT: ERROR IN POSITION: " + preanalysis.position + " | ; WAS EXPECTED");
            }
        }
        else {
            error = true;
            System.out.println("PRINT_STMT: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void WHILE_STMT()
    {
        if(error) return;
        if(preanalysis.equals(WHILE))
        {
            match(WHILE);
            if(preanalysis.equals(LPAR))
            {
                match(LPAR);
                EXPRESSION();
                if(preanalysis.equals(RPAR))
                {
                    match(RPAR);
                    STATEMENT();
                }
                else {
                    error = true;
                    System.out.println("WHILE_STMT: ERROR IN POSITION: " + preanalysis.position + " | BRACKET IS NOT CLOSED");
                }
            }
            else {
                error = true;
                System.out.println("WHILE_STMT: ERROR IN POSITION: " + preanalysis.position + " | BRACKET IS NOT OPENED");
            }
        }
        else {
            error = true;
            System.out.println("WHILE_STMT: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void CLASS_INNER()
    {
        if(error) return;
        if(preanalysis.equals(LTHAN))
        {
            match(LTHAN);
            if(preanalysis.equals(ID))
            {
                match(ID);
            }
            else
            {
                error = true;
                System.out.println("CLASS_INNER: ERROR IN POSITION: " + preanalysis.position + " | TRY < {YOUR ID}");
            }
        }

    }
    void FUNCTIONS()
    {
        if(error) return;
        if(preanalysis.equals(ID))
        {
            FUNCTION();
            if(error) return;
            FUNCTIONS();
        }
    }
    void FUNCTION()
    {
        if(error) return;
        if(preanalysis.equals(ID))
        {
            match(ID);
            if(preanalysis.equals(LPAR))
            {
                match(LPAR);
                PARAMETERS_OPC();
                if(preanalysis.equals(RPAR))
                {
                    match(RPAR);
                    BLOCK();
                }
                else
                {
                    error = true;
                    System.out.println("FUNCTION: ERROR IN POSITION: " + preanalysis.position + " | RIGHT PARENTHESIS IS NOT CLOSED");
                }
            }
            else
            {
                error = true;
                System.out.println("FUNCTION: ERROR IN POSITION: " + preanalysis.position + " | LEFT PARENTHESIS IS NOT CLOSED");
            }
        }

    }
    void PARAMETERS_OPC()
    {
        if(error) return;
        if(preanalysis.equals(ID))
        {
            PARAMETERS();
        }
    }
    void PARAMETERS()
    {
        if(error) return;
        if(preanalysis.equals(ID))
        {
            match(ID);
            PARAMETERS_2();
        }
        else{
            error = true;
            System.out.println("PARAMETERS: ERROR IN POSITION: " + preanalysis.position + " | PARAMETER ID IS MISSING");
        }

    }
    void PARAMETERS_2()
    {
        if(error) return;
        if(preanalysis.equals(COMMA))
        {
            match(COMMA);
            if(preanalysis.equals(ID))
            {
                match(ID);
                PARAMETERS_2();
            }
            else{
                error = true;
                System.out.println("PARAMETERS_2: ERROR IN POSITION: " + preanalysis.position + " | PARAMETER ID IS MISSING");
            }
        }
    }
    void BLOCK()
    {
        if(error) return;

        if(preanalysis.equals(LBRA))
        {
            BLOCK_DECL();
            if(error) return;
            if(preanalysis.equals(RBRA))
            {

                match(RBRA);
            }
            else
            {
                error = true;
                System.out.println("BLOCK: ERROR IN POSITION: " + preanalysis.position+ " | RIGHT PARENTHESIS IS NOT CLOSED");
            }
        }
        else
        {
            error = true;
            System.out.println("BLOCK: ERROR IN POSITION: " + preanalysis.position + " | LEFT PARENTHESIS IS NOT CLOSED");
        }
    }
    void BLOCK_DECL()
    {
        if(error) return;
        if(preanalysis.equals(LBRA)) {
            match(LBRA);
            DECLARATION();
            BLOCK_DECL();
        }
    }
    void VAR_INIT()
    {
        if(error) return;
        if(preanalysis.equals(EQUAL))
        {
            match(EQUAL);
            EXPRESSION();
        }

    }
    void EXPRESSION()
    {
        if(error) return;
        if(preanalysis.equals(ID) || preanalysis.equals(LPAR) || preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(SUPER)) {
            ASSIGNMENT();
        }
        else
        {
            error = true;
            System.out.println("EXPRESSION: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void ASSIGNMENT()
    {
        if(error) return;

        if(preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            LOGIC_OR();
            ASSIGNMENT_OPC();
        }
        else
        {
            error = true;
            System.out.println("ASSIGNMENT: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void LOGIC_OR()
    {
        if(error) return;
        if(preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            LOGIC_AND();
            LOGIC_OR_2();
        }
        else
        {
            error = true;
            System.out.println("LOGIC_OR: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void LOGIC_OR_2()
    {
        if(error) return;
        if(preanalysis.equals(OR))
        {
            match(OR);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }
    void ASSIGNMENT_OPC()
    {
        if(error) return;
        if(preanalysis.equals(EQUAL))
        {
            match(EQUAL);
            EXPRESSION();
        }
    }
    void LOGIC_AND()
    {
        if(error) return;

        if(preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            EQUALITY();
            LOGIC_AND_2();
        }
        else
        {
            error = true;
            System.out.println("LOGIC AND: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void LOGIC_AND_2()
    {
        if(error) return;
        if(preanalysis.equals(AND))
        {
            match(AND);
            EQUALITY();
            LOGIC_AND_2();
        }
    }
    void EQUALITY()
    {
        if(error) return;

        if(preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            COMPARISON();
            EQUALITY_2();
        }
        else
        {
            error = true;
            System.out.println("EQUALITY: ERROR IN POSITION: " + preanalysis.position);
        }
    }
    void EQUALITY_2()
    {
        if(error) return;
        if(preanalysis.equals(NEQUAL))
        {
            match(NEQUAL);
            COMPARISON();
            EQUALITY_2();
        }
        else if(preanalysis.equals(EQUALS))
        {
            match(EQUALS);
            COMPARISON();
            EQUALITY_2();
        }
    }
    void COMPARISON()
    {
        if(error) return;
        if(preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            TERM();
            COMPARISON_2();
        }

        else
        {
            error = true;
            System.out.println("COMPARISON: ERROR IN POSITION: " + preanalysis.position + " | ");
        }
    }
    void COMPARISON_2()
    {
        if(error) return;
        if(preanalysis.equals(LTHAN))
        {
            match(LTHAN);
            TERM();
            COMPARISON_2();
        }
        else if(preanalysis.equals(LTHANE))
        {
            match(LTHANE);
            TERM();
            COMPARISON_2();
        }
        else if(preanalysis.equals(GTHAN))
        {
            match(GTHAN);
            TERM();
            COMPARISON_2();
        }
        else if(preanalysis.equals(GTHANE))
        {
            match(GTHANE);
            TERM();
            COMPARISON_2();
        }

    }
    void TERM()
    {
        if(error) return;
        if(preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            FACTOR();
            TERM_2();
        }
        else
        {
            error = true;
            System.out.println("TERM: ERROR IN POSITION: " + preanalysis.position + " | ");
        }
    }
    void TERM_2()
    {
        if(error) return;
        if(preanalysis.equals(HYPHEN))
        {
            match(HYPHEN);
            FACTOR();
            TERM_2();
        }
        else if(preanalysis.equals(PLUS))
        {
            match(PLUS);
            FACTOR();
            TERM_2();
        }
    }
    void FACTOR()
    {
        if(error) return;
        if(preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            UNARY();
            FACTOR_2();
        }
        else
        {
            error = true;
            System.out.println("FACTOR: ERROR IN POSITION: " + preanalysis.position + " | ");
        }
    }
    void FACTOR_2()
    {
        if(error) return;
        if(preanalysis.equals(SLASH))
        {
            match(SLASH);
            UNARY();
            FACTOR_2();
        }
        else if(preanalysis.equals(AST))
        {
            match(AST);
            UNARY();
            FACTOR_2();
        }
    }
    void UNARY()
    {
        if(error) return;
        if(preanalysis.equals(EXMA))
        {
            match(EXMA);
            UNARY();
        }
        else if(preanalysis.equals(HYPHEN))
        {
            match(HYPHEN);
            UNARY();
        }
        else if(preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            CALL();
        }
        else
        {
            error = true;
            System.out.println("UNARY: ERROR IN POSITION: " + preanalysis.position + " | ");
        }
    }
    void CALL()
    {
        if(error) return;
        if(preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            PRIMARY();
            CALL_2();
        }
        else
        {
            error = true;
            System.out.println("CALL: ERROR IN POSITION: " + preanalysis.position + " | ");
        }
    }
    void CALL_2()
    {
        if(error) return;
        if(preanalysis.equals(LPAR))
        {
            match(LPAR);
            ARGUMENTS_OPC();
            if(preanalysis.equals(RPAR))
            {
                match(RPAR);
                CALL_2();
            }
            else
            {
                error = true;
                System.out.println("CALL_2 : ERROR IN POSITION: " + preanalysis.position + "| PARENTHESIS NOT CLOSED ");
            }
        }
        else if(preanalysis.equals(FS))
        {
            match(FS);
            if(preanalysis.equals(ID))
            {
                match(ID);
                CALL_2();
            }
        }
    }
    void PRIMARY()
    {
        if(error) return;
        if(preanalysis.equals(TRUE))
        {
            match(TRUE);
        }
        else if(preanalysis.equals(FALSE))
        {
            match(FALSE);
        }
        else if(preanalysis.equals(NULL))
        {
            match(NULL);
        }
        else if(preanalysis.equals(THIS))
        {
            match(THIS);
        }
        else if(preanalysis.equals(NUMBER))
        {
            match(NUMBER);
        }
        else if(preanalysis.equals(STRING))
        {
            match(STRING);
        }
        else if(preanalysis.equals(ID))
        {
            match(ID);
        }
        else if(preanalysis.equals(LPAR))
        {
            match(LPAR);
            EXPRESSION();
            if(preanalysis.equals(RPAR))
            {
                match(RPAR);
            }
            else
            {
                error = true;
                System.out.println("PRIMARY: ERROR IN POSITION: " + preanalysis.position + " | PARENTHESIS IS NOT CLOSED");
            }
        }
        else if(preanalysis.equals(SUPER))
        {
            match(SUPER);
            if(preanalysis.equals(FS))
            {
                match(FS);
                if(preanalysis.equals(ID))
                {
                    match(ID);
                }
                else
                {
                    error = true;
                    System.out.println("PRIMARY: ERROR IN POSITION: " + preanalysis.position + " | ID IS MISISNG");
                }
            }
            else
            {
                error = true;
                System.out.println("PRIMARY: ERROR IN POSITION: " + preanalysis.position + " | MAYBE DOT IS MISSING");
            }
        }
        else
        {
            error = true;
            System.out.println("PRIMARY: ERROR IN POSITION: " + preanalysis.position + " | ");
        }
    }
    void ARGUMENTS_OPC()
    {
        if(error) return;
        if(preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            ARGUMENTS();
        }
    }
    void ARGUMENTS()
    {
        if(error) return;
        if(preanalysis.equals(EXMA) || preanalysis.equals(HYPHEN) || preanalysis.equals(TRUE) || preanalysis.equals(FALSE) || preanalysis.equals(NULL) || preanalysis.equals(THIS) || preanalysis.equals(NUMBER) || preanalysis.equals(STRING) || preanalysis.equals(ID) || preanalysis.equals(LPAR) ||  preanalysis.equals(SUPER))
        {
            EXPRESSION();
            ARGUMENTS_2();
        }
        else
        {
            error = true;
            System.out.println("ARGUMENTS: ERROR IN POSITION: " + preanalysis.position + " | ");
        }
    }
    void ARGUMENTS_2()
    {
        if(error) return;
        if(preanalysis.equals(COMMA))
        {
            match(COMMA);
            EXPRESSION();
            ARGUMENTS_2();
        }
    }
    void match(Token t){
        if(error) return;

        if(preanalysis.type == t.type){
            i++;
            //System.out.println("I : " + i);
            preanalysis= tokens.get(i);
        }
        else{
            error = true;
            System.out.println("ERROR IN POSITION " + preanalysis.position + ". EXPECTED  " + t.type);

        }
    }
}
