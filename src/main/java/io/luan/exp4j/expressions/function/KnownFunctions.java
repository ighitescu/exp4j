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

package io.luan.exp4j.expressions.function;

import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.value.NumberExpression;
import io.luan.exp4j.util.NumberUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A set of functions known by the system.
 * Can be evaluated without having to
 */
public class KnownFunctions {

    public static Function<NumericExpression[], NumericExpression> ABS;

    private static Map<String, Function<NumericExpression[], NumericExpression>> funcMap;

    static {
        ABS = KnownFunctions::abs;

        fillKnownFunctions();
    }

    public static Function<NumericExpression[], NumericExpression> getFunc(String name) {
        return funcMap.get(name);
    }

    private static NumericExpression abs(NumericExpression[] params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("params.length != 1");
        }

        NumericExpression exp = params[0];
        if (exp instanceof NumberExpression) {
            NumberExpression numExp = (NumberExpression) exp;
            Number num = numExp.getNumber();
            Number absNum = NumberUtil.abs(num);
            if (num.equals(absNum)) {
                return exp;
            }
            return new NumberExpression(absNum);
        }

        throw new IllegalArgumentException("Input must be a NumberExpression");
    }

    private static void fillKnownFunctions() {
        funcMap = new HashMap<>();
        funcMap.put("abs", ABS);
    }
}
