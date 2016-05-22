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

package io.luan.exp4j;

public enum ExpressionType {
    None(0),

    Rational(2),
    Complex(4),
    Number(9),

    Variable(11),
    Parameter(12),
    Constant(13),

    Function(21),

    Sum(101),
    Product(102),
    Power(103),

    Conditional(201),

    BooleanValue(1001),
    LogicalAnd(2001),
    LogicalOr(2002),
    LogicalNot(2003),

    Comparison(3001);

    private int value;

    ExpressionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
