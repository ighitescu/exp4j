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

package io.luan.exp4j.operations.simplification.rules;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.value.NumberExpression;

import java.util.ArrayList;

public class NumericFactorSimplificationRule implements SimplificationRule {

    public Expression apply(Expression original) {
        ProductExpression prodExp = (ProductExpression) original;
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newExponents = new ArrayList<NumericExpression>();

        NumericExpression sumWeight = NumberExpression.One;

        for (int i = 0; i < prodExp.getOperands().length; i++) {
            if (prodExp.getOperands()[i].isNumeric()) {
                NumericExpression opNum = (NumericExpression) prodExp.getOperands()[i];
                if (prodExp.getExponents()[i].compareTo(NumberExpression.Zero) > 0) {
                    sumWeight = sumWeight.multiply(opNum.power(prodExp.getExponents()[i]));
                } else if (prodExp.getExponents()[i].compareTo(NumberExpression.Zero) < 0) {
                    sumWeight = sumWeight.divide(opNum.power(prodExp.getExponents()[i].negate()));
                }
            } else {
                newOprands.add(prodExp.getOperands()[i]);
                newExponents.add(prodExp.getExponents()[i]);
            }
        }

        if (newOprands.size() > 0) {
            ProductExpression newProdExp = new ProductExpression(newOprands.toArray(new Expression[0]),
                    newExponents.toArray(new NumericExpression[0]));
            if (!sumWeight.equate(NumberExpression.One)) {
                SumExpression simplified = new SumExpression(new Expression[]{newProdExp}, new NumericExpression[]{sumWeight});
                return simplified;
            }
            return newProdExp;
        }
        return sumWeight;
    }

    public boolean canApply(Expression original) {
        if (original.getType() == ExpressionType.Product) {
            ProductExpression prodExp = (ProductExpression) original;
            for (Expression op : prodExp.getOperands()) {
                if (op.isNumeric()) {
                    return true;
                }
            }
        }
        return false;
    }
}
