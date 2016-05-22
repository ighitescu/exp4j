package io.luan.exp4j.expressions.arithmetic;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BaseExpression;

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
