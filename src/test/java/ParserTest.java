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
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ParserTest {

    @Test
    public void parseGreatThan() {

        Expression exp = Expression.parse("(x > 3) ? (x>4 ? x*2 : x*10) : x + 2");
        Map<String, Object> input = new HashMap<String, Object>(10);
        input.put("x", 3.512345);

        System.out.println(exp);

        Expression result = exp.evaluate(input);

        System.out.println(result);

    }

    @Test
    public void testNumbers() {
        Expression exp = Expression.parse("x / 10");
        Map<String, Object> input = new HashMap<String, Object>(10);
        input.put("x", 100);
        System.out.println(exp);

        Expression result = exp.evaluate(input);
        System.out.println(result);

    }

}
