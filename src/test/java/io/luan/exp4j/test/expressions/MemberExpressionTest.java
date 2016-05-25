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
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Miao on 5/25/2016.
 */
public class MemberExpressionTest {

    @Test
    public void testMember() {
        VariableExpression xVar = (VariableExpression) Expression.variable("x");
        MemberExpression member = new MemberExpression(xVar, "xxx");

        System.out.println(member);

        Map<String, Object> input = new HashMap<>();
        input.put("x", new TestMember() );
        Expression result = member.evaluate(input);
        System.out.println(result);
        System.out.println(result.getClass());
    }

    @Test
    public void testString() {
        VariableExpression xVar = (VariableExpression) Expression.variable("x");

        Map<String, Object> input = new HashMap<>();
        input.put("x", 0.1 );
        Expression result = xVar.evaluate(input);
        System.out.println(result);
        System.out.println(result.getClass());
    }

    public class TestMember {
       public int xxx = 12;
    }
}
