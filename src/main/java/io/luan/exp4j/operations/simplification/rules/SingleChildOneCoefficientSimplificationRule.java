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

package io.luan.exp4j.operations.simplification.rules;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.value.NumberExpression;

/// <summary>
/// Applies to SUM node, single child with coefficient == 1
/// </summary>
public class SingleChildOneCoefficientSimplificationRule implements SimplificationRule {
    public Expression apply(Expression original) {
        Expression simplified = ((SumExpression) original).getOperands()[0];
        return simplified;
    }

    public boolean canApply(Expression original) {
        if (original.getType() == ExpressionType.Sum) {
            SumExpression SumExp = (SumExpression) original;
            return SumExp.getOperands().length == 1 && SumExp.getCoefficients()[0].equate(NumberExpression.One);
        }
        return false;
    }
}
