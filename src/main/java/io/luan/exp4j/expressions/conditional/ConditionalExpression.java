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

package io.luan.exp4j.expressions.conditional;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BooleanExpression;
import io.luan.exp4j.expressions.BaseExpression;

/**
 * Expression for If-else relationship
 * (condition ? exp-if-true : exp-if-false)
 * <p>
 * e.g.:
 * <p>
 * exp(x) -> x > 3 ? 3 * x : 2 * x;
 */
public class ConditionalExpression extends BaseExpression {

    private BooleanExpression condition;
    private Expression trueExp;
    private Expression falseExp;

    public ConditionalExpression(BooleanExpression condition, Expression trueExp, Expression falseExp) {
        this.condition = condition;
        this.trueExp = trueExp;
        this.falseExp = falseExp;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitConditional(this);
    }

    public boolean equals(Expression other) {
        if (other instanceof ConditionalExpression) {
            ConditionalExpression otherExp = (ConditionalExpression) other;
            return false;
        }
        return false;
    }

    public BooleanExpression getCondition() {
        return condition;
    }

    public Expression getFalseExpression() {
        return falseExp;
    }

    public int getSize() {
        return condition.getSize() + trueExp.getSize() + falseExp.getSize();
    }

    public Expression getTrueExpression() {
        return trueExp;
    }

    public ExpressionType getType() {
        return ExpressionType.Conditional;
    }
}
