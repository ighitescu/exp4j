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
