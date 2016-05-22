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

public class TokenStates {

    public static final TokenState Start = TokenState.CreateStart("Start");
    public static final TokenState Error = TokenState.CreateError("Error");
    public static final TokenState Integer = TokenState.CreateTerminal("Integer").withTokenType(TokenType.Integer);
    public static final TokenState IntegerDot = TokenState.CreateNonTerminal("IntegerDot");
    public static final TokenState Decimal = TokenState.CreateTerminal("Decimal").withTokenType(TokenType.Decimal);
    public static final TokenState DecimalExpPartial = TokenState.CreateNonTerminal("DecimalExpPartial");
    public static final TokenState DecimalExpPlusMinusPartial = TokenState.CreateNonTerminal("DecimalExpPlusMinusPartial");
    public static final TokenState DecimalExponent = TokenState.CreateTerminal("DecimalExponent").withTokenType(TokenType.Decimal);
    public static final TokenState Variable = TokenState.CreateTerminal("Variable").withTokenType(TokenType.Variable);
    public static final TokenState VariableBracket = TokenState.CreateNonTerminal("VariableBracket");
    public static final TokenState VariableFull = TokenState.CreateTerminal("VariableFull").withTokenType(TokenType.Variable);
    public static final TokenState ParameterStart = TokenState.CreateNonTerminal("ParameterStart");
    public static final TokenState Parameter = TokenState.CreateTerminal("Parameter").withTokenType(TokenType.Parameter);
    public static final TokenState ConstantStart = TokenState.CreateNonTerminal("ConstantStart");
    public static final TokenState Constant = TokenState.CreateTerminal("Constant").withTokenType(TokenType.Constant);
    public static final TokenState Function = TokenState.CreateTerminal("Function").withTokenType(TokenType.Function);
    public static final TokenState Plus = TokenState.CreateTerminal("Plus").withTokenType(TokenType.Plus);
    public static final TokenState Minus = TokenState.CreateTerminal("Minus").withTokenType(TokenType.Minus);
    public static final TokenState Asterisk = TokenState.CreateTerminal("Asterisk").withTokenType(TokenType.Asterisk);
    public static final TokenState Slash = TokenState.CreateTerminal("Slash").withTokenType(TokenType.Slash);
    public static final TokenState Percent = TokenState.CreateTerminal("Percent").withTokenType(TokenType.Percent);
    public static final TokenState Caret = TokenState.CreateTerminal("Power").withTokenType(TokenType.Caret);
    public static final TokenState LeftParen = TokenState.CreateTerminal("LeftParen").withTokenType(TokenType.LeftParen);
    public static final TokenState RightParen = TokenState.CreateTerminal("RightParen").withTokenType(TokenType.RightParen);
    public static final TokenState Comma = TokenState.CreateTerminal("Comma").withTokenType(TokenType.Comma);
    public static final TokenState Equal = TokenState.CreateTerminal("Equal").withTokenType(TokenType.Equal);
    public static final TokenState GreaterThan = TokenState.CreateTerminal("GreaterThan").withTokenType(TokenType.GreaterThan);
    public static final TokenState GreaterThanOrEqual = TokenState.CreateTerminal("GreaterThanOrEqual").withTokenType(TokenType.GreaterThanOrEqual);
    public static final TokenState LessThan = TokenState.CreateTerminal("LessThan").withTokenType(TokenType.LessThan);
    public static final TokenState LessThanOrEqual = TokenState.CreateTerminal("LessThanOrEqual").withTokenType(TokenType.LessThanOrEqual);
    public static final TokenState Equate = TokenState.CreateTerminal("Equate").withTokenType(TokenType.Equate);
    public static final TokenState LogicalNot = TokenState.CreateTerminal("LogicalNot").withTokenType(TokenType.LogicalNot);
    public static final TokenState NotEquate = TokenState.CreateTerminal("NotEquate").withTokenType(TokenType.NotEquate);
    public static final TokenState BitwiseAnd = TokenState.CreateTerminal("BitwiseAnd").withTokenType(TokenType.BitwiseAnd);
    public static final TokenState LogicalAnd = TokenState.CreateTerminal("LogicalAnd").withTokenType(TokenType.LogicalAnd);
    public static final TokenState BitwiseOr = TokenState.CreateTerminal("BitwiseOr").withTokenType(TokenType.BitwiseOr);
    public static final TokenState LogicalOr = TokenState.CreateTerminal("LogicalOr").withTokenType(TokenType.LogicalOr);
    public static final TokenState TernaryQuestion = TokenState.CreateTerminal("TernaryQuestion").withTokenType(TokenType.QuestionMark);
    public static final TokenState TernaryColon = TokenState.CreateTerminal("TernaryColon").withTokenType(TokenType.Colon);

    static {
        registerTransitions();
    }

    private static void registerTransitions() {
        Start.addTransition(c -> Character.isDigit(c), TokenStates.Integer);
        Start.addTransition(c -> Character.isLetter(c), TokenStates.Variable);
        //Start.addTransition(c -> c == '_', TokenStates.Variable);	// Note: Do not allow initial underscore.
        Start.addTransition(c -> c == '.', TokenStates.IntegerDot);
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
        Start.addTransition(c -> c == '@', TokenStates.ParameterStart);    // Non terminal
        Start.addTransition(c -> c == '#', TokenStates.ConstantStart);        // Non terminal

        Integer.addTransition(c -> Character.isDigit(c), TokenStates.Integer);
        Integer.addTransition(c -> c == '.', TokenStates.IntegerDot);
        Integer.addTransition(c -> c == 'e' || c == 'E', TokenStates.DecimalExpPartial);

        IntegerDot.addTransition(c -> Character.isDigit(c), TokenStates.Decimal);

        Decimal.addTransition(c -> Character.isDigit(c), TokenStates.Decimal);
        Decimal.addTransition(c -> c == 'e' || c == 'E', TokenStates.DecimalExpPartial);

        DecimalExpPartial.addTransition(c -> Character.isDigit(c), TokenStates.DecimalExponent);
        DecimalExpPartial.addTransition(c -> c == '+' || c == '-', TokenStates.DecimalExpPlusMinusPartial);

        DecimalExpPlusMinusPartial.addTransition(c -> Character.isDigit(c), TokenStates.DecimalExponent);

        DecimalExponent.addTransition(c -> Character.isDigit(c), TokenStates.DecimalExponent);

        Variable.addTransition(c -> Character.isLetterOrDigit(c), TokenStates.Variable);
        Variable.addTransition(c -> c == '_', TokenStates.Variable);
        Variable.addTransition(c -> c == '(', TokenStates.Function, false);    // Special Case: If ( is encountered, the previous token is a function call token. should NOT consume the next char.
        Variable.addTransition(c -> c == '{', TokenStates.VariableBracket);

        VariableBracket.addTransition(c -> Character.isLetterOrDigit(c), TokenStates.VariableBracket);
        VariableBracket.addTransition(c -> c == ',', TokenStates.VariableBracket);
        VariableBracket.addTransition(c -> c == '(', TokenStates.VariableBracket);
        VariableBracket.addTransition(c -> c == ')', TokenStates.VariableBracket);
        VariableBracket.addTransition(c -> c == '}', TokenStates.VariableFull);


        ParameterStart.addTransition(c -> Character.isLetter(c), TokenStates.Parameter);

        Parameter.addTransition(c -> Character.isLetterOrDigit(c), TokenStates.Parameter);
        Parameter.addTransition(c -> c == '_', TokenStates.Parameter);

        ConstantStart.addTransition(c -> Character.isLetter(c), TokenStates.Constant);

        Constant.addTransition(c -> Character.isLetterOrDigit(c), TokenStates.Constant);
        Constant.addTransition(c -> c == '_', TokenStates.Constant);

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
