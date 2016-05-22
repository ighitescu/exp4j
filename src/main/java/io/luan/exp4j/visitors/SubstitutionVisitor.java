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

package io.luan.exp4j.visitors;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.symbolic.ConstantExpression;
import io.luan.exp4j.expressions.symbolic.ParameterExpression;
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import io.luan.exp4j.expressions.type.NumberExpression;

import java.util.ArrayList;

public class SubstitutionVisitor extends BaseExpressionVisitor {

    private Expression target;
    private Expression substitute;

    public SubstitutionVisitor(Expression target, Expression substitute) {
        this.target = target;
        this.substitute = substitute;
    }

    public Expression getSubstitute() {
        return substitute;
    }

    public Expression getTarget() {
        return target;
    }

    public Expression visitConstant(ConstantExpression expression) {
        if (target.getType() == ExpressionType.Constant) {
            ConstantExpression varTarget = (ConstantExpression) target;
            if (varTarget.getName().equals(expression.getName())) {
                return substitute;
            }
        }
        return super.visitConstant(expression);
    }

    @Override
    public Expression visitNumber(NumberExpression expression) {
        if (target.getType() == ExpressionType.Number) {
            NumberExpression decTarget = (NumberExpression) target;
            if (decTarget.equate(expression)) {
                return substitute;
            }
        }
        return super.visitNumber(expression);
    }

    public Expression visitParameter(ParameterExpression expression) {
        if (target.getType() == ExpressionType.Parameter) {
            ParameterExpression varTarget = (ParameterExpression) target;
            if (varTarget.getName().equals(expression.getName())) {
                return substitute;
            }
        }
        return super.visitParameter(expression);
    }

    public Expression visitProduct(ProductExpression expression) {
        boolean success = false;
        ArrayList<Expression> operands = new ArrayList<Expression>();
        ArrayList<NumericExpression> exponents = new ArrayList<NumericExpression>();
        for (int i = 0; i < expression.getOperands().length; i++) {
            Expression sub = expression.getOperands()[i].accept(this);
            if (!expression.getOperands()[i].equals(sub)) {
                success = true;
            }
            operands.add(sub);
            exponents.add(expression.getExponents()[i]);
        }
        if (success) {
            return new ProductExpression(operands.toArray(new Expression[0]),
                    exponents.toArray(new NumericExpression[0]));
        }
        return super.visitProduct(expression);
    }

    public Expression visitSum(SumExpression expression) {
        boolean success = false;
        ArrayList<Expression> operands = new ArrayList<Expression>();
        ArrayList<NumericExpression> coefs = new ArrayList<NumericExpression>();
        for (int i = 0; i < expression.getOperands().length; i++) {
            Expression sub = expression.getOperands()[i].accept(this);
            if (!expression.getOperands()[i].equals(sub)) {
                success = true;
            }
            operands.add(sub);
            coefs.add(expression.getCoefficients()[i]);
        }
        if (success) {
            return new SumExpression(operands.toArray(new Expression[0]), coefs.toArray(new NumericExpression[0]));
        }
        return super.visitSum(expression);
    }

    public Expression visitVariable(VariableExpression expression) {
        if (target.getType() == ExpressionType.Variable) {
            VariableExpression varTarget = (VariableExpression) target;
            if (varTarget.getName().equals(expression.getName())) {
                return substitute;
            }
        }
        return super.visitVariable(expression);
    }
}
