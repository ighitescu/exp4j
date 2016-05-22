package io.luan.exp4j.expressions.function;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.BaseExpression;

import java.util.function.Function;

public class FunctionExpression extends BaseExpression {

    private String name;
    private Expression[] funcParams;
    private Function<NumericExpression[], NumericExpression> func;

    public FunctionExpression(String name, Expression[] parameters,
                              Function<NumericExpression[], NumericExpression> func) {
        if (parameters.length == 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.funcParams = parameters;
        this.func = func;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitFunction(this);
    }

    public boolean equals(Expression other) {
        if (other instanceof FunctionExpression) {
            FunctionExpression otherExp = (FunctionExpression) other;

            if (!name.equals(otherExp.name)) {
                return false;
            }

            if (funcParams.length != otherExp.funcParams.length) {
                return false;
            }

            // BUG: For now assume perfectly ordered. For later use HashCode
            // ordering
            for (int i = 0; i < funcParams.length; i++) {
                if (!funcParams[i].equals(otherExp.funcParams[i])) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    public Function<NumericExpression[], NumericExpression> getFunc() {
        return func;
    }

    public Expression[] getFuncParams() {
        return funcParams;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return 0;
    }

    public ExpressionType getType() {
        return ExpressionType.Function;
    }
}
