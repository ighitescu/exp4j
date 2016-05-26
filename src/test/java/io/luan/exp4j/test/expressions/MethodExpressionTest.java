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

package io.luan.exp4j.test.expressions;

import io.luan.exp4j.Expression;
import io.luan.exp4j.expressions.symbolic.MemberExpression;
import io.luan.exp4j.expressions.symbolic.MethodExpression;
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MethodExpressionTest {

    @Test
    public void testMethod() {
        VariableExpression xVar = (VariableExpression) Expression.variable("x");
        VariableExpression aVar = (VariableExpression) Expression.variable("a");
        VariableExpression bVar = (VariableExpression) Expression.variable("b");
        MethodExpression exp = new MethodExpression(xVar, "test", new Expression[]{aVar, bVar});

        System.out.println(exp);

        Map<String, Object> input = new HashMap<>();
        input.put("x", new TestMethod() );
        input.put("a", 1);
        input.put("b", 2);
        Expression result = exp.evaluate(input);
        System.out.println(result);
        System.out.println(result.getClass());
    }

    public class TestMethod {
       public int xxx = 12;
        public int test(Integer a, Integer b) {
            return a + b;
        }
    }
}
