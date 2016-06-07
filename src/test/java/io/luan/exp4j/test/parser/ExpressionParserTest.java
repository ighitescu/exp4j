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

package io.luan.exp4j.test.parser;


import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.symbolic.MethodExpression;
import io.luan.exp4j.expressions.value.NumberExpression;
import io.luan.exp4j.parser.ExpressionParser;
import io.luan.exp4j.util.NumberUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ExpressionParserTest {

    public static Number sum(Number[] numbers) {
        return NumberUtil.add(numbers[0], numbers[1]);
    }

    public class TestMethodObj {
        public int b = 102;

        public TestMethodObj(int x) {
            this.b = x;
        }

        public int sum(int a, int b) {
            return a + b;
        }
    }

    @Test
    public void testKnownFunction() {
        Expression exp = Expression.parse("abs(-1)");
        Map<String, Object> input = new HashMap<>();

        Expression result = exp.evaluate(input);
        System.out.println(result);
    }

    @Test
    public void testExternalFunction() {
        ExpressionParser parser = new ExpressionParser("sum(x, 4)");
        Expression exp = parser.parse();

        System.out.println(exp);
        Assert.assertEquals(ExpressionType.Function, exp.getType());

        Map<String, Object> input = new HashMap<>();
        input.put("x", 3);

        Map<String, Object> funcs = new HashMap<>();
        funcs.put("sum", new TestMethodObj(1));

        Expression result = exp.evaluate(input, funcs);
        System.out.println(result);
    }

    @Test
    public void testMember() {
        ExpressionParser parser = new ExpressionParser("a.b");
        Expression exp = parser.parse();


        Assert.assertEquals(ExpressionType.Member, exp.getType());
        System.out.println(exp);

        Map<String, Object> input = new HashMap<>();
        input.put("a", new TestMethodObj(203));

        Expression result = exp.evaluate(input);
        System.out.println(result);
        Assert.assertEquals(result.getType(), ExpressionType.Number);
        NumberExpression numExp = (NumberExpression) result;
        Integer intVal = (Integer) numExp.getNumber();
        Assert.assertEquals(203, (int) intVal);
    }

    @Test
    public void testMethod() {
        ExpressionParser parser = new ExpressionParser("a.b(g(),d)");
        Expression exp = parser.parse();

        System.out.println(exp);
        Assert.assertEquals(ExpressionType.Method, exp.getType());
        MethodExpression mExp = (MethodExpression) exp;
        Expression[] params = mExp.getParameters();
        Assert.assertEquals(2, params.length);
    }
}