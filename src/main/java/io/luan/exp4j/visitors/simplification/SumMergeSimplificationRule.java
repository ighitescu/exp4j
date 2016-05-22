package io.luan.exp4j.visitors.simplification;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;

import java.util.ArrayList;

public class SumMergeSimplificationRule implements SimplificationRule {

    public Expression apply(Expression original) {
        SumExpression sumExp = (SumExpression) original;
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newCoefs = new ArrayList<NumericExpression>();

        for (int i = 0; i < sumExp.getOperands().length; i++) {
            Expression op = sumExp.getOperands()[i];
            if (op instanceof SumExpression) {
                SumExpression subExp = (SumExpression) op;

                for (int j = 0; j < subExp.getOperands().length; j++) {
                    newOprands.add(subExp.getOperands()[j]);
                    newCoefs.add(subExp.getCoefficients()[j].multiply(sumExp.getCoefficients()[i]));
                }

            } else {
                newOprands.add(sumExp.getOperands()[i]);
                newCoefs.add(sumExp.getCoefficients()[i]);
            }
        }

        SumExpression simplified = new SumExpression(newOprands.toArray(new Expression[0]), newCoefs.toArray(new NumericExpression[0]));
        return simplified;
    }

    public boolean canApply(Expression original) {
        // A sum merge rule can be applied to a sum expression,
        // AND, at least one operand is a sum expression
        if (original.getType() == ExpressionType.Sum) {
            SumExpression sumExp = (SumExpression) original;
            for (Expression oprand : sumExp.getOperands()) {
                if (oprand.getType() == ExpressionType.Sum) {
                    return true;
                }
            }
        }
        return false;
    }
}
