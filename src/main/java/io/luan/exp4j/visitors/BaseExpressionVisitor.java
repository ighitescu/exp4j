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
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.arithmetic.PowerExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.comparison.ComparisonExpression;
import io.luan.exp4j.expressions.conditional.ConditionalExpression;
import io.luan.exp4j.expressions.function.FunctionExpression;
import io.luan.exp4j.expressions.logical.LogicalAndExpression;
import io.luan.exp4j.expressions.logical.LogicalNotExpression;
import io.luan.exp4j.expressions.logical.LogicalOrExpression;
import io.luan.exp4j.expressions.symbolic.ConstantExpression;
import io.luan.exp4j.expressions.symbolic.ParameterExpression;
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import io.luan.exp4j.expressions.type.BooleanValueExpression;
import io.luan.exp4j.expressions.type.NumberExpression;

public class BaseExpressionVisitor implements ExpressionVisitor {

    @Override
    public Expression visitComparison(ComparisonExpression expression) {
        return expression;
    }

    @Override
    public Expression visitLogicalAnd(LogicalAndExpression expression) {
        return expression;
    }

    @Override
    public Expression visitLogicalNot(LogicalNotExpression expression) {
        return expression;
    }

    @Override
    public Expression visitLogicalOr(LogicalOrExpression expression) {
        return expression;
    }

    @Override
    public Expression visitBooleanValue(BooleanValueExpression expression) {
        return expression;
    }

    @Override
    public Expression visitConditional(ConditionalExpression expression) {
        return expression;
    }

    @Override
    public Expression visitConstant(ConstantExpression expression) {
        return expression;
    }

    @Override
    public Expression visitNumber(NumberExpression expression) {
        return expression;
    }

    @Override
    public Expression visitExpression(Expression expression) {
        return expression;
    }

    @Override
    public Expression visitFunction(FunctionExpression expression) {
        return expression;
    }

    @Override
    public Expression visitParameter(ParameterExpression expression) {
        return expression;
    }

    @Override
    public Expression visitPower(PowerExpression expression) {
        return expression;
    }

    @Override
    public Expression visitProduct(ProductExpression expression) {
        return expression;
    }

    @Override
    public Expression visitSum(SumExpression expression) {
        return expression;
    }

    @Override
    public Expression visitVariable(VariableExpression expression) {
        return expression;
    }

}
