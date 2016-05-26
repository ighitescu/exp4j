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

package io.luan.exp4j.expressions.value;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.util.NumberUtil;

/**
 * Contains a Number
 * <p>
 * - Valid for Integer, Long, BigDecimal, BigInteger
 */
public class NumberExpression extends ValueExpression implements NumericExpression {

    public static final NumberExpression One = new NumberExpression(1);
    public static final NumberExpression MinusOne = new NumberExpression(-1);
    public static final NumberExpression Zero = new NumberExpression(0);
    public static final NumberExpression Ten = new NumberExpression(10);

    public NumberExpression(Number number) {
        super(number);
    }

    @Override
    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitNumber(this);
    }

    @Override
    public NumericExpression add(NumericExpression other) {
        if (other instanceof NumberExpression) {
            NumberExpression otherExp = (NumberExpression) other;
            Number result = NumberUtil.add(getNumber(), otherExp.getNumber());
            return new NumberExpression(result);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public int compareTo(NumericExpression other) {
        if (other instanceof NumberExpression) {
            NumberExpression otherExp = (NumberExpression) other;
            return NumberUtil.compare(getNumber(), otherExp.getNumber());
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public NumericExpression divide(NumericExpression other) {
        if (other instanceof NumberExpression) {
            NumberExpression otherExp = (NumberExpression) other;
            Number result = NumberUtil.divide(getNumber(), otherExp.getNumber());
            return new NumberExpression(result);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equate(NumericExpression other) {
        if (other instanceof NumberExpression) {
            NumberExpression otherExp = (NumberExpression) other;
            return NumberUtil.equate(getNumber(), otherExp.getNumber());
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public NumericExpression multiply(NumericExpression other) {
        if (other instanceof NumberExpression) {
            NumberExpression otherExp = (NumberExpression) other;
            Number result = NumberUtil.multiply(getNumber(),otherExp.getNumber());
            return new NumberExpression(result);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public NumericExpression negate() {
        Number result = NumberUtil.negate(getNumber());
        return new NumberExpression(result);
    }

    @Override
    public NumericExpression power(NumericExpression other) {
        if (other instanceof NumberExpression) {
            NumberExpression otherExp = (NumberExpression) other;
            Number result = NumberUtil.power(getNumber(), otherExp.getNumber());
            return new NumberExpression(result);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public NumericExpression subtract(NumericExpression other) {
        if (other instanceof NumberExpression) {
            NumberExpression otherExp = (NumberExpression) other;
            Number result = NumberUtil.subtract(getNumber(), otherExp.getNumber());
            return new NumberExpression(result);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Expression other) {
        if (other != null && other instanceof NumberExpression) {
            NumberExpression otherExp = (NumberExpression) other;
            return NumberUtil.equate(getNumber(), otherExp.getNumber());
        }
        return false;
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.Number;
    }

    public Number getNumber() {
        return (Number)getObject();
    }
}
