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

import io.luan.exp4j.Expression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.comparison.ComparisonExpression;
import io.luan.exp4j.expressions.conditional.ConditionalExpression;
import io.luan.exp4j.expressions.type.BooleanValueExpression;
import io.luan.exp4j.expressions.NumericExpression;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MainTest {
    @Test
    public void evaluatesExpression() {

        Expression exp = Expression.parse("3.3 + (x^2 + 3.2) * x +  B + B * 3");
        Map<String, Object> input = new HashMap<String, Object>(10);
        input.put("x", 10);
        input.put("B", 2);
        System.out.println("-----" + Expression.variables(exp));
        System.out.println("-----" + Expression.constants(exp));

        Expression result = exp.evaluate(input);

        System.out.println(result);
    }

    @Test
    public void testConditional() {
        Expression left = Expression.parse("x");
        Expression right = Expression.parse("3");

        ComparisonExpression gt = new ComparisonExpression(left, right, ComparisonExpression.ComparisonOperator.GreaterThan);

        Expression ifTrue = Expression.parse("x ^ 2");
        Expression ifFalse = Expression.parse("x - 1");

        ConditionalExpression ifExp = new ConditionalExpression(gt, ifTrue, ifFalse);

        Map<String, Object> input = new HashMap<>();
        input.put("x", 4);

        System.out.println(gt);
        System.out.println(ifExp);

        Expression result = ifExp.evaluate(input);
        Assert.assertNotNull(result);

        System.out.println(result);
        Assert.assertTrue(result instanceof NumericExpression);

    }

    @Test
    public void testConditionalSum() {
        Expression left = Expression.parse("x");
        Expression right = Expression.parse("3");

        ComparisonExpression gt = new ComparisonExpression(left, right, ComparisonExpression.ComparisonOperator.GreaterThan);

        Expression ifTrue = Expression.parse("x ^ 2");
        Expression ifFalse = Expression.parse("x - 1");

        ConditionalExpression ifExp = new ConditionalExpression(gt, ifTrue, ifFalse);

        Expression another = Expression.parse("x + 4");

        SumExpression sum = new SumExpression(new Expression[]{ifExp, another});

        Map<String, Object> input = new HashMap<>();
        input.put("x", 2);

        System.out.println(gt);
        System.out.println(ifExp);
        System.out.println(sum);

        Expression result = sum.evaluate(input);
        Assert.assertNotNull(result);

        System.out.println(result);

    }

    @Test
    public void testGreaterThan() {
        Expression left = Expression.parse("x");
        Expression right = Expression.parse("3");

        ComparisonExpression gt = new ComparisonExpression(left, right, ComparisonExpression.ComparisonOperator.GreaterThan);

        Expression ifTrue = Expression.parse("x ^ 2");
        Expression ifFalse = Expression.parse("x - 1");

        ConditionalExpression ifExp = new ConditionalExpression(gt, ifTrue, ifFalse);

        Map<String, Object> input = new HashMap<>();
        input.put("x", 4);

        System.out.println(gt);
        System.out.println(ifExp);

        Expression result = gt.evaluate(input);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof BooleanValueExpression);

        BooleanValueExpression boolValExp = (BooleanValueExpression) result;
        Assert.assertTrue(boolValExp.getBooleanValue());

        System.out.println(result);
    }
}