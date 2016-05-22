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
import io.luan.exp4j.expressions.type.NumberExpression;

import java.util.ArrayList;

/// <summary>
/// Numeric Sum Rule (Applies to Sum)
/// 	All numeric operands should be consolidated to a single numeric node
/// 	Stop if there is one or zero numeric operands, and if exist, the coef is ONE
/// 	If the consolidated type is zero, remove this node.
/// If the sum has no other terms other than a type, return the type node.
/// </summary>
public class NumericSumSimplificationRule implements SimplificationRule {

    public Expression apply(Expression original) {
        SumExpression sumExp = (SumExpression) original;
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newCoefs = new ArrayList<NumericExpression>();

        NumericExpression newNumOprand = NumberExpression.Zero;

        for (int i = 0; i < sumExp.getOperands().length; i++) {
            if (sumExp.getOperands()[i].isNumeric()) {
                NumericExpression numOp = (NumericExpression) sumExp.getOperands()[i];
                newNumOprand = newNumOprand.add(numOp.multiply(sumExp.getCoefficients()[i]));
            } else {
                newOprands.add(sumExp.getOperands()[i]);
                newCoefs.add(sumExp.getCoefficients()[i]);
            }
        }

        // Case 3 - There are no non-numeric operands, just return the sum
        if (newOprands.size() == 0) {
            return newNumOprand;
        }
        if (!newNumOprand.equate(NumberExpression.Zero)) {
            newOprands.add(newNumOprand);
            newCoefs.add(NumberExpression.One);
        }

        Expression simplified = new SumExpression(newOprands.toArray(new Expression[0]), newCoefs.toArray(new NumericExpression[0]));
        return simplified;
    }

    public boolean canApply(Expression original) {
        if (original.getType() == ExpressionType.Sum) {
            SumExpression sumExp = (SumExpression) original;
            int numericCount = 0;
            boolean hasNonOne = false;
            boolean hasZeroOperand = false;
            for (int i = 0; i < sumExp.getOperands().length; i++) {
                if (sumExp.getOperands()[i].isNumeric()) {
                    numericCount++;
                    if (sumExp.getCoefficients()[i] != NumberExpression.One) {
                        hasNonOne = true;
                    }
                    if (((NumericExpression) sumExp.getOperands()[i]) == NumberExpression.Zero) {
                        hasZeroOperand = true;
                    }
                }
            }

            // Three conditions:
            // 1 - More than 1 numeric operands, need consolidation
            // 2 - At least one numeric operands' coefficient is NON-ONE
            // 3 - There are no non-numeric oprands
            return numericCount > 1 || hasNonOne || hasZeroOperand || numericCount == sumExp.getOperands().length;
        }
        return false;
    }
}
