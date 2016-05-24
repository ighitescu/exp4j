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

package io.luan.exp4j.parser.lexical;

public enum TokenType {
    None,

    // Operands
    Integer(TokenTypeType.Operand), // [0-9]+
    Decimal(TokenTypeType.Operand), // [0-9]+.[0-9]*([eE][+-]?[0-9]+)

    Variable(TokenTypeType.Operand), // [a-zA-Z][a-zA-Z0-9]*

    Dot(TokenTypeType.BinaryOperator), // '.'

    // TODO: Should this be operand?
    Function(TokenTypeType.Operand), // [a-zA-Z][a-zA-Z0-9]* but followed by (

    //Unary Operators
    UnaryPositive(TokenTypeType.UnaryOperator), // '+' after a binary operator or nothing
    UnaryNegative(TokenTypeType.UnaryOperator), // '-' after a binary operator or nothing
    LogicalNot(TokenTypeType.UnaryOperator), // '!'
    BitwiseNot(TokenTypeType.UnaryOperator), // '~'

    // Binary Operators
    Plus(TokenTypeType.BinaryOperator), // '+' after an operand or a rightParen
    Minus(TokenTypeType.BinaryOperator), // '-' after an operand or a rightParen
    Asterisk(TokenTypeType.BinaryOperator), // '*' multiply
    Slash(TokenTypeType.BinaryOperator), // '/' divide
    Percent(TokenTypeType.BinaryOperator), // '%' mod operator (not yet implemented)
    Caret(TokenTypeType.BinaryOperator), // '^' power

    GreaterThan(TokenTypeType.BinaryOperator), // >
    GreaterThanOrEqual(TokenTypeType.BinaryOperator), // >=
    LessThan(TokenTypeType.BinaryOperator),// <
    LessThanOrEqual(TokenTypeType.BinaryOperator), // <=
    Equate(TokenTypeType.BinaryOperator), // ==
    NotEquate(TokenTypeType.BinaryOperator), // !=

    LogicalAnd(TokenTypeType.BinaryOperator), // &&
    LogicalOr(TokenTypeType.BinaryOperator), // ||

    BitwiseAnd(TokenTypeType.BinaryOperator), // &
    BitwiseOr(TokenTypeType.BinaryOperator), // |

    // Others
    Equal, // '='
    LeftParen, // '('
    Comma, // ','
    RightParen, // ')'

    // Ternary
    QuestionMark(TokenTypeType.TernaryOperator), // '?'
    Colon(TokenTypeType.TernaryOperator), // ':'
    ;

    private TokenTypeType type;

    TokenType() {
        this(TokenTypeType.Normal);
    }

    TokenType(TokenTypeType type) {

        this.type = type;
    }

    public boolean isOperand() {
        return type == TokenTypeType.Operand;
    }

    public boolean isOperator() {
        return type == TokenTypeType.BinaryOperator || type == TokenTypeType.UnaryOperator
                || type == TokenTypeType.TernaryOperator;
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
