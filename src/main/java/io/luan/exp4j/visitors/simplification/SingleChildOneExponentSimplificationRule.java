package io.luan.exp4j.visitors.simplification;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.type.NumberExpression;

/// <summary>
/// Applies to PROD node, single child with exponent == 1
/// </summary>
public class SingleChildOneExponentSimplificationRule implements SimplificationRule {

    public Expression apply(Expression original) {
        Expression simplified = ((ProductExpression) original).getOperands()[0];
        return simplified;
    }

    public boolean canApply(Expression original) {
        if (original.getType() == ExpressionType.Product) {
            ProductExpression prodExp = (ProductExpression) original;
            return prodExp.getOperands().length == 1 && prodExp.getExponents()[0].equate(NumberExpression.One);
        }
        return false;
    }
}
