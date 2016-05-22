package io.luan.exp4j.visitors.simplification;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;

import java.util.ArrayList;

/// <summary>
/// This merges the exponents of two or more oprands that are the same.
/// Applies to Product Node
/// </summary>
public class MergeExponentsSimplificationRule implements SimplificationRule {

    /// <summary>
    /// Applies ONLY if there can be two operands of the same value
    /// Algorithm:
    ///   For each oprand, check if it already exists in the new array, if not, add, otherwise, update the coefs
    /// </summary>
    public Expression apply(Expression original) {
        boolean success = false;
        ProductExpression prodExp = (ProductExpression) original;
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newExponents = new ArrayList<NumericExpression>();

        for (int i = 0; i < prodExp.getOperands().length; i++) {
            boolean exists = false;
            for (int j = 0; j < newOprands.size(); j++) {
                if (newOprands.get(j).equals(prodExp.getOperands()[i])) {
                    // Already exists, just update the coef
                    newExponents.set(j, newExponents.get(j).add(prodExp.getExponents()[i]));
                    exists = true;
                    success = true;
                }
            }

            if (!exists) {
                newOprands.add(prodExp.getOperands()[i]);
                newExponents.add(prodExp.getExponents()[i]);
            }
        }

        if (success) {
            ProductExpression simplified = new ProductExpression(newOprands.toArray(new Expression[0]),
                    newExponents.toArray(new NumericExpression[0]));
            return simplified;
        }
        return original;
    }

    public boolean canApply(Expression original) {
        // Can be applied if there is more than one oprand.
        if (original.getType() == ExpressionType.Product) {
            ProductExpression prodExp = (ProductExpression) original;
            return prodExp.getOperands().length > 1;
        }
        return false;
    }
}
