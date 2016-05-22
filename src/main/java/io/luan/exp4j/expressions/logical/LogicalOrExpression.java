package io.luan.exp4j.expressions.logical;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BaseExpression;
import io.luan.exp4j.expressions.BooleanExpression;

/**
 * Multiple node OR operators
 * i.e. A || B || C
 * return true if and operand evaluates to true
 * <p>
 * Optimization: If any operand eval to true, return true immediately.
 */
public class LogicalOrExpression extends BaseExpression implements BooleanExpression {

    private BooleanExpression[] operands;

    public LogicalOrExpression(BooleanExpression[] operands) {
        if (operands.length == 0) {
            throw new IllegalArgumentException();
        }
        this.operands = operands;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitLogicalOr(this);
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
        return ExpressionType.LogicalOr;
    }
}
