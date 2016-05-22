package io.luan.exp4j.expressions.type;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BaseExpression;
import io.luan.exp4j.expressions.BooleanExpression;

public class BooleanValueExpression extends BaseExpression implements BooleanExpression {

    public static final BooleanValueExpression True = new BooleanValueExpression(true);
    public static final BooleanValueExpression False = new BooleanValueExpression(false);

    private boolean boolValue;

    public BooleanValueExpression(boolean value) {
        boolValue = value;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitBooleanValue(this);
    }

    @Override
    public boolean equals(Expression other) {
        return false;
    }

    public boolean getBooleanValue() {
        return boolValue;
    }

    public int getSize() {
        return 1;
    }

    public ExpressionType getType() {
        return ExpressionType.BooleanValue;
    }

    @Override
    public int hashCode() {
        return boolValue ? 1 : 0;
    }

}
