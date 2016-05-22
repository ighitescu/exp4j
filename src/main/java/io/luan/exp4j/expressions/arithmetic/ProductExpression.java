package io.luan.exp4j.expressions.arithmetic;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BaseExpression;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.type.NumberExpression;

public class ProductExpression extends BaseExpression {

    private NumericExpression[] exponents;
    private Expression[] operands;

    /// <summary>
    /// Default - all exponent is one.
    /// </summary>
    public ProductExpression(Expression[] operands) {
        if (operands.length == 0) {
            throw new IllegalArgumentException();
        }
        NumericExpression[] exponents = new NumericExpression[operands.length];
        for (int i = 0; i < exponents.length; i++) {
            exponents[i] = NumberExpression.One;
        }

        this.operands = operands;
        this.exponents = exponents;
    }

    public ProductExpression(Expression[] operands, NumericExpression[] exponents) {
        if (operands.length == 0 || operands.length != exponents.length) {
            throw new IllegalArgumentException();
        }
        this.operands = operands;
        this.exponents = exponents;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitProduct(this);
    }

    public boolean equals(Expression other) {
        if (other instanceof ProductExpression) {
            ProductExpression otherExp = (ProductExpression) other;

            if (operands.length != otherExp.operands.length) {
                return false;
            }

            // BUG: For now assume perfectly ordered. For later use HashCode
            // ordering
            for (int i = 0; i < operands.length; i++) {
                if (!exponents[i].equals(otherExp.exponents[i])) {
                    return false;
                }
                if (!operands[i].equals(otherExp.operands[i])) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    public int getSize() {
        return 0;
    }

    public ExpressionType getType() {
        return ExpressionType.Product;
    }

    public NumericExpression[] getExponents() {
        return exponents;
    }

    public Expression[] getOperands() {
        return operands;
    }

}
