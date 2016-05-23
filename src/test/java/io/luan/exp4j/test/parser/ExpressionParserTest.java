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
import io.luan.exp4j.parser.ExpressionParser;
import org.junit.Test;

public class ExpressionParserTest {

    @Test
    public void testA() {
        ExpressionParser parser = new ExpressionParser("-a");
        Expression exp = parser.parse();
        System.out.println(exp);

        ExpressionParser parser2 = new ExpressionParser("-123");
        Expression exp2 = parser2.parse();
        System.out.println(exp2);


        ExpressionParser parser3 = new ExpressionParser("abc + -23");
        Expression exp3 = parser3.parse();
        System.out.println(exp3);

        ExpressionParser parser4 = new ExpressionParser("abc + +23");
        Expression exp4 = parser4.parse();
        System.out.println(exp4);
    }
}
