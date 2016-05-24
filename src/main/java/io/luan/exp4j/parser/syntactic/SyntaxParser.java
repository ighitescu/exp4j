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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static io.luan.exp4j.parser.syntactic.SyntaxParserUtil.getAssociativity;
import static io.luan.exp4j.parser.syntactic.SyntaxParserUtil.getPrecedence;

/**
 * Parses a string into an syntax tree
 * Each parser is one-use only.
 * <p>
 * The algorithm used: Shunting Yard algorithm
 */
public class SyntaxParser {

    private static final SyntaxParserException ArgEx = new SyntaxParserException("The string cannot be parsed.");
    private static final SyntaxParserException BracketEx = new SyntaxParserException("Bracket Mismatch.");

    private Lexer lexer;

    private Stack<Token> opStack;
    private Stack<SyntaxNode> queue;

    // Keeps track of all func's operands
    private Map<Token, Stack<SyntaxNode>> funcOperandsMap;

    // Keeps track of the funcs
    private Stack<Token> funcStack;

    // Key = operator, value = func, could be null if no func
    private Map<Token, Token> opFuncMap;

    private int bracketCount;
    private Token lastToken;

    public SyntaxParser(String expression) {
        lexer = new Lexer(expression);
        opStack = new Stack<>();
        queue = new Stack<>();
        funcStack = new Stack<>();
        funcOperandsMap = new HashMap<>();
        opFuncMap = new HashMap<>();

        bracketCount = 0;
        lastToken = Token.EMPTY;
    }


    public SyntaxNode parse() {

        Token token = lexer.take();
        while (token != null) {
            if (Config.DEBUG_PARSER) {
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
            handleTopOperator();
        }

        // There should only be the root node left on the queue;
        if (queue.size() != 1) {
            throw new SyntaxParserException("There should be only one root node");
        }

        return queue.firstElement();
    }

    private SyntaxNode buildSyntaxNode(Token token) {
        SyntaxNode syntaxNode = new SyntaxNode(token);
        int operandCount = getOperandCount(syntaxNode);
        Stack<SyntaxNode> stack = new Stack<>();
        for (int i = 0; i < operandCount; i++) {
            SyntaxNode node = this.queue.pop();
            stack.push(node);
        }

        // Handle func params
        Token funcToken = opFuncMap.get(token);
        if (funcToken != null) {
            Stack<SyntaxNode> funcOpStack = funcOperandsMap.get(funcToken);
            for (int i = 0; i < operandCount; i++) {
                funcOpStack.pop();
            }
        }

        while (stack.size() > 0) {
            syntaxNode.getChildNodes().add(stack.pop());
        }

        if (token.getType() == TokenType.Function) {
//            this.funcList.remove(this.funcList.size() - 1);
//            this.funcOperands.remove(this.funcOperands.size() - 1);
        }
        return syntaxNode;
    }

    private int getOperandCount(SyntaxNode node) {
        switch (node.getType()) {
            case Function:
                List<SyntaxNode> list = funcOperandsMap.get(node.getToken());
                return list.size();

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
            case Dot:
                return 2;
            case UnaryNegative:
            case UnaryPositive:
            case LogicalNot:
            case BitwiseNot:
                return 1;
            case Number:
            case Variable:
                return 0;
        }
        throw new SyntaxParserException("Unrecognized SyntaxNode type: " + node.getType());
    }

    private void handleTopOperator() {
        Token top = opStack.pop();
        SyntaxNode node = buildSyntaxNode(top);
        queue.push(node);

        Token funcToken = opFuncMap.get(top);
        if (funcToken != null) {
            Stack<SyntaxNode> operandStack = funcOperandsMap.get(funcToken);
            operandStack.push(node);
        }
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

        while (opStack.size() > 0) {
            if (opStack.peek().getType() == TokenType.LeftParen) {
                break;
            } else {
                handleTopOperator();
            }
        }

        // comma is added whenever it's encountered. The comma count should
        // always be added to the last func.
//        int index = funcOperands.size() - 1;
//        int count = funcOperands.get(index);
//        funcOperands.set(index, count + 1);
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

        opStack.push(token);

        if (!funcStack.isEmpty()) {
            Token funcToken = funcStack.peek();
            Stack<SyntaxNode> opStack = funcOperandsMap.get(funcToken);
            opStack.push(new SyntaxNode(token));
        }
        // also put into func stack;
        funcStack.push(token);
        funcOperandsMap.put(token, new Stack<>());

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

        opStack.push(token);
        if (funcStack.size() > 0) {
            opFuncMap.put(token, funcStack.peek());
        }
        bracketCount++;
        lastToken = token;
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
        queue.push(node);
        if (!funcStack.isEmpty()) {
            Token funcToken = funcStack.peek();
            Stack<SyntaxNode> opStack = funcOperandsMap.get(funcToken);
            opStack.push(node);
        }
        lastToken = token;
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

                handleTopOperator();
            }
            opStack.push(token);
            if (funcStack.size() > 0) {
                opFuncMap.put(token, funcStack.peek());
            }
            lastToken = token;
        } else {
            if (token.getType() == TokenType.Minus) {
                Token unaryToken = new Token(TokenType.UnaryNegative, "-");
                opStack.push(unaryToken);
                if (funcStack.size() > 0) {
                    opFuncMap.put(unaryToken, funcStack.peek());
                }
                lastToken = unaryToken;
            } else if (token.getType() == TokenType.Plus) {
                Token unaryToken = new Token(TokenType.UnaryPositive, "+");
                opStack.push(unaryToken);
                if (funcStack.size() > 0) {
                    opFuncMap.put(unaryToken, funcStack.peek());
                }
                lastToken = unaryToken;
            } else if (token.getType() == TokenType.LogicalNot) {
                Token unaryToken = new Token(TokenType.LogicalNot, "!");
                opStack.push(unaryToken);
                if (funcStack.size() > 0) {
                    opFuncMap.put(unaryToken, funcStack.peek());
                }
                lastToken = unaryToken;
            } else {
                throw new SyntaxParserException("An non-unary operator cannot go after an operator");
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
                handleTopOperator();
            }
        }
        if ((opStack.size() > 0) && (opStack.peek().getType() == TokenType.Function)) {
            handleTopOperator();
            funcStack.pop();
        }
        bracketCount--;
        lastToken = token;
    }

    private void parseToken(Token token) {
        switch (token.getType()) {
            case Integer:
            case Decimal:
                parseNumber(token);
                break;
            case Variable:
                parseVariable(token);
                break;
            case Function:
                parseFunction(token);
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
            case Dot:
                parseOperator(token);
                break;
            default:
                throw new SyntaxParserException("Unsupported TokenType: " + token.getType());
        }
    }

    /**
     * Variable, Parameter, Constant
     * <p>
     * Cannot go after:
     * - Operand
     * - RightParen
     */
    private void parseVariable(Token token) {
        if (lastToken.getType().isOperand()) {
            throw ArgEx;
        }
        if (lastToken.getType() == TokenType.RightParen) {
            throw ArgEx;
        }

        SyntaxNode node = buildSyntaxNode(token);
        queue.push(node);
        if (!funcStack.isEmpty()) {
            Token funcToken = funcStack.peek();
            Stack<SyntaxNode> opStack = funcOperandsMap.get(funcToken);
            opStack.push(node);
        }
        lastToken = token;
    }


    enum AssociativityType {
        Left, Right, None
    }
}
