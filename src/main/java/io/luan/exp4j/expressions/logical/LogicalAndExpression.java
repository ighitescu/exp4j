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

package io.luan.exp4j.expressions.logical;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BaseExpression;
import io.luan.exp4j.expressions.BooleanExpression;

/**
 * Multiple node AND operators
 * i.e. A && B && C
 * return true if all operands evaluates to true
 * <p>
 * Optimization: If any operand eval to false, return false immediately.
 */
public class LogicalAndExpression extends BaseExpression implements BooleanExpression {

    private BooleanExpression[] operands;

    public LogicalAndExpression(BooleanExpression[] operands) {
        if (operands.length == 0) {
            throw new IllegalArgumentException();
        }
        this.operands = operands;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitLogicalAnd(this);
    }

    @Override
    public boolean equals(Expression other) {
        return false;
    }

    public BooleanExpression[] getOperands() {
        return operands;
    }

    public int getSize() {
        return 0;
    }

    public ExpressionType getType() {
        return ExpressionType.LogicalAnd;
    }

}
