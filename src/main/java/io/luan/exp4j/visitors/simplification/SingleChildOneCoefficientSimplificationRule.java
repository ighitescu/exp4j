package io.luan.exp4j.visitors.simplification;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.type.NumberExpression;

/// <summary>
/// Applies to SUM node, single child with coefficient == 1
/// </summary>
public class SingleChildOneCoefficientSimplificationRule implements SimplificationRule {
    public Expression apply(Expression original) {
        Expression simplified = ((SumExpression) original).getOperands()[0];
        return simplified;
    }

    public boolean canApply(Expression original) {
        if (original.getType() == ExpressionType.Sum) {
            SumExpression SumExp = (SumExpression) original;
            return SumExp.getOperands().length == 1 && SumExp.getCoefficients()[0].equate(NumberExpression.One);
        }
        return false;
    }
}
