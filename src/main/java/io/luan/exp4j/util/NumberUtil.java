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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

/**
 * 基于Number的计算，尽可能的保留Precision
 * <p>
 * TODO: Scale down e.g. Long to Integer when possible
 */
public class NumberUtil {

    public static Number abs(Number number) {
        if (number == null) {
            return null;
        }

        if (number instanceof Integer) {
            return (Integer) number >= 0 ? number : negate(number);
        }

        if (number instanceof Long) {
            return (Long) number >= 0 ? number : negate(number);
        }

        if (number instanceof BigInteger) {
            return ((BigInteger) number).compareTo(BigInteger.ZERO) >= 0 ? number : negate(number);
        }

        if (number instanceof BigDecimal) {
            return ((BigDecimal) number).compareTo(BigDecimal.ZERO) >= 0 ? number : negate(number);
        }

        throw new UnsupportedOperationException();
    }

    public static Number add(Number left, Number right) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Integer) {
            Integer intLeft = (Integer) left;
            if (right instanceof Integer) {
                return add(intLeft, (Integer) right);
            }
            if (right instanceof Long) {
                return add(Long.valueOf(intLeft), (Long) right);
            }
            if (right instanceof BigInteger) {
                return BigInteger.valueOf(intLeft).add((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(intLeft).add((BigDecimal) right);
            }
        }

        if (left instanceof Long) {
            Long longLeft = (Long) left;
            if (right instanceof Integer) {
                return add(longLeft, Long.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return add(longLeft, (Long) right);
            }
            if (right instanceof BigInteger) {
                return BigInteger.valueOf(longLeft).add((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(longLeft).add((BigDecimal) right);
            }
        }

        if (left instanceof BigInteger) {
            BigInteger biLeft = (BigInteger) left;
            if (right instanceof Integer) {
                return add(biLeft, BigInteger.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return add(biLeft, BigInteger.valueOf((Long) right));
            }
            if (right instanceof BigInteger) {
                return biLeft.add((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return new BigDecimal(biLeft).add((BigDecimal) right);
            }
        }

        if (left instanceof BigDecimal) {
            BigDecimal bdLeft = (BigDecimal) left;
            if (right instanceof Integer) {
                return bdLeft.add(BigDecimal.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return bdLeft.add(BigDecimal.valueOf((Long) right));
            }
            if (right instanceof BigInteger) {
                return bdLeft.add(new BigDecimal((BigInteger) right));
            }
            if (right instanceof BigDecimal) {
                return bdLeft.add((BigDecimal) right);
            }
        }

        throw new UnsupportedOperationException();
    }

    private static Number add(Integer left, Integer right) {
        int i1 = left;
        int i2 = right;
        if (i1 == 0) {
            return right;
        }
        if (i2 == 0) {
            return left;
        }

        // Wont overflow
        if (i1 >= 0 && i2 <= 0 || i1 <= 0 && i2 >= 0) {
            return i1 + i2;
        }

        // Overflown
        int rst = i1 + i2;
        if (i1 > 0 && rst < i1 || i1 < 0 && rst > i1) {
            return (long) i1 + (long) i2;
        }
        return rst;
    }

    private static Number add(Long left, Long right) {
        long l1 = left;
        long l2 = right;
        if (l1 == 0L) {
            return right;
        }
        if (l2 == 0L) {
            return left;
        }

        // Wont overflow
        if (l1 >= 0L && l2 <= 0L || l1 <= 0L && l2 >= 0L) {
            return l1 + l2;
        }

        // Overflown
        long rst = l1 + l2;
        if (l1 > 0L && rst < l1 || l1 < 0L && rst > l1) {
            BigInteger bi1 = BigInteger.valueOf(l1);
            BigInteger bi2 = BigInteger.valueOf(l2);
            return bi1.add(bi2);
        }
        return rst;
    }

    public static int compare(Number left, Number right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException();
        }

        if (left instanceof Integer) {
            Integer intLeft = (Integer) left;
            if (right instanceof Integer) {
                return intLeft.compareTo((Integer) right);
            }
            if (right instanceof Long) {
                return Long.valueOf(intLeft).compareTo((Long) right);
            }
            if (right instanceof BigInteger) {
                return BigInteger.valueOf(intLeft).compareTo((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(intLeft).compareTo((BigDecimal) right);
            }
        }

        if (left instanceof Long) {
            Long longLeft = (Long) left;
            if (right instanceof Integer) {
                return longLeft.compareTo(right.longValue());
            }
            if (right instanceof Long) {
                return longLeft.compareTo(right.longValue());
            }
            if (right instanceof BigInteger) {
                return BigInteger.valueOf(longLeft).compareTo((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(longLeft).compareTo((BigDecimal) right);
            }
        }

        if (left instanceof BigInteger) {
            BigInteger biLeft = (BigInteger) left;
            if (right instanceof Integer) {
                return biLeft.compareTo(BigInteger.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return biLeft.compareTo(BigInteger.valueOf((Long) right));
            }
            if (right instanceof BigInteger) {
                return biLeft.compareTo((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return new BigDecimal(biLeft).compareTo((BigDecimal) right);
            }
        }

        if (left instanceof BigDecimal) {
            BigDecimal bdLeft = (BigDecimal) left;
            if (right instanceof Integer) {
                return bdLeft.compareTo(BigDecimal.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return bdLeft.compareTo(BigDecimal.valueOf((Long) right));
            }
            if (right instanceof BigInteger) {
                return bdLeft.compareTo(new BigDecimal((BigInteger) right));
            }
            if (right instanceof BigDecimal) {
                return bdLeft.compareTo((BigDecimal) right);
            }
        }

        throw new UnsupportedOperationException();
    }

    public static Number divide(Number left, Number right) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Integer) {
            Integer intLeft = (Integer) left;
            if (right instanceof Integer) {
                return divide(intLeft, (Integer) right);
            }
            if (right instanceof Long) {
                return divide(Long.valueOf(intLeft), (Long) right);
            }
            if (right instanceof BigInteger) {
                return BigDecimal.valueOf(intLeft).divide(new BigDecimal((BigInteger) right), MathContext.DECIMAL128);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(intLeft).divide((BigDecimal) right, MathContext.DECIMAL128);
            }
        }

        if (left instanceof Long) {
            Long longLeft = (Long) left;
            if (right instanceof Integer) {
                return divide(longLeft, Long.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return divide(longLeft, (Long) right);
            }
            if (right instanceof BigInteger) {
                return BigDecimal.valueOf(longLeft).divide(new BigDecimal((BigInteger) right), MathContext.DECIMAL128);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(longLeft).divide((BigDecimal) right, MathContext.DECIMAL128);
            }
        }

        if (left instanceof BigInteger) {
            BigInteger biLeft = (BigInteger) left;
            if (right instanceof Integer) {
                return divide(biLeft, BigInteger.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return divide(biLeft, BigInteger.valueOf((Long) right));
            }
            if (right instanceof BigInteger) {
                return new BigDecimal(biLeft).divide(new BigDecimal((BigInteger) right), MathContext.DECIMAL128);
            }
            if (right instanceof BigDecimal) {
                return new BigDecimal(biLeft).divide((BigDecimal) right, MathContext.DECIMAL128);
            }
        }

        if (left instanceof BigDecimal) {
            BigDecimal bdLeft = (BigDecimal) left;
            if (right instanceof Integer) {
                return bdLeft.divide(BigDecimal.valueOf((Integer) right), MathContext.DECIMAL128);
            }
            if (right instanceof Long) {
                return bdLeft.divide(BigDecimal.valueOf((Long) right), MathContext.DECIMAL128);
            }
            if (right instanceof BigInteger) {
                return bdLeft.divide(new BigDecimal((BigInteger) right), MathContext.DECIMAL128);
            }
            if (right instanceof BigDecimal) {
                return bdLeft.divide((BigDecimal) right, MathContext.DECIMAL128);
            }
        }

        throw new UnsupportedOperationException();
    }

    private static Number divide(Integer left, Integer right) {
        int i1 = left;
        int i2 = right;
        if (i1 == 0) {
            return 0;
        }
        if (i2 == 1) {
            return left;
        }
        if (i2 == -1) {
            return negate(i1);
        }

        // If can be divided equally then return int
        int result = i1 / i2;
        if (result * i2 == i1) {
            return result;
        }

        return BigDecimal.valueOf(i1).divide(BigDecimal.valueOf(i2), MathContext.DECIMAL128);
    }

    private static Number divide(Long left, Long right) {
        long l1 = left;
        long l2 = right;
        if (l1 == 0L) {
            return 0;
        }
        if (l2 == 1L) {
            return left;
        }
        if (l2 == -1L) {
            return negate(l1);
        }

        long result = l1 / l2;
        if (result * l2 == l1) {
            return result;
        }

        return BigDecimal.valueOf(l1).divide(BigDecimal.valueOf(l2), MathContext.DECIMAL128);
    }

    public static boolean equate(Number left, Number right) {
        if (left == null || right == null) {
            return false;
        }

        if (left instanceof Integer) {
            Integer intLeft = (Integer) left;
            if (right instanceof Integer) {
                return left.intValue() == right.intValue();
            }
            if (right instanceof Long) {
                return left.longValue() == right.longValue();
            }
            if (right instanceof BigInteger) {
                return BigInteger.valueOf(intLeft).equals(right);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(intLeft).equals(right);
            }
        }

        if (left instanceof Long) {
            Long longLeft = (Long) left;
            if (right instanceof Integer) {
                return left.longValue() == right.longValue();
            }
            if (right instanceof Long) {
                return left.longValue() == right.longValue();
            }
            if (right instanceof BigInteger) {
                return BigInteger.valueOf(longLeft).equals(right);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(longLeft).equals(right);
            }
        }

        if (left instanceof BigInteger) {
            BigInteger biLeft = (BigInteger) left;
            if (right instanceof Integer) {
                return biLeft.equals(BigInteger.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return biLeft.equals(BigInteger.valueOf((Long) right));
            }
            if (right instanceof BigInteger) {
                return biLeft.equals(right);
            }
            if (right instanceof BigDecimal) {
                return new BigDecimal(biLeft).equals(right);
            }
        }

        if (left instanceof BigDecimal) {
            BigDecimal bdLeft = (BigDecimal) left;
            if (right instanceof Integer) {
                return bdLeft.equals(BigDecimal.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return bdLeft.equals(BigDecimal.valueOf((Long) right));
            }
            if (right instanceof BigInteger) {
                return bdLeft.equals(new BigDecimal((BigInteger) right));
            }
            if (right instanceof BigDecimal) {
                return bdLeft.equals(right);
            }
        }

        throw new UnsupportedOperationException();
    }

    public static Number multiply(Number left, Number right) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Integer) {
            Integer intLeft = (Integer) left;
            if (right instanceof Integer) {
                return multiply(intLeft, (Integer) right);
            }
            if (right instanceof Long) {
                return multiply(Long.valueOf(intLeft), (Long) right);
            }
            if (right instanceof BigInteger) {
                return BigInteger.valueOf(intLeft).multiply((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(intLeft).multiply((BigDecimal) right);
            }
        }

        if (left instanceof Long) {
            Long longLeft = (Long) left;
            if (right instanceof Integer) {
                return multiply(longLeft, Long.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return multiply(longLeft, (Long) right);
            }
            if (right instanceof BigInteger) {
                return BigInteger.valueOf(longLeft).multiply((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(longLeft).multiply((BigDecimal) right);
            }
        }

        if (left instanceof BigInteger) {
            BigInteger biLeft = (BigInteger) left;
            if (right instanceof Integer) {
                return multiply(biLeft, BigInteger.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return multiply(biLeft, BigInteger.valueOf((Long) right));
            }
            if (right instanceof BigInteger) {
                return biLeft.multiply((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return new BigDecimal(biLeft).multiply((BigDecimal) right);
            }
        }

        if (left instanceof BigDecimal) {
            BigDecimal bdLeft = (BigDecimal) left;
            if (right instanceof Integer) {
                return bdLeft.multiply(BigDecimal.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return bdLeft.multiply(BigDecimal.valueOf((Long) right));
            }
            if (right instanceof BigInteger) {
                return bdLeft.multiply(new BigDecimal((BigInteger) right));
            }
            if (right instanceof BigDecimal) {
                return bdLeft.multiply((BigDecimal) right);
            }
        }

        throw new UnsupportedOperationException();
    }

    private static Number multiply(Integer left, Integer right) {
        int i1 = left;
        int i2 = right;
        if (i1 == 0 || i2 == 0) {
            return 0;
        }

        if (i1 == 1) {
            return right;
        }
        if (i2 == 1) {
            return left;
        }

        if (i1 == -1) {
            return negate(i2);
        }
        if (i2 == -1) {
            return negate(i1);
        }

        // Check Overflow
        long product = (long) i1 * (long) i2;
        if (product > Integer.MAX_VALUE || product < Integer.MIN_VALUE) {
            return product;
        }
        return i1 * i2;
    }

    private static Number multiply(Long left, Long right) {
        long l1 = left;
        long l2 = right;
        if (l1 == 0L || l2 == 0L) {
            return 0;
        }

        if (l1 == 1L) {
            return right;
        }
        if (l2 == 1L) {
            return left;
        }

        if (l1 == -1L) {
            return negate(l2);
        }
        if (l2 == -1L) {
            return negate(l1);
        }

        // Check Overflow - Potential bug
        BigInteger biProduct = BigInteger.valueOf(l1).multiply(BigInteger.valueOf(l2));
        long longProduct = l1 * l2;
        if (biProduct.longValue() == longProduct) {
            return longProduct;
        }
        return biProduct;
    }

    public static Number negate(Number number) {
        if (number == null) {
            return null;
        }

        if (number instanceof Integer) {
            return negate(number.intValue());
        }
        if (number instanceof Long) {
            return negate(number.longValue());
        }
        if (number instanceof BigInteger) {
            return ((BigInteger) number).negate();
        }
        if (number instanceof BigDecimal) {
            return ((BigDecimal) number).negate();
        }

        throw new UnsupportedOperationException();
    }

    private static Number negate(int intVal) {
        if (intVal == Integer.MIN_VALUE) {
            return -(long) intVal;
        }
        return -intVal;
    }

    private static Number negate(long longVal) {
        if (longVal == Long.MIN_VALUE) {
            return BigInteger.valueOf(longVal).negate();
        }
        return -longVal;
    }

    public static Number parse(String text) {
        // Case 1 test for integer
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            // do nothing
        }

        // Case 2 test for long
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            // do nothing
        }

        // Case 3 BigInteger
        try {
            return new BigInteger(text);
        } catch (NumberFormatException e) {
            // do nothing
        }

        // Case 4 BigDecimal
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException e) {
            // do nothing
        }

        return null;
    }

    public static Number power(Number left, Number right) {
        if (left == null || right == null) {
            return null;
        }

        if (right instanceof Integer) {
            if (left instanceof Integer) {
                double result = Math.pow(left.intValue(), right.intValue());
                if (result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
                    return (int) result;
                }
                if (result <= Long.MAX_VALUE && result >= Long.MIN_VALUE) {
                    return (long) result;
                }

                BigInteger biLeft = BigInteger.valueOf(left.longValue());
                return biLeft.pow(right.intValue());
            }
            if (left instanceof Long) {
                double result = Math.pow(left.longValue(), right.intValue());
                if (result <= Integer.MAX_VALUE && result >= Integer.MIN_VALUE) {
                    return (int) result;
                }
                if (result <= Long.MAX_VALUE && result >= Long.MIN_VALUE) {
                    return (long) result;
                }

                BigInteger biLeft = BigInteger.valueOf(left.longValue());
                return biLeft.pow(right.intValue());
            }
            if (left instanceof BigInteger) {
                if (right.intValue() >= 0) {
                    return ((BigInteger) left).pow(right.intValue());
                }

                BigDecimal divisor = new BigDecimal((BigInteger) left).pow(-right.intValue());
                return BigDecimal.ONE.divide(divisor, BigDecimal.ROUND_DOWN);
            }
            if (left instanceof BigDecimal) {
                if (right.intValue() >= 0) {
                    return ((BigDecimal) left).pow(right.intValue());
                }

                BigDecimal divisor = ((BigDecimal) left).pow(-right.intValue());
                return BigDecimal.ONE.divide(divisor, BigDecimal.ROUND_DOWN);
            }
        }
        throw new UnsupportedOperationException();
    }

    public static Number subtract(Number left, Number right) {
        if (left == null || right == null) {
            return null;
        }

        if (left instanceof Integer) {
            Integer intLeft = (Integer) left;
            if (right instanceof Integer) {
                return subtract(intLeft, (Integer) right);
            }
            if (right instanceof Long) {
                return subtract(Long.valueOf(intLeft), (Long) right);
            }
            if (right instanceof BigInteger) {
                return BigInteger.valueOf(intLeft).subtract((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(intLeft).subtract((BigDecimal) right);
            }
        }

        if (left instanceof Long) {
            Long longLeft = (Long) left;
            if (right instanceof Integer) {
                return subtract(longLeft, Long.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return subtract(longLeft, (Long) right);
            }
            if (right instanceof BigInteger) {
                return BigInteger.valueOf(longLeft).subtract((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return BigDecimal.valueOf(longLeft).subtract((BigDecimal) right);
            }
        }

        if (left instanceof BigInteger) {
            BigInteger biLeft = (BigInteger) left;
            if (right instanceof Integer) {
                return subtract(biLeft, BigInteger.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return subtract(biLeft, BigInteger.valueOf((Long) right));
            }
            if (right instanceof BigInteger) {
                return biLeft.subtract((BigInteger) right);
            }
            if (right instanceof BigDecimal) {
                return new BigDecimal(biLeft).subtract((BigDecimal) right);
            }
        }

        if (left instanceof BigDecimal) {
            BigDecimal bdLeft = (BigDecimal) left;
            if (right instanceof Integer) {
                return bdLeft.subtract(BigDecimal.valueOf((Integer) right));
            }
            if (right instanceof Long) {
                return bdLeft.subtract(BigDecimal.valueOf((Long) right));
            }
            if (right instanceof BigInteger) {
                return bdLeft.subtract(new BigDecimal((BigInteger) right));
            }
            if (right instanceof BigDecimal) {
                return bdLeft.subtract((BigDecimal) right);
            }
        }

        throw new UnsupportedOperationException();
    }

    private static Number subtract(Integer left, Integer right) {
        int i1 = left;
        int i2 = right;
        if (i2 == 0) {
            return left;
        }
        if (i1 == 0) {
            return negate(i2);
        }

        // Wont overflow
        if (i1 >= 0 && i2 >= 0 || i1 <= 0 && i2 <= 0) {
            return i1 - i2;
        }

        // Overflown
        int rst = i1 - i2;
        if (i1 > 0 && rst < i1 || i1 < 0 && rst > i1) {
            return (long) i1 - (long) i2;
        }
        return rst;
    }

    private static Number subtract(Long left, Long right) {
        long l1 = left;
        long l2 = right;
        if (l2 == 0L) {
            return left;
        }
        if (l1 == 0L) {
            return negate(l2);
        }

        // Wont Overflow
        if (l1 >= 0L && l2 >= 0L || l1 <= 0L && l2 <= 0L) {
            return l1 - l2;
        }

        // Overflown
        long rst = l1 - l2;
        if (l1 > 0L && rst < l1 || l1 < 0L && rst > l1) {
            BigInteger bi1 = BigInteger.valueOf(l1);
            BigInteger bi2 = BigInteger.valueOf(l2);
            return bi1.subtract(bi2);
        }
        return rst;
    }
}
