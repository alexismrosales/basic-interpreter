public enum TokenType {
    //Logic operators
    AND,
    OR,
    NOT,
    //Identifier
    ID,
    //Data type
    STR,
    NUMBER,
    //Special Words
    CLASS,
    //Language signs
    LPAR, //-->Left parenthesis (
    RPAR, //-->Right parenthesis )
    LBRA, //-->Left brace {
    RBRA, //->Right brace }
    FS, //-->Full stop .
    COMMA, //-->Comma ,
    SC, //-->Semi colon ;
    HYPHEN, //-->Hyphen -
    PLUS, //-->Plus +
    AST, //-->Asterisk *
    SLASH, //-->Slash /
    EXMA, //-->Exclamation mark !
    EQUALS, //-->Equal sign =
    EQUAL, //-->Equal operator ==
    NEQUAL, //-->Not equal operator !=
    LTHAN, //-->Less than <
    GTHAN, //-->Greater than >
    LTHANE, //-->Less than or equal operator <=
    GTHANE, //-->Great than or equal operator >=
    ERROR,
    //RESERVED WORDS
    FUN,
    IF,
    ELSE,
    FOR,
    WHILE,
    TRUE,
    FALSE,
    RETURN,
    PRINT,
    NULL,
    SUPER,
    VAR,
    THIS,


    EOF
}
