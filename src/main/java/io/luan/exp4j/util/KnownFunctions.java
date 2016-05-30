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

package io.luan.exp4j.util;

import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.value.NumberExpression;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A set of functions known by the system.
 * Can be evaluated without having to
 */
public class KnownFunctions {

    public static Function<Number[], Number> ABS;

    private static Map<String, Function<Number[], Number>> funcMap;

    static {
        ABS = KnownFunctions::abs;

        fillKnownFunctions();
    }

    private static Number abs(Number[] params) {
        if (params.length != 1) {
            throw new IllegalArgumentException("params.length != 1");
        }

        Number num = params[0];
        return NumberUtil.abs(num);
    }

    private static void fillKnownFunctions() {
        funcMap = new HashMap<>();
        funcMap.put("abs", ABS);
    }

    public static Function<Number[], Number> getFunc(String name) {
        return funcMap.get(name);
    }
}
