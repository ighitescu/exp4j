package io.luan.exp4j.expressions.util;

import io.luan.exp4j.Expression;
import io.luan.exp4j.expressions.value.BooleanValueExpression;
import io.luan.exp4j.expressions.value.NumberExpression;
import io.luan.exp4j.expressions.value.ObjectExpression;
import io.luan.exp4j.util.NumberUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ExpressionUtil {

    /**
     * Convert object into expression based on object's value
     *   - Numbers are converted into number related expressions
     *   - Boolean is converted into BooleanValueExpression
     *   - String is first try to convert into a number
     *     - if doesn't work, then use String value
     *   - If all above fails use ObjectExpression
     */
    public static Expression objToExpression(Object obj) {
        if (obj == null) {
            return null;
        }

        if (obj instanceof Boolean) {
            boolean bool = (Boolean)obj;
            return bool ? BooleanValueExpression.True : BooleanValueExpression.False;
        }
        if (obj instanceof Integer) {
            return new NumberExpression((Integer) obj);
        }
        if (obj instanceof Long) {
            return new NumberExpression((Long) obj);
        }
        if (obj instanceof BigInteger) {
            return new NumberExpression((BigInteger) obj);
        }
        if (obj instanceof BigDecimal) {
            return new NumberExpression((BigDecimal) obj);
        }
        if (obj instanceof Double) {
            return new NumberExpression(BigDecimal.valueOf((Double) obj));
        }
        if (obj instanceof String) {
            Number number = NumberUtil.parse((String) obj);
            if (number != null) {
                return new NumberExpression(number);
            }
            else {
                throw new NumberFormatException("Only strings representing number is currently supported");
            }
        }

        return new ObjectExpression(obj);
    }
}
