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

package io.luan.exp4j;

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

public interface ExpressionVisitor {

    Expression visitComparison(ComparisonExpression expression);

    Expression visitLogicalAnd(LogicalAndExpression expression);

    Expression visitLogicalNot(LogicalNotExpression expression);

    Expression visitLogicalOr(LogicalOrExpression expression);

    Expression visitBooleanValue(BooleanValueExpression expression);

    Expression visitConditional(ConditionalExpression expression);

    Expression visitConstant(ConstantExpression expression);

    Expression visitNumber(NumberExpression expression);

    Expression visitExpression(Expression expression);

    Expression visitFunction(FunctionExpression expression);

    Expression visitParameter(ParameterExpression expression);

    Expression visitPower(PowerExpression expression);

    Expression visitProduct(ProductExpression expression);

    Expression visitSum(SumExpression expression);

    Expression visitVariable(VariableExpression expression);
}
