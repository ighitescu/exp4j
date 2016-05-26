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

package io.luan.exp4j.visitors.simplification;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.value.NumberExpression;

import java.util.ArrayList;

/// <summary>
/// Zero Coefficient: 	A term with zero coefficient is removed
/// </summary>
public class ZeroCoefficientSimplificationRule implements SimplificationRule {

    public Expression apply(Expression original) {
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newCoefs = new ArrayList<NumericExpression>();
        SumExpression sumExp = (SumExpression) original;

        for (int i = 0; i < sumExp.getOperands().length; i++) {
            if (!sumExp.getCoefficients()[i].equate(NumberExpression.Zero)) {
                newOprands.add(sumExp.getOperands()[i]);
                newCoefs.add(sumExp.getCoefficients()[i]);
            }
        }

        if (newOprands.size() == 0) {
            return NumberExpression.Zero; // somehow nothing left, return ZERO
        }
        Expression simplified = new SumExpression(newOprands.toArray(new Expression[0]), newCoefs.toArray(new NumericExpression[0]));
        return simplified;
    }

    public boolean canApply(Expression original) {
        if (original.getType() == ExpressionType.Sum) {
            SumExpression sumExp = (SumExpression) original;
            for (NumericExpression exp : sumExp.getCoefficients()) {
                if (exp.equate(NumberExpression.Zero)) {
                    return true;
                }
            }
        }
        return false;
    }
}
