/*
 * Copyright 2016 Guangmiao Luan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.luan.exp4j.operations.evaluation;

import io.luan.exp4j.Expression;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.PowerExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.comparison.ComparisonExpression;
import io.luan.exp4j.expressions.conditional.ConditionalExpression;
import io.luan.exp4j.expressions.function.FunctionExpression;
import io.luan.exp4j.util.KnownFunctions;
import io.luan.exp4j.expressions.logical.LogicalAndExpression;
import io.luan.exp4j.expressions.logical.LogicalNotExpression;
import io.luan.exp4j.expressions.symbolic.MemberExpression;
import io.luan.exp4j.expressions.symbolic.MethodExpression;
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import io.luan.exp4j.expressions.util.ExpressionUtil;
import io.luan.exp4j.expressions.value.BooleanValueExpression;
import io.luan.exp4j.expressions.value.NumberExpression;
import io.luan.exp4j.expressions.value.ObjectExpression;
import io.luan.exp4j.expressions.value.ValueExpression;
import io.luan.exp4j.operations.base.BaseExpressionVisitor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EvaluationVisitor extends BaseExpressionVisitor {

    private Map<String, Object> values;
    private Map<String, Function<Number[],Number>> funcs;

    public EvaluationVisitor() {
        this(null);
    }

    public EvaluationVisitor(Map<String, Object> values) {
        this(values, null);
    }

    public EvaluationVisitor(Map<String, Object> values, Map<String, Function<Number[],Number>> funcs) {
        if (values == null) {
            values = new HashMap<>();
        }
        this.values = values;

        if (funcs == null) {
            funcs = new HashMap<>();
        }
        this.funcs = funcs;
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

    public Expression visitFunction(FunctionExpression expression) {

        Function<Number[], Number> func = funcs.get(expression.getName());
        if (func == null) {
            func = KnownFunctions.getFunc(expression.getName());
        }
        if (func == null) {
            return super.visitFunction(expression);
        }

        Number[] params = new Number[expression.getFuncParams().length];

        for (int i = 0; i < expression.getFuncParams().length; i++) {
            Expression param = expression.getFuncParams()[i].accept(this);
            if (param instanceof NumberExpression) {
                params[i] =( (NumberExpression) param).getNumber();
            } else {
                // sub expression cannot be evaluated, return self.
                return super.visitFunction(expression);
            }
        }

        Number result = func.apply(params);
        return new NumberExpression(result);
    }

    @Override
    public Expression visitMember(MemberExpression expression) {
        Expression owner = expression.getOwner().accept(this);
        ObjectExpression ownerExp = (ObjectExpression) owner;
        Object ownerObj = ownerExp.getObject();
        String name = expression.getName();
        try {
            Field field = ownerObj.getClass().getField(name);
            Object result = field.get(ownerObj);
            return ExpressionUtil.objToExpression(result);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return super.visitMember(expression);
    }

    @Override
    public Expression visitMethod(MethodExpression expression) {
        Expression owner = expression.getOwner().accept(this);
        ObjectExpression ownerExp = (ObjectExpression) owner;
        Object ownerObj = ownerExp.getObject();
        String name = expression.getName();
        try {
            Class[] classes = new Class[expression.getParameters().length];
            Object[] paramObjs = new Object[expression.getParameters().length];
            for (int i = 0; i < expression.getParameters().length; i++) {
                Expression paramExp = expression.getParameters()[i].accept(this);
                if (!(paramExp instanceof ValueExpression)) {
                    return super.visitMethod(expression);
                }

                paramObjs[i] = ((ValueExpression) paramExp).getObject();
                classes[i] = paramObjs[i].getClass();
            }

            for (Method m : ownerObj.getClass().getMethods()) {
                System.out.println(m);
            }

            Method method = ownerObj.getClass().getMethod(name, classes);
            Object result = method.invoke(ownerObj, paramObjs);
            return ExpressionUtil.objToExpression(result);

        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return super.visitMethod(expression);
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
            throw new IllegalArgumentException("Input cannot be null");
        }

        return ExpressionUtil.objToExpression(varValue);
    }
}
