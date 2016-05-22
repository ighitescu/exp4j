package io.luan.exp4j.expressions.logical;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BaseExpression;
import io.luan.exp4j.expressions.BooleanExpression;

/**
 * LogicalNot operator
 * i.e. !A
 */
public class LogicalNotExpression extends BaseExpression implements BooleanExpression {

    private BooleanExpression operand;

    public LogicalNotExpression(BooleanExpression operand) {
        this.operand = operand;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitLogicalNot(this);
    }

    @Override
    public boolean equals(Expression other) {
        return false;
    }

    public BooleanExpression getOperand() {
        return operand;
    }

    public int getSize() {
        return 0;
    }

    public ExpressionType getType() {
        return ExpressionType.LogicalNot;
    }
}
