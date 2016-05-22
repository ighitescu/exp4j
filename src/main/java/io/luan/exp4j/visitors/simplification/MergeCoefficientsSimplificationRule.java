package io.luan.exp4j.visitors.simplification;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;

import java.util.ArrayList;

/// <summary>
/// This merges the coefficients of two or more oprands that are the same.
/// Applies to SumNode
/// </summary>
public class MergeCoefficientsSimplificationRule implements SimplificationRule {

    /// <summary>
    /// Applies ONLY if there can be two operands of the same value
    /// Algorithm:
    ///   For each oprand, check if it already exists in the new array, if not, add, otherwise, update the coefs
    /// </summary>
    public Expression apply(Expression original) {
        boolean success = false;
        SumExpression sumExp = (SumExpression) original;
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newCoefs = new ArrayList<NumericExpression>();

        for (int i = 0; i < sumExp.getOperands().length; i++) {
            boolean exists = false;
            for (int j = 0; j < newOprands.size(); j++) {
                if (newOprands.get(j).equals(sumExp.getOperands()[i])) {
                    // Already exists, just update the coef
                    newCoefs.set(j, newCoefs.get(j).add(sumExp.getCoefficients()[i]));
                    exists = true;
                    success = true;
                }
            }

            if (!exists) {
                newOprands.add(sumExp.getOperands()[i]);
                newCoefs.add(sumExp.getCoefficients()[i]);
            }
        }

        if (success) {
            SumExpression simplified = new SumExpression(newOprands.toArray(new Expression[0]), newCoefs.toArray(new NumericExpression[0]));
            return simplified;
        }
        return original;
    }

    public boolean canApply(Expression original) {
        // Can be applied if there is more than one oprand.
        if (original.getType() == ExpressionType.Sum) {
            SumExpression sumExp = (SumExpression) original;
            return sumExp.getOperands().length > 1;
        }
        return false;
    }
}
