package io.luan.exp4j.expressions.symbolic;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BaseExpression;

public class ConstantExpression extends BaseExpression {

    private String name;

    public ConstantExpression(String name) {
        this.name = name;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitConstant(this);
    }

    /// <summary>
    /// Two variable expressions are equal if they are both variable expressions
    /// AND the variable names are the same.
    /// </summary>
    public boolean equals(Expression other) {
        if (other instanceof ConstantExpression) {
            ConstantExpression otherExp = (ConstantExpression) other;
            return name.equals(otherExp.name);
        }
        return false;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.Constant;
    }
}
