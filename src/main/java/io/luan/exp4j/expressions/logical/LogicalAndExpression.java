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
