package io.luan.exp4j.expressions.type;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BaseExpression;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.util.NumberUtil;

/**
 * Contains a Number
 * <p>
 * - Valid for Integer, Long, BigDecimal, BigInteger
 */
public class NumberExpression extends BaseExpression implements NumericExpression {

    public static final NumberExpression One = new NumberExpression(1);
    public static final NumberExpression MinusOne = new NumberExpression(-1);
    public static final NumberExpression Zero = new NumberExpression(0);
    public static final NumberExpression Ten = new NumberExpression(10);

    private final Number number;

    public NumberExpression(Number number) {
        if (number == null)
            throw new IllegalArgumentException("number is null!");
        this.number = number;
    }

    @Override
    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitNumber(this);
    }

    @Override
    public NumericExpression add(NumericExpression other) {
        if (other instanceof NumberExpression) {
            Number result = NumberUtil.add(number, ((NumberExpression) other).number);
            return new NumberExpression(result);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(NumericExpression other) {
        if (other instanceof NumberExpression) {
            return NumberUtil.compare(number, ((NumberExpression) other).number);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public NumericExpression divide(NumericExpression other) {
        if (other instanceof NumberExpression) {
            Number result = NumberUtil.divide(number, ((NumberExpression) other).number);
            return new NumberExpression(result);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equate(NumericExpression other) {
        if (other instanceof NumberExpression) {
            return NumberUtil.equate(number, ((NumberExpression) other).number);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public NumericExpression multiply(NumericExpression other) {
        if (other instanceof NumberExpression) {
            Number result = NumberUtil.multiply(number, ((NumberExpression) other).number);
            return new NumberExpression(result);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public NumericExpression negate() {
        Number result = NumberUtil.negate(number);
        return new NumberExpression(result);
    }

    @Override
    public NumericExpression power(NumericExpression other) {
        if (other instanceof NumberExpression) {
            Number result = NumberUtil.power(number, ((NumberExpression) other).number);
            return new NumberExpression(result);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public NumericExpression subtract(NumericExpression other) {
        if (other instanceof NumberExpression) {
            Number result = NumberUtil.subtract(number, ((NumberExpression) other).number);
            return new NumberExpression(result);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Expression other) {
        if (other != null && other instanceof NumberExpression) {
            return NumberUtil.equate(number, ((NumberExpression) other).number);
        }
        return false;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.Number;
    }

    public Number getNumber() {
        return number;
    }
}
