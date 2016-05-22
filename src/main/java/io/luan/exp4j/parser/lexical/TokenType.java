package io.luan.exp4j.parser.lexical;

public enum TokenType {
    None(0),

    // Operands
    Integer(1, TokenTypeType.Operand), // [0-9]+
    Decimal(2, TokenTypeType.Operand), // [0-9]+.[0-9]*([eE][+-]?[0-9]+)

    Variable(11, TokenTypeType.Operand), // [a-zA-Z][a-zA-Z0-9]*{[a-zA-Z0-9_()}
    Parameter(12, TokenTypeType.Operand), // @[a-zA-Z][a-zA-Z0-9]*
    Constant(13, TokenTypeType.Operand), // #[a-zA-Z][a-zA-Z0-9]*

    // TODO: Should this be operand?
    Function(51, TokenTypeType.Operand), // [a-zA-Z][a-zA-Z0-9]* but followed by (


    //Unary Operators
    UnaryPositive(101, TokenTypeType.UnaryOperator), // '+' after a binary operator or nothing
    UnaryNegative(102, TokenTypeType.UnaryOperator), // '-' after a binary operator or nothing
    LogicalNot(111, TokenTypeType.UnaryOperator), // '!'
    BitwiseNot(121, TokenTypeType.UnaryOperator), // '~'

    // Binary Operators
    Plus(1001, TokenTypeType.BinaryOperator), // '+' after an operand or a rightParen
    Minus(1002, TokenTypeType.BinaryOperator), // '-' after an operand or a rightParen
    Asterisk(1011, TokenTypeType.BinaryOperator), // '*' multiply
    Slash(1012, TokenTypeType.BinaryOperator), // '/' divide
    Percent(1021, TokenTypeType.BinaryOperator), // '%' mod operator (not yet implemented)
    Caret(1031, TokenTypeType.BinaryOperator), // '^' power

    GreaterThan(2001, TokenTypeType.BinaryOperator), // >
    GreaterThanOrEqual(2002, TokenTypeType.BinaryOperator), // >=
    LessThan(2011, TokenTypeType.BinaryOperator),// <
    LessThanOrEqual(2012, TokenTypeType.BinaryOperator), // <=
    Equate(2021, TokenTypeType.BinaryOperator), // ==
    NotEquate(2022, TokenTypeType.BinaryOperator), // !=

    LogicalAnd(3001, TokenTypeType.BinaryOperator), // &&
    LogicalOr(3002, TokenTypeType.BinaryOperator), // ||

    BitwiseAnd(4001, TokenTypeType.BinaryOperator), // &
    BitwiseOr(4002, TokenTypeType.BinaryOperator), // |

    // Others
    Equal(10001), // '='
    LeftParen(10002), // '('
    Comma(10003), // ','
    RightParen(10004), // ')'

    // Ternary
    QuestionMark(20001, TokenTypeType.TernaryOperator), // '?'
    Colon(20002, TokenTypeType.TernaryOperator), // ':'
    ;


    private int value;
    private TokenTypeType type;

    TokenType(int value) {
        this(value, TokenTypeType.Normal);
    }

    TokenType(int value, TokenTypeType type) {
        this.value = value;
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public boolean isBinaryOperator() {
        return type == TokenTypeType.BinaryOperator;
    }

    public boolean isOperand() {
        return type == TokenTypeType.Operand;
    }

    public boolean isOperator() {
        return type == TokenTypeType.BinaryOperator || type == TokenTypeType.UnaryOperator
                || type == TokenTypeType.TernaryOperator;
    }

    public boolean isTernaryOperator() {
        return type == TokenTypeType.TernaryOperator;
    }

    public boolean isUnaryOperator() {
        return type == TokenTypeType.UnaryOperator;
    }

    private enum TokenTypeType {
        Normal,
        Operand,
        UnaryOperator,
        BinaryOperator,
        TernaryOperator
    }
}
