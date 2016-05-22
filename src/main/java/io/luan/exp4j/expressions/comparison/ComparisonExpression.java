package io.luan.exp4j.expressions.comparison;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BaseExpression;
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
