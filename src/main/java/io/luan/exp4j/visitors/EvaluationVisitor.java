package io.luan.exp4j.visitors;

import io.luan.exp4j.Expression;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.PowerExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.comparison.ComparisonExpression;
import io.luan.exp4j.expressions.conditional.ConditionalExpression;
import io.luan.exp4j.expressions.function.FunctionExpression;
import io.luan.exp4j.expressions.logical.LogicalAndExpression;
import io.luan.exp4j.expressions.logical.LogicalNotExpression;
import io.luan.exp4j.expressions.symbolic.ConstantExpression;
import io.luan.exp4j.expressions.symbolic.ParameterExpression;
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import io.luan.exp4j.expressions.type.BooleanValueExpression;
import io.luan.exp4j.expressions.type.NumberExpression;
import io.luan.exp4j.util.NumberUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class EvaluationVisitor extends BaseExpressionVisitor {

    private Map<String, Object> values;

    public EvaluationVisitor() {
        this(null);
    }

    public EvaluationVisitor(Map<String, Object> values) {
        if (values == null) {
            values = new HashMap<String, Object>();
        }
        this.values = values;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    @Override
    public Expression visitComparison(ComparisonExpression expression) {
        Expression left = expression.getLeftOperand().accept(this);
        Expression right = expression.getRightOperand().accept(this);
        if ((left instanceof NumericExpression) && (right instanceof NumericExpression)) {
            NumericExpression numLeft = (NumericExpression) left;
            NumericExpression numRight = (NumericExpression) right;

            boolean result;

            switch (expression.getOperator()) {
                case GreaterThan:
                    result = numLeft.compareTo(numRight) > 0;
                    break;
                case GreaterThanOrEqual:
                    result = numLeft.compareTo(numRight) >= 0;
                    break;
                case LessThan:
                    result = numLeft.compareTo(numRight) < 0;
                    break;
                case LessThanOrEqual:
                    result = numLeft.compareTo(numRight) <= 0;
                    break;
                case Equal:
                    result = numLeft.equate(numRight);
                    break;
                case NotEqual:
                    result = !numLeft.equate(numRight);
                    break;
                default:
                    throw new UnsupportedOperationException();
            }

            return result ? BooleanValueExpression.True : BooleanValueExpression.False;
        }
        return super.visitComparison(expression);

    }

    @Override
    public Expression visitLogicalAnd(LogicalAndExpression expression) {
        for (int i = 0; i < expression.getOperands().length; i++) {
            Expression exp = expression.getOperands()[i].accept(this);
            if (exp instanceof BooleanValueExpression) {
                BooleanValueExpression boolExp = (BooleanValueExpression) exp;
                if (!boolExp.getBooleanValue()) {
                    return BooleanValueExpression.False;
                }
            } else {
                // Sub Expression cannot be evaluated. Return self
                return super.visitLogicalAnd(expression);
            }
        }
        return BooleanValueExpression.True;
    }

    @Override
    public Expression visitLogicalNot(LogicalNotExpression expression) {
        Expression operand = expression.getOperand().accept(this);
        if (operand instanceof BooleanValueExpression) {
            if (((BooleanValueExpression) operand).getBooleanValue()) {
                return BooleanValueExpression.False;
            } else {
                return BooleanValueExpression.True;
            }
        }
        return super.visitLogicalNot(expression);
    }

    @Override
    public Expression visitConditional(ConditionalExpression expression) {

        Expression cond = expression.getCondition().accept(this);
        if (cond instanceof BooleanValueExpression) {
            BooleanValueExpression condBool = (BooleanValueExpression) cond;
            if (condBool.getBooleanValue()) {
                return expression.getTrueExpression().accept(this);
            } else {
                return expression.getFalseExpression().accept(this);
            }
        }

        return super.visitConditional(expression);
    }

    @Override
    public Expression visitConstant(ConstantExpression expression) {
        Object varValue = values.get(expression.getName());
        if (varValue == null) {
            return NumberExpression.One;
        }

        if (varValue instanceof Integer) {
            return new NumberExpression((Integer) varValue);
        }
        if (varValue instanceof BigDecimal) {
            return new NumberExpression((BigDecimal) varValue);
        }

        if (varValue instanceof String) {
            try {
                int intVal = Integer.parseInt((String) varValue);
                return new NumberExpression(intVal);
            } catch (NumberFormatException e) {
                // do nothing
            }

            try {
                BigDecimal decVal = new BigDecimal((String) varValue);
                return new NumberExpression(decVal);
            } catch (NumberFormatException e) {
                // do nothing
            }

            throw new NumberFormatException();
        }

        return super.visitConstant(expression);
    }

    public Expression visitFunction(FunctionExpression expression) {
        NumericExpression[] paramExps = new NumericExpression[expression.getFuncParams().length];

        for (int i = 0; i < expression.getFuncParams().length; i++) {
            Expression param = expression.getFuncParams()[i].accept(this);
            if (param instanceof NumericExpression) {
                paramExps[i] = (NumericExpression) param;
            } else {
                // sub expression cannot be evaluated, return self.
                return super.visitFunction(expression);
            }
        }

        Expression result = expression.getFunc().apply(paramExps);
        return result;
    }

    @Override
    public Expression visitParameter(ParameterExpression expression) {
        Object varValue = values.get(expression.getName());
        if (varValue == null) {
            return NumberExpression.One;
        }

        if (varValue instanceof Integer) {
            return new NumberExpression((Integer) varValue);
        }
        if (varValue instanceof BigDecimal) {
            return new NumberExpression((BigDecimal) varValue);
        }

        if (varValue instanceof String) {
            try {
                int intVal = Integer.parseInt((String) varValue);
                return new NumberExpression(intVal);
            } catch (NumberFormatException e) {
                // do nothing
            }

            try {
                BigDecimal decVal = new BigDecimal((String) varValue);
                return new NumberExpression(decVal);
            } catch (NumberFormatException e) {
                // do nothing
            }

            throw new NumberFormatException();
        }

        return super.visitParameter(expression);
    }

    @Override
    public Expression visitPower(PowerExpression expression) {
        Expression base = expression.getBase().accept(this);
        if (base instanceof NumericExpression) {
            NumericExpression baseNum = (NumericExpression) base;

            Expression exp = expression.getExponent().accept(this);
            if (exp instanceof NumericExpression) {
                NumericExpression expNum = (NumericExpression) exp;
                return baseNum.power(expNum);
            }
        }
        return super.visitPower(expression);
    }

    /// <summary>
    /// Try to evaluate the expression based on inputs.
    /// If not all sub expressions can be resolved as numeric, then return
    /// unevaluated self.
    /// </summary>
    @Override
    public Expression visitProduct(ProductExpression expression) {
        // Multiplication always starts with 1
        NumericExpression result = NumberExpression.One;

        for (int i = 0; i < expression.getOperands().length; i++) {
            Expression exp = expression.getOperands()[i].accept(this);
            if (exp instanceof NumericExpression) {
                NumericExpression numExp = (NumericExpression) exp;

                if (expression.getExponents()[i].compareTo(NumberExpression.Zero) > 0) {
                    NumericExpression pow = numExp.power(expression.getExponents()[i]);
                    result = result.multiply(pow);
                } else if (expression.getExponents()[i].compareTo(NumberExpression.Zero) < 0) {
                    NumericExpression neg = expression.getExponents()[i].negate();
                    NumericExpression pow = numExp.power(neg);
                    result = result.divide(pow);
                } else {
                    // This is case when exponent == 0;
                    // No need to change the result since multiply by 1.
                }
            } else {
                // Sub Expression cannot be evaluated. Return self
                return super.visitProduct(expression);
            }
        }
        return result;
    }

    /// <summary>
    /// Try to evaluate the expression based on inputs.
    /// If not all sub expressions can be resolved as numeric, then return
    /// unevaluated self.
    /// </summary>
    @Override
    public Expression visitSum(SumExpression expression) {
        // Summation always starts with zero
        NumericExpression result = NumberExpression.Zero;

        for (int i = 0; i < expression.getOperands().length; i++) {
            Expression exp = expression.getOperands()[i].accept(this);
            if (exp instanceof NumericExpression) {
                NumericExpression numExp = (NumericExpression) exp;
                NumericExpression multi = numExp.multiply(expression.getCoefficients()[i]);
                result = result.add(multi);
            } else {
                // Sub Expression cannot be evaluated. Return self
                return super.visitSum(expression);
            }
        }
        return result;
    }

    @Override
    public Expression visitVariable(VariableExpression expression) {
        Object varValue = values.get(expression.getName());
        if (varValue == null) {
            return NumberExpression.One;
        }

        if (varValue instanceof Integer) {
            return new NumberExpression((Integer) varValue);
        }
        if (varValue instanceof Long) {
            return new NumberExpression((Long) varValue);
        }
        if (varValue instanceof BigInteger) {
            return new NumberExpression((BigInteger) varValue);
        }
        if (varValue instanceof BigDecimal) {
            return new NumberExpression((BigDecimal) varValue);
        }
        if (varValue instanceof Double) {
            return new NumberExpression(BigDecimal.valueOf((Double) varValue));
        }
        if (varValue instanceof String) {
            Number number = NumberUtil.parse((String) varValue);
            return new NumberExpression(number);
        }

        return super.visitVariable(expression);
    }
}
