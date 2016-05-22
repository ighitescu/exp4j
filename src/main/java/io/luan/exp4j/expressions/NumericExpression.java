package io.luan.exp4j.expressions;

import io.luan.exp4j.Expression;

/**
 * Base interface of the Four type type expressions:
 * Number Expression (Can be Long or BigDecimal)
 * Fraction Expression TODO: A Fraction have both top and bottom as integers
 * Complex Expression TODO: Complex Expression
 * All math should be done in a manner than preserves precision as much as possible.
 */
public interface NumericExpression extends Expression {

    NumericExpression add(NumericExpression other);

    int compareTo(NumericExpression other);

    NumericExpression divide(NumericExpression other);

    boolean equate(NumericExpression other);

    NumericExpression multiply(NumericExpression other);

    NumericExpression negate();

    NumericExpression power(NumericExpression other);

    NumericExpression subtract(NumericExpression other);
}
