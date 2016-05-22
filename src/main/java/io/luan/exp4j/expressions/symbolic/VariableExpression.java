package io.luan.exp4j.expressions.symbolic;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BaseExpression;

public class VariableExpression extends BaseExpression {

    private String name;

    public VariableExpression(String name) {
        this.name = name;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitVariable(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Expression)) {
            return false;
        }
        Expression other = (Expression) obj;
        return equals(other);
    }

    /// <summary>
    /// Two variable expressions are equal if they are both variable expressions
    /// AND the variable names are the same.
    /// </summary>
    public boolean equals(Expression other) {
        if (other instanceof VariableExpression) {
            VariableExpression otherExp = (VariableExpression) other;
            return name.equals(otherExp.name);
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return 0;
    }

    public ExpressionType getType() {
        return ExpressionType.Variable;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
