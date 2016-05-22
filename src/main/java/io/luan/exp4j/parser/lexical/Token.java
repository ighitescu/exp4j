package io.luan.exp4j.parser.lexical;

public class Token {
    public static final Token EMPTY = new Token(TokenType.None, "");

    private TokenType type;
    private String text;

    public Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + "[" + text + "]";
    }
}
