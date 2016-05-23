/*
 * Copyright 2016 Guangmiao Luan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.luan.exp4j.parser.syntactic;

import io.luan.exp4j.Config;
import io.luan.exp4j.parser.lexical.Lexer;
import io.luan.exp4j.parser.lexical.Token;
import io.luan.exp4j.parser.lexical.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Parses a string into an syntax tree
 * Each parser is one-use only.
 * <p>
 * The algorithm used: Shunting Yard algorithm
 */
public class SyntaxParser {

    private static final ParserException ArgEx = new ParserException("The string cannot be parsed.");
    private static final ParserException BracketEx = new ParserException("Bracket Mismatch.");

    private Lexer lexer;

    private Stack<Token> opStack;
    private Stack<SyntaxNode> queue;
    private List<Token> funcList;
    private List<Integer> funcOperands;

    private int bracketCount;
    private Token lastToken;

    public SyntaxParser(String expression) {
        this.lexer = new Lexer(expression);
        this.opStack = new Stack<>();
        this.queue = new Stack<>();
        this.funcList = new ArrayList<>();
        this.funcOperands = new ArrayList<>();

        this.bracketCount = 0;
        this.lastToken = Token.EMPTY;
    }

    /**
     * 越小越优先
     */
    private static int getPrecedence(TokenType tokenType) {
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

    private static AssociativityType getAssociativity(TokenType tokenType) {
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
                return AssociativityType.Left;
            case UnaryNegative:
            case UnaryPositive:
            case Caret:
            case LogicalNot:
            case BitwiseNot:
            case QuestionMark:
            case Colon:
                return AssociativityType.Right;
            default:
                return AssociativityType.None;
        }
    }

    private SyntaxNode buildSyntaxNode(Token token) {
        SyntaxNode syntaxNode = new SyntaxNode(token);
        int operandCount = getOperandCount(syntaxNode);
        Stack<SyntaxNode> stack = new Stack<SyntaxNode>();
        for (int i = 0; i < operandCount; i++) {
            SyntaxNode node = this.queue.pop();
            stack.push(node);
        }
        while (stack.size() > 0) {
            syntaxNode.getChildNodes().add(stack.pop());
        }

        if (token.getType() == TokenType.Function) {
            this.funcList.remove(this.funcList.size() - 1);
            this.funcOperands.remove(this.funcOperands.size() - 1);
        }
        return syntaxNode;
    }

    private int getOperandCount(SyntaxNode node) {
        switch (node.getType()) {
            case Function:
                // String nodeText = node.getToken().getText();
                // String funcText = funcList.get(funcList.size() - 1).getText();
                return this.funcOperands.get(funcOperands.size() - 1) + 1;

            case BinaryAdd:
            case BinarySubtract:
            case BinaryMultiply:
            case BinaryDivide:
            case BinaryMod:
            case BinaryPower:
            case GreaterThan:
            case GreaterThanOrEqual:
            case LessThan:
            case LessThanOrEqual:
            case Equal:
            case NotEqual:
            case LogicalAnd:
            case LogicalOr:
            case TernaryQuestion: // only 2 operands e.g. a ? (x:y)
            case TernaryColon: // only 2 operands e.g. x:y
                return 2;
            case UnaryNegative:
            case LogicalNot:
            case BitwiseNot:
                return 1;
            case Number:
            case Variable:
            case Parameter:
            case Constant:
                return 0;
        }
        throw ArgEx;
    }

    public SyntaxNode parse() {

        Token token = lexer.take();
        while (token != null) {
            if (Config.DEBUG) {
                System.out.println("[TOKEN] " + token);
            }
            parseToken(token);
            token = lexer.take();
        }

        // After all tokens are read, if there is still bracket left, there is mismatch.
        if (bracketCount > 0) {
            throw BracketEx;
        }

        while (opStack.size() > 0) {
            SyntaxNode node = buildSyntaxNode(opStack.pop());
            queue.push(node);
        }

        // There should only be the root node left on the queue;
        if (queue.size() != 1) {
            throw ArgEx;
        }

        return queue.firstElement();
    }

    /**
     * Comma ',': Separate parameters in a function
     * e.g. xxx(a,b,c)
     * <p>
     * Must be within a bracket (
     * <p>
     * Can not go after:
     * - LeftParen
     * - Comma,
     * - Operator
     */
    private void parseComma(Token token) {
        if (bracketCount == 0) {
            throw ArgEx;
        }
        if (lastToken.getType() == TokenType.LeftParen || lastToken.getType() == TokenType.Comma) {
            throw ArgEx;
        }
        if (lastToken.getType().isOperator()) {
            throw ArgEx;
        }

        // comma is added whenever it's encountered. The comma count should
        // always be added to the last func.
        int index = funcOperands.size() - 1;
        int count = funcOperands.get(index);
        funcOperands.set(index, count++);
        lastToken = token;
    }

    /**
     * Function: xxx()
     * <p>
     * Cannot go after:
     * - Operand
     * - RightParen
     */
    private void parseFunction(Token token) {
        if (lastToken.getType().isOperand()) {
            throw ArgEx;
        }
        if (lastToken.getType() == TokenType.RightParen) {
            throw ArgEx;
        }

        this.opStack.push(token);

        // also put into func list
        this.funcList.add(token);
        this.funcOperands.add(0);

        this.lastToken = token;
    }

    /**
     * ID Token: Variable, Parameter, Constant
     * <p>
     * Cannot go after:
     * - Operand
     * - RightParen
     */
    private void parseIdentifier(Token token) {
        if (lastToken.getType().isOperand()) {
            throw ArgEx;
        }
        if (lastToken.getType() == TokenType.RightParen) {
            throw ArgEx;
        }

        SyntaxNode node = buildSyntaxNode(token);
        queue.push(node);
        lastToken = token;
    }

    /**
     * Left Paren '('
     * <p>
     * Cannot go after:
     * - Number
     * - Variable
     * - RightParen
     */
    private void parseLeftParen(Token token) {
        if (lastToken.getType() == TokenType.Integer || lastToken.getType() == TokenType.Decimal) {
            throw ArgEx;
        }
        if (lastToken.getType() == TokenType.Variable) {
            throw ArgEx;
        }
        if (lastToken.getType() == TokenType.RightParen) {
            throw ArgEx;
        }

        this.opStack.push(token);
        this.bracketCount++;
        this.lastToken = token;
    }

    /**
     * Number: Integer or Decimal
     * <p>
     * Cannot go after:
     * - Operand
     * - RightParen
     */
    private void parseNumber(Token token) {
        if (lastToken.getType().isOperand()) {
            throw ArgEx;
        }
        if (lastToken.getType() == TokenType.RightParen) {
            throw ArgEx;
        }

        SyntaxNode node = buildSyntaxNode(token);
        this.queue.push(node);
        this.lastToken = token;
    }

    /**
     * Operators (Unary or binary)
     * <p>
     * Cannot go after:
     * - Unary Operator
     */
    private void parseOperator(Token token) {
        if (lastToken.getType().isUnaryOperator()) {
            throw ArgEx;
        }

        if (lastToken.getType().isOperand() || lastToken.getType() == TokenType.RightParen) {
            while (opStack.size() > 0) {
                Token top = opStack.peek();
                if (top.getType() == TokenType.LeftParen) {
                    break;
                }
                if (!top.getType().isOperator()) {
                    break;
                }

                if (getPrecedence(token.getType()) < getPrecedence(top.getType())) {
                    break;
                }

                if (getPrecedence(token.getType()) == getPrecedence(top.getType())) {
                    if (getAssociativity(token.getType()) == AssociativityType.Right) {
                        break;
                    }
                }

                SyntaxNode node = buildSyntaxNode(opStack.pop());
                queue.push(node);
            }
            opStack.push(token);
            lastToken = token;
        } else {
            if (token.getType() == TokenType.Minus) {
                Token unaryToken = new Token(TokenType.UnaryNegative, "-");
                opStack.push(unaryToken);
                lastToken = unaryToken;
            } else if (token.getType() == TokenType.LogicalNot) {
                Token unaryToken = new Token(TokenType.LogicalNot, "!");
                opStack.push(unaryToken);
                lastToken = unaryToken;
            } else {
                throw ArgEx;
            }
        }
    }

    /**
     * RightParen ')':
     * <p>
     * Must be able to close brackets
     * <p>
     * Cannot go after:
     * - Comma,
     * - Operator
     */
    private void parseRightParen(Token token) {
        if (bracketCount == 0) {
            throw ArgEx;
        }
        if (lastToken.getType() == TokenType.Comma) {
            throw ArgEx;
        }
        if (lastToken.getType().isOperator()) {
            throw ArgEx;
        }

        while (opStack.size() > 0) {
            if (opStack.peek().getType() == TokenType.LeftParen) {
                opStack.pop();
                break;
            } else {
                SyntaxNode node = buildSyntaxNode(opStack.pop());
                queue.push(node);
            }
        }
        if ((opStack.size() > 0) && (opStack.peek().getType() == TokenType.Function)) {
            SyntaxNode node = buildSyntaxNode(opStack.pop());
            queue.push(node);
        }
        bracketCount--;
        lastToken = token;
    }

    private void parseToken(Token token) {
        switch (token.getType()) {
            case None:
                throw ArgEx;
            case Function:
                parseFunction(token);
                break;
            case Variable:
            case Parameter:
            case Constant:
                parseIdentifier(token);
                break;
            case Integer:
            case Decimal:
                parseNumber(token);
                break;
            case LeftParen:
                parseLeftParen(token);
                break;
            case RightParen:
                parseRightParen(token);
                break;
            case Comma:
                parseComma(token);
                break;
            case Plus:
            case Minus:
            case Asterisk:
            case Slash:
            case Percent:
            case Caret:
            case GreaterThan:
            case GreaterThanOrEqual:
            case LessThan:
            case LessThanOrEqual:
            case Equate:
            case NotEquate:
            case LogicalNot:
            case LogicalAnd:
            case LogicalOr:
            case QuestionMark:
            case Colon:
                parseOperator(token);
                break;
            default:
                throw new ParserException("Unsupported TokenType: " + token.getType());
        }
    }


    private enum AssociativityType {
        Left, Right, None
    }
}
