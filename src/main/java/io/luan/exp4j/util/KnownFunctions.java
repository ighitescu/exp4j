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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * A set of functions known by the system.
 * Can be evaluated without having to
 */
public class KnownFunctions {

    private static Map<String, Method> methodMap;

    static {
        fillKnownFunctions();
    }

    public static Number abs(Number num) {
        return NumberUtil.abs(num);
    }

    public static Object invoke(String name, Object[] params) {
        Method method = methodMap.get(name);
        if (method == null) {
            return null;
        }

        try {
            return method.invoke(new Object(), params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void fillKnownFunctions() {
        methodMap = new HashMap<>();
        Method[] methods = KnownFunctions.class.getMethods();
        for (Method method : methods) {
            System.out.println(method);
            if (method.getModifiers() == (Modifier.STATIC | Modifier.PUBLIC)) {
                if (!method.getName().equals("invoke")) {
                    methodMap.put(method.getName(), method);
                }
            }
        }
    }
}
