package io.luan.exp4j.parser.syntactic;

import io.luan.exp4j.parser.lexical.TokenType;

class SyntaxParserUtil {

    static SyntaxParser.AssociativityType getAssociativity(TokenType tokenType) {
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
            case Dot:
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

    /**
     * Smaller has higher priority
     */
    static int getPrecedence(TokenType tokenType) {
        switch (tokenType) {
            case Function:
                return 1;
            case Dot:
                return 2;
            case UnaryNegative:
            case UnaryPositive:
            case LogicalNot:
            case BitwiseNot:
                return 3;
            case Caret: // Power
                return 4;
            case Asterisk: // Multiply
            case Slash: // Divide
            case Percent: // Mod
                return 5;
            case Plus: // Add
            case Minus: // Subtract
                return 6;
            case GreaterThan:
            case GreaterThanOrEqual:
            case LessThan:
            case LessThanOrEqual:
                return 7;
            case Equate: // ==
            case NotEquate: // !=
                return 8;
            case BitwiseAnd: // &
                return 9;
            case BitwiseOr: // |
                return 10;
            case LogicalAnd: // &&
                return 11;
            case LogicalOr: // ||
                return 12;

            case QuestionMark:// conditional ? :
            case Colon:// conditional ? :
                return 15;

            case Equal: // '='
                return 20;
            default:
                return 99;
        }
    }

    static SyntaxNodeType getSyntaxType(TokenType tokenType) {
        switch (tokenType) {
            case Integer:
            case Decimal:
                return SyntaxNodeType.Number;
            case Variable:
                return SyntaxNodeType.Variable;
            case Function:
                return SyntaxNodeType.Function;
            case Dot:
                return SyntaxNodeType.Dot;
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
            case UnaryPositive:
                return SyntaxNodeType.UnaryPositive;
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

        throw new SyntaxParserException("Unsupported TokenType: " + tokenType);
    }
}
