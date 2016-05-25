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

package io.luan.exp4j.expressions.comparison;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.base.BaseExpression;
import io.luan.exp4j.expressions.BooleanExpression;

/**
 * A == B
 * A != B
 * A > B
 * A >= B
 * A < B
 * A <= B
 */
public class ComparisonExpression extends BaseExpression implements BooleanExpression {

    private Expression leftOperand;
    private Expression rightOperand;
    private ComparisonOperator operator;

    public ComparisonExpression(Expression leftOperand, Expression rightOperand, ComparisonOperator operator) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitComparison(this);
    }

    @Override
    public boolean equals(Expression other) {
        return false;
    }

    public Expression getLeftOperand() {
        return leftOperand;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public Expression getRightOperand() {
        return rightOperand;
    }

    public int getSize() {
        return 0;
    }

    public ExpressionType getType() {
        return ExpressionType.Comparison;
    }

    public enum ComparisonOperator {
        Equal,
        NotEqual,
        GreaterThan,
        GreaterThanOrEqual,
        LessThan,
        LessThanOrEqual
    }
}
