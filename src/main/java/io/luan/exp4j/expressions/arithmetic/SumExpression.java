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

package io.luan.exp4j.expressions.arithmetic;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.base.BaseExpression;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.value.NumberExpression;

public class SumExpression extends BaseExpression {

    private NumericExpression[] coefficients;
    private Expression[] operands;

    public SumExpression(Expression[] operands) {
        if (operands.length == 0) {
            throw new IllegalArgumentException();
        }
        NumericExpression[] coefficients = new NumericExpression[operands.length];
        for (int i = 0; i < coefficients.length; i++) {
            coefficients[i] = NumberExpression.One;
        }
        this.operands = operands;
        this.coefficients = coefficients;
    }

    public SumExpression(Expression[] operands, NumericExpression[] coefficients) {
        if (operands.length == 0 || operands.length != coefficients.length) {
            throw new IllegalArgumentException();
        }
        this.operands = operands;
        this.coefficients = coefficients;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitSum(this);
    }

    public boolean equals(Expression other) {
        if (other instanceof SumExpression) {
            SumExpression otherExp = (SumExpression) other;

            if (operands.length != otherExp.operands.length) {
                return false;
            }

            // BUG: For now assume perfectly ordered. For later use HashCode
            // ordering
            for (int i = 0; i < operands.length; i++) {
                if (!coefficients[i].equals(otherExp.coefficients[i])) {
                    return false;
                }
                if (!operands[i].equals(otherExp.operands[i])) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    public int getSize() {
        return 0;
    }

    public ExpressionType getType() {
        return ExpressionType.Sum;
    }

    public NumericExpression[] getCoefficients() {
        return coefficients;
    }

    public Expression[] getOperands() {
        return operands;
    }

}
