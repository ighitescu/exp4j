package io.luan.exp4j.parser.syntactic;

import io.luan.exp4j.parser.lexical.TokenType;

public class SyntaxParserUtil {

    /**
     * 越小越优先
     */
    public static int getPrecedence(TokenType tokenType) {
        switch (tokenType) {
            case Function:
                return 1;
            case UnaryNegative:
            case UnaryPositive:
            case LogicalNot:
            case BitwiseNot:
                return 2;
            case Caret: // Power
                return 3;
            case Asterisk: // Multiply
            case Slash: // Divide
            case Percent: // Mod
                return 4;
            case Plus: // Add
            case Minus: // Subtract
                return 5;
            case GreaterThan:
            case GreaterThanOrEqual:
            case LessThan:
            case LessThanOrEqual:
                return 6;
            case Equate: // ==
            case NotEquate: // !=
                return 7;
            case BitwiseAnd: // &
                return 8;
            case BitwiseOr: // |
                return 9;
            case LogicalAnd: // &&
                return 10;
            case LogicalOr: // ||
                return 11;

            case QuestionMark:// conditional ? :
            case Colon:// conditional ? :
                return 15;

            case Equal: // '='
                return 20;
            default:
                return 99;
        }
    }

    public static SyntaxParser.AssociativityType getAssociativity(TokenType tokenType) {
        switch (tokenType) {
            case Asterisk:
            case Slash:
            case Plus:
            case Minus:
            case GreaterThan:
            case GreaterThanOrEqual:
            case LessThan:
            case LessThanOrEqual:
            case Equate:
            case NotEquate:
            case LogicalAnd:
            case LogicalOr:
                return SyntaxParser.AssociativityType.Left;
            case UnaryNegative:
            case UnaryPositive:
            case Caret:
            case LogicalNot:
            case BitwiseNot:
            case QuestionMark:
            case Colon:
                return SyntaxParser.AssociativityType.Right;
            default:
                return SyntaxParser.AssociativityType.None;
        }
    }
}
