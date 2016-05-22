package io.luan.exp4j.visitors.simplification;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.type.NumberExpression;

import java.util.ArrayList;

/// <summary>
/// Zero Coefficient: 	A term with zero coefficient is removed
/// </summary>
public class ZeroCoefficientSimplificationRule implements SimplificationRule {

    public Expression apply(Expression original) {
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newCoefs = new ArrayList<NumericExpression>();
        SumExpression sumExp = (SumExpression) original;

        for (int i = 0; i < sumExp.getOperands().length; i++) {
            if (!sumExp.getCoefficients()[i].equate(NumberExpression.Zero)) {
                newOprands.add(sumExp.getOperands()[i]);
                newCoefs.add(sumExp.getCoefficients()[i]);
            }
        }

        if (newOprands.size() == 0) {
            return NumberExpression.Zero; // somehow nothing left, return ZERO
        }
        Expression simplified = new SumExpression(newOprands.toArray(new Expression[0]), newCoefs.toArray(new NumericExpression[0]));
        return simplified;
    }

    public boolean canApply(Expression original) {
        if (original.getType() == ExpressionType.Sum) {
            SumExpression sumExp = (SumExpression) original;
            for (NumericExpression exp : sumExp.getCoefficients()) {
                if (exp.equate(NumberExpression.Zero)) {
                    return true;
                }
            }
        }
        return false;
    }
}
