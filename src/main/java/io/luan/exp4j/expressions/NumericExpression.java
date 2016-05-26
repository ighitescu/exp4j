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

package io.luan.exp4j.expressions;

import io.luan.exp4j.Expression;

/**
 * Base interface of the Four value value expressions:
 * Number Expression (Can be Long or BigDecimal)
 * Fraction Expression TODO: A Fraction have both top and bottom as integers
 * Complex Expression TODO: Complex Expression
 * All math should be done in a manner than preserves precision as much as possible.
 */
public interface NumericExpression extends Expression {

    NumericExpression add(NumericExpression other);

    int compareTo(NumericExpression other);

    NumericExpression divide(NumericExpression other);

    boolean equate(NumericExpression other);

    NumericExpression multiply(NumericExpression other);

    NumericExpression negate();

    NumericExpression power(NumericExpression other);

    NumericExpression subtract(NumericExpression other);
}
