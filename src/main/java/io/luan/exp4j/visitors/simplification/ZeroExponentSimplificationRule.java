package io.luan.exp4j.visitors.simplification;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.type.NumberExpression;

import java.util.ArrayList;

/// <summary>
/// remove Zero Exponent term in a product node
/// </summary>
public class ZeroExponentSimplificationRule implements SimplificationRule {

    public Expression apply(Expression original) {
        ProductExpression prodExp = (ProductExpression) original;
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newExponents = new ArrayList<NumericExpression>();

        for (int i = 0; i < prodExp.getOperands().length; i++) {
            if (!prodExp.getExponents()[i].equate(NumberExpression.Zero)) {
                newOprands.add(prodExp.getOperands()[i]);
                newExponents.add(prodExp.getExponents()[i]);
            }
        }
        if (newOprands.size() == 0) {
            return NumberExpression.One; // somehow no terms left, return ONE
        }
        ProductExpression simplified = new ProductExpression(newOprands.toArray(new Expression[0]),
                newExponents.toArray(new NumericExpression[0]));
        return simplified;
    }

    public boolean canApply(Expression original) {
        if (original.getType() == ExpressionType.Product) {
            ProductExpression prodExp = (ProductExpression) original;
            for (NumericExpression exp : prodExp.getExponents()) {
                if (exp.equate(NumberExpression.Zero)) {
                    return true;
                }
            }
        }
        return false;
    }
}
