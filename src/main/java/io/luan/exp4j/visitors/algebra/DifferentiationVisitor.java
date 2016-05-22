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

package io.luan.exp4j.visitors.algebra;

import io.luan.exp4j.Expression;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.function.FunctionExpression;
import io.luan.exp4j.expressions.symbolic.ConstantExpression;
import io.luan.exp4j.expressions.symbolic.ParameterExpression;
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import io.luan.exp4j.expressions.type.NumberExpression;
import io.luan.exp4j.visitors.BaseExpressionVisitor;

import java.util.ArrayList;

public class DifferentiationVisitor extends BaseExpressionVisitor {
    // private readonly SineFunctionDifferentiationRule sinRule = new
    // SineFunctionDifferentiationRule();
    // private readonly CosineFunctionDifferentiationRule cosRule = new
    // CosineFunctionDifferentiationRule();

    private String variable;

    public DifferentiationVisitor(String variable) {
        this.variable = variable;
    }

    public String getVariable() {
        return variable;
    }

    public Expression visitConstant(ConstantExpression expression) {
        return NumberExpression.Zero;
    }

    public Expression visitNumber(NumberExpression expression) {
        return NumberExpression.Zero;
    }

    public Expression visitFunction(FunctionExpression expression) {
        // if (this.sinRule.CanApply(expression, Variable)) {
        // return this.sinRule.Apply(expression, Variable);
        // }
        // if (this.cosRule.CanApply(expression, Variable)) {
        // return this.cosRule.Apply(expression, Variable);
        // }
        throw new RuntimeException();
    }

    public Expression visitParameter(ParameterExpression expression) {
        return NumberExpression.Zero;
    }

    public Expression visitProduct(ProductExpression expression) {
        if (expression.getOperands().length == 1) {
            Expression onlyChild = expression.getOperands()[0];
            NumericExpression exponent = expression.getExponents()[0];

            Expression diffExp = onlyChild.accept(this);
            Expression[] newOperands = new Expression[]{onlyChild, diffExp};
            NumericExpression[] newExponents = new NumericExpression[]{exponent.subtract(NumberExpression.One),
                    NumberExpression.One};

            ProductExpression newProdExp = new ProductExpression(newOperands, newExponents);

            SumExpression sumExp = new SumExpression(new Expression[]{newProdExp},
                    new NumericExpression[]{exponent});
            return sumExp;
        } else {
            ArrayList<Expression> sumOperands = new ArrayList<Expression>();
            ArrayList<NumericExpression> sumCoefs = new ArrayList<NumericExpression>();

            for (int i = 0; i < expression.getOperands().length; i++) {
                ArrayList<Expression> prodOperands = new ArrayList<Expression>();
                ArrayList<NumericExpression> prodExponents = new ArrayList<NumericExpression>();
                for (int j = 0; j < expression.getOperands().length; j++) {
                    if (i == j) {
                        // Need to build a temporary product expression (aka.
                        // f(x)^n) IF Exponents[i] != 1
                        if (!expression.getExponents()[i].equate(NumberExpression.One)) {
                            ProductExpression tempExp = new ProductExpression(
                                    new Expression[]{expression.getOperands()[i]},
                                    new NumericExpression[]{expression.getExponents()[i]});
                            prodOperands.add(tempExp.accept(this));
                        } else {
                            prodOperands.add(expression.getOperands()[i].accept(this));
                        }
                    } else {
                        prodOperands.add(expression.getOperands()[j]);
                    }
                    prodExponents.add(NumberExpression.One);
                }
                ProductExpression newProdExp = new ProductExpression(prodOperands.toArray(new Expression[0]),
                        prodExponents.toArray(new NumericExpression[0]));
                sumOperands.add(newProdExp);
                sumCoefs.add(NumberExpression.One);
            }

            SumExpression sumExp = new SumExpression(sumOperands.toArray(new Expression[0]),
                    sumCoefs.toArray(new NumericExpression[0]));
            return sumExp;
        }
    }

    public Expression visitSum(SumExpression expression) {
        ArrayList<Expression> operands = new ArrayList<Expression>();
        ArrayList<NumericExpression> coefficients = new ArrayList<NumericExpression>();

        for (int i = 0; i < expression.getOperands().length; i++) {
            Expression subDiff = expression.getOperands()[i].accept(this);
            operands.add(subDiff);
            coefficients.add(expression.getCoefficients()[i]);
        }

        return new SumExpression(operands.toArray(new Expression[0]), coefficients.toArray(new NumericExpression[0]));
    }

    public Expression visitVariable(VariableExpression expression) {
        return expression.getName().equals(variable) ? NumberExpression.One : NumberExpression.Zero;
    }
}
