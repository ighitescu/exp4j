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

public class PowerExpression extends BaseExpression {

    private Expression base;
    // must be non-numeric
    private Expression exponent;

    public PowerExpression(Expression baseExp, Expression exponentExp) {
        base = baseExp;
        exponent = exponentExp;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitPower(this);
    }

    public boolean equals(Expression other) {
        if (other instanceof PowerExpression) {
            PowerExpression otherExp = (PowerExpression) other;

            if (!base.equals(otherExp.base)) {
                return false;
            }

            if (!exponent.equals(otherExp.exponent)) {
                return false;
            }

            return true;
        }

        return false;

    }

    public Expression getBase() {
        return base;
    }

    public Expression getExponent() {
        return base;
    }

    public int getSize() {
        return base.getSize() + exponent.getSize();
    }

    public ExpressionType getType() {
        return ExpressionType.Power;
    }
}
