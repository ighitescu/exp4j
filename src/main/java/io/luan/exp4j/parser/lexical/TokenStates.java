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

class TokenStates {

    static final TokenState Start = TokenState.nonTerminal("Start");
    static final TokenState Error = TokenState.error("Error");
    static final TokenState Integer = TokenState.terminal("Integer", TokenType.Integer);
    static final TokenState Dot = TokenState.terminal("Dot", TokenType.Dot);
    static final TokenState IntegerDot = TokenState.nonTerminal("IntegerDot");
    static final TokenState Decimal = TokenState.terminal("Decimal", TokenType.Decimal);
    static final TokenState DecimalExpPartial = TokenState.nonTerminal("DecimalExpPartial");
    static final TokenState DecimalExpPlusMinusPartial = TokenState.nonTerminal("DecimalExpPlusMinusPartial");
    static final TokenState DecimalExponent = TokenState.terminal("DecimalExponent", TokenType.Decimal);

    static final TokenState Variable = TokenState.terminal("Variable", TokenType.Variable);
    static final TokenState Function = TokenState.terminal("Function", TokenType.Function);

    static final TokenState Plus = TokenState.terminal("Plus", TokenType.Plus);
    static final TokenState Minus = TokenState.terminal("Minus", TokenType.Minus);
    static final TokenState Asterisk = TokenState.terminal("Asterisk", TokenType.Asterisk);
    static final TokenState Slash = TokenState.terminal("Slash", TokenType.Slash);
    static final TokenState Percent = TokenState.terminal("Percent", TokenType.Percent);
    static final TokenState Caret = TokenState.terminal("Power", TokenType.Caret);
    static final TokenState LeftParen = TokenState.terminal("LeftParen", TokenType.LeftParen);
    static final TokenState RightParen = TokenState.terminal("RightParen", TokenType.RightParen);
    static final TokenState Comma = TokenState.terminal("Comma", TokenType.Comma);
    static final TokenState Equal = TokenState.terminal("Equal", TokenType.Equal);
    static final TokenState GreaterThan = TokenState.terminal("GreaterThan", TokenType.GreaterThan);
    static final TokenState GreaterThanOrEqual = TokenState.terminal("GreaterThanOrEqual", TokenType.GreaterThanOrEqual);
    static final TokenState LessThan = TokenState.terminal("LessThan", TokenType.LessThan);
    static final TokenState LessThanOrEqual = TokenState.terminal("LessThanOrEqual", TokenType.LessThanOrEqual);
    static final TokenState Equate = TokenState.terminal("Equate", TokenType.Equate);
    static final TokenState LogicalNot = TokenState.terminal("LogicalNot", TokenType.LogicalNot);
    static final TokenState NotEquate = TokenState.terminal("NotEquate", TokenType.NotEquate);
    static final TokenState BitwiseAnd = TokenState.terminal("BitwiseAnd", TokenType.BitwiseAnd);
    static final TokenState LogicalAnd = TokenState.terminal("LogicalAnd", TokenType.LogicalAnd);
    static final TokenState BitwiseOr = TokenState.terminal("BitwiseOr", TokenType.BitwiseOr);
    static final TokenState LogicalOr = TokenState.terminal("LogicalOr", TokenType.LogicalOr);
    static final TokenState TernaryQuestion = TokenState.terminal("TernaryQuestion", TokenType.QuestionMark);
    static final TokenState TernaryColon = TokenState.terminal("TernaryColon", TokenType.Colon);

    static {
        registerTransitions();
    }

    static void registerTransitions() {
        Start.addTransition(c -> Character.isDigit(c), TokenStates.Integer);
        Start.addTransition(c -> Character.isLetter(c), TokenStates.Variable);
        //Start.addTransition(c -> c == '_', TokenStates.Variable);	// Note: Do not allow initial underscore.
        Start.addTransition(c -> c == '.', TokenStates.Dot);
        Start.addTransition(c -> c == '+', TokenStates.Plus);
        Start.addTransition(c -> c == '-', TokenStates.Minus);
        Start.addTransition(c -> c == '*', TokenStates.Asterisk);
        Start.addTransition(c -> c == '/', TokenStates.Slash);
        Start.addTransition(c -> c == '%', TokenStates.Percent);
        Start.addTransition(c -> c == '^', TokenStates.Caret);
        Start.addTransition(c -> c == '(', TokenStates.LeftParen);
        Start.addTransition(c -> c == ')', TokenStates.RightParen);
        Start.addTransition(c -> c == ',', TokenStates.Comma);
        Start.addTransition(c -> c == '=', TokenStates.Equal);

        Integer.addTransition(c -> Character.isDigit(c), TokenStates.Integer);
        Integer.addTransition(c -> c == '.', TokenStates.IntegerDot);
        Integer.addTransition(c -> c == 'e' || c == 'E', TokenStates.DecimalExpPartial);
        Integer.addFatal(c -> Character.isLetter(c) && c != 'e' && c != 'E'); // Fatal

        IntegerDot.addTransition(c -> Character.isDigit(c), TokenStates.Decimal);
        IntegerDot.addFatal(c -> Character.isLetter(c) && c != 'e' && c != 'E'); // Fatal

        Dot.addTransition(c -> Character.isDigit(c), TokenStates.Decimal);

        Decimal.addTransition(c -> Character.isDigit(c), TokenStates.Decimal);
        Decimal.addTransition(c -> c == 'e' || c == 'E', TokenStates.DecimalExpPartial);
        Decimal.addFatal(c -> Character.isLetter(c) && c != 'e' && c != 'E'); // Fatal

        DecimalExpPartial.addTransition(c -> Character.isDigit(c), TokenStates.DecimalExponent);
        DecimalExpPartial.addTransition(c -> c == '+' || c == '-', TokenStates.DecimalExpPlusMinusPartial);
        DecimalExpPartial.addFatal(c -> Character.isLetter(c) && c != 'e' && c != 'E'); // Fatal

        DecimalExpPlusMinusPartial.addTransition(c -> Character.isDigit(c), TokenStates.DecimalExponent);
        DecimalExpPlusMinusPartial.addFatal(c -> Character.isLetter(c) && c != 'e' && c != 'E'); // Fatal

        DecimalExponent.addTransition(c -> Character.isDigit(c), TokenStates.DecimalExponent);
        DecimalExponent.addFatal(c -> Character.isLetter(c) && c != 'e' && c != 'E'); // Fatal

        Variable.addTransition(c -> Character.isLetterOrDigit(c), TokenStates.Variable);
        Variable.addTransition(c -> c == '_', TokenStates.Variable);
        Variable.addTransition(c -> c == '(', TokenStates.Function, false);    // Special Case: If ( is encountered, the previous token is a function call token. should NOT consume the next char.

        Start.addTransition(c -> c == '>', TokenStates.GreaterThan);
        GreaterThan.addTransition(c -> c == '=', TokenStates.GreaterThanOrEqual);

        Start.addTransition(c -> c == '<', TokenStates.LessThan);
        LessThan.addTransition(c -> c == '=', TokenStates.LessThanOrEqual);

        Equal.addTransition(c -> c == '=', TokenStates.Equate);

        Start.addTransition(c -> c == '!', TokenStates.LogicalNot);
        LogicalNot.addTransition(c -> c == '=', NotEquate);

        Start.addTransition(c -> c == '&', BitwiseAnd);
        BitwiseAnd.addTransition(c -> c == '&', LogicalAnd);

        Start.addTransition(c -> c == '|', BitwiseOr);
        BitwiseOr.addTransition(c -> c == '|', LogicalOr);

        Start.addTransition(c -> c == '?', TernaryQuestion);
        Start.addTransition(c -> c == ':', TernaryColon);
    }
}
