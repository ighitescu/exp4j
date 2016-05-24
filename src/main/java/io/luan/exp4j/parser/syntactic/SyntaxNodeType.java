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

public enum SyntaxNodeType {
    None,
    Number,
    Variable,
    Function,
    Dot,
    UnaryNegative,
    UnaryPositive,
    BinaryAdd,
    BinarySubtract,
    BinaryMultiply,
    BinaryDivide,
    BinaryMod,
    BinaryPower,
    GreaterThan,
    GreaterThanOrEqual,
    LessThan,
    LessThanOrEqual,
    Equal,
    NotEqual,
    LogicalNot,
    LogicalAnd,
    LogicalOr,
    BitwiseNot,
    BitwiseAnd,
    BitwiseOr,
    TernaryQuestion,
    TernaryColon
}
