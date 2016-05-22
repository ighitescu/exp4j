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

import io.luan.exp4j.Config;

import java.math.BigDecimal;

public class NumberFormatter {
    private static final BigDecimal POSITIVE_UPPER_BOUND = new BigDecimal("9999999999"); // 9 9s
    private static final BigDecimal POSITIVE_LOWER_BOUND = new BigDecimal("0.000000001"); // 9 zeroes

    private static final BigDecimal NEGATIVE_UPPER_BOUND = new BigDecimal("-0.000000001"); // 9 9s
    private static final BigDecimal NEGATIVE_LOWER_BOUND = new BigDecimal("-9999999999"); // 9 zeroes

    public static String format(BigDecimal decimal) {
        BigDecimal dec = decimal.stripTrailingZeros();

        if (decimal.signum() > 0) {
            if (decimal.compareTo(POSITIVE_UPPER_BOUND) <= 0 && decimal.compareTo(POSITIVE_LOWER_BOUND) >= 0) {
                return dec.toPlainString();
            }
        }

        if (decimal.signum() < 0) {
            if (decimal.compareTo(NEGATIVE_UPPER_BOUND) <= 0 && decimal.compareTo(NEGATIVE_LOWER_BOUND) >= 0) {
                return dec.toPlainString();
            }
        }

        return dec.toString();
    }

    public static String format(Number number) {
        if (Config.DEBUG) {
            System.out.println("[FORMAT] " + number.getClass().getSimpleName());
        }

        if (number instanceof BigDecimal) {
            return NumberFormatter.format((BigDecimal) number);
        }
        return number.toString();
    }
}
