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
import io.luan.exp4j.parser.ExpressionParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ExpressionParserTest {

    @Test
    public void testMember() {
        ExpressionParser parser = new ExpressionParser("a.b");
        Expression exp = parser.parse();

        Assert.assertEquals(ExpressionType.Member, exp.getType());
        System.out.println(exp);
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

    @Test
    public void testFunction() {
        ExpressionParser parser = new ExpressionParser("abs(x+ 4)");
        Expression exp = parser.parse();

        System.out.println(exp);
        Assert.assertEquals(ExpressionType.Function, exp.getType());

        Map<String, Object> input = new HashMap<>();
        input.put("x", -5);
        Expression result = exp.evaluate(input);
        System.out.println(result);
    }
}