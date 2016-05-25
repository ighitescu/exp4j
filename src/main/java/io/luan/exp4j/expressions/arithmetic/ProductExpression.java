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
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.base.BaseExpression;
import io.luan.exp4j.expressions.type.NumberExpression;

public class ProductExpression extends BaseExpression {

    private NumericExpression[] exponents;
    private Expression[] operands;

    /// <summary>
    /// Default - all exponent is one.
    /// </summary>
    public ProductExpression(Expression[] operands) {
        if (operands.length == 0) {
            throw new IllegalArgumentException();
        }
        NumericExpression[] exponents = new NumericExpression[operands.length];
        for (int i = 0; i < exponents.length; i++) {
            exponents[i] = NumberExpression.One;
        }

        this.operands = operands;
        this.exponents = exponents;
    }

    public ProductExpression(Expression[] operands, NumericExpression[] exponents) {
        if (operands.length == 0 || operands.length != exponents.length) {
            throw new IllegalArgumentException();
        }
        this.operands = operands;
        this.exponents = exponents;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitProduct(this);
    }

    public boolean equals(Expression other) {
        if (other instanceof ProductExpression) {
            ProductExpression otherExp = (ProductExpression) other;

            if (operands.length != otherExp.operands.length) {
                return false;
            }

            // BUG: For now assume perfectly ordered. For later use HashCode
            // ordering
            for (int i = 0; i < operands.length; i++) {
                if (!exponents[i].equals(otherExp.exponents[i])) {
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
        return ExpressionType.Product;
    }

    public NumericExpression[] getExponents() {
        return exponents;
    }

    public Expression[] getOperands() {
        return operands;
    }

}
