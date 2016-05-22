package io.luan.exp4j.parser.syntactic;

import io.luan.exp4j.parser.lexical.Token;
import io.luan.exp4j.parser.lexical.TokenType;

import java.util.ArrayList;
import java.util.List;

public class SyntaxNode {

    private SyntaxNodeType type;
    private List<SyntaxNode> childNodes;
    private Token token;
    private String text;

    public SyntaxNode(Token token) {
        this.type = getSyntaxType(token.getType());
        this.token = token;
        this.text = token.getText();
        this.childNodes = new ArrayList<>();
    }

    private static SyntaxNodeType getSyntaxType(TokenType tokenType) {
        switch (tokenType) {
            case Integer:
            case Decimal:
                return SyntaxNodeType.Number;
            case Variable:
                return SyntaxNodeType.Variable;
            case Parameter:
                return SyntaxNodeType.Parameter;
            case Constant:
                return SyntaxNodeType.Constant;
            case Function:
                return SyntaxNodeType.Function;
            case Plus:
                return SyntaxNodeType.BinaryAdd;
            case Minus:
                return SyntaxNodeType.BinarySubtract;
            case Asterisk:
                return SyntaxNodeType.BinaryMultiply;
            case Slash:
                return SyntaxNodeType.BinaryDivide;
            case Caret:
                return SyntaxNodeType.BinaryPower;
            case UnaryNegative:
                return SyntaxNodeType.UnaryNegative;
            case GreaterThan:
                return SyntaxNodeType.GreaterThan;
            case GreaterThanOrEqual:
                return SyntaxNodeType.GreaterThanOrEqual;
            case LessThan:
                return SyntaxNodeType.LessThan;
            case LessThanOrEqual:
                return SyntaxNodeType.LessThanOrEqual;
            case Equate:
                return SyntaxNodeType.Equal;
            case NotEquate:
                return SyntaxNodeType.NotEqual;
            case LogicalNot:
                return SyntaxNodeType.LogicalNot;
            case LogicalAnd:
                return SyntaxNodeType.LogicalAnd;
            case LogicalOr:
                return SyntaxNodeType.LogicalOr;
            case QuestionMark:
                return SyntaxNodeType.TernaryQuestion;
            case Colon:
                return SyntaxNodeType.TernaryColon;
        }

        throw new ParserException("Unsupported TokenType: " + tokenType);
    }

    public List<SyntaxNode> getChildNodes() {
        return childNodes;
    }

    public String getText() {
        return text;
    }

    public Token getToken() {
        return token;
    }

    public SyntaxNodeType getType() {
        return type;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        toString(builder, 0);
        return builder.toString();
    }

    private void toString(StringBuilder builder, int depth) {
        for (int i = 0; i < depth; i++) {
            builder.append("  ");
        }

        builder.append("[SYNTAX] ");
        builder.append(getType());
        builder.append('[');
        builder.append(getText());
        builder.append(']');
        builder.append('\n');

        for (SyntaxNode item : childNodes) {
            item.toString(builder, depth + 1);
        }
    }
}
