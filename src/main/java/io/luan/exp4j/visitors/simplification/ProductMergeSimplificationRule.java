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

package io.luan.exp4j.visitors.simplification;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;

import java.util.ArrayList;

public class ProductMergeSimplificationRule implements SimplificationRule {
    public Expression apply(Expression original) {
        ProductExpression prodExp = (ProductExpression) original;
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newExponents = new ArrayList<NumericExpression>();

        for (int i = 0; i < prodExp.getOperands().length; i++) {
            Expression op = prodExp.getOperands()[i];
            if (op instanceof ProductExpression) {
                ProductExpression subExp = (ProductExpression) op;

                for (int j = 0; j < subExp.getOperands().length; j++) {
                    newOprands.add(subExp.getOperands()[j]);
                    newExponents.add(subExp.getExponents()[j].multiply(prodExp.getExponents()[i]));
                }
            } else {
                newOprands.add(prodExp.getOperands()[i]);
                newExponents.add(prodExp.getExponents()[i]);
            }
        }

        ProductExpression simplified = new ProductExpression(newOprands.toArray(new Expression[0]),
                newExponents.toArray(new NumericExpression[0]));
        return simplified;
    }

    public boolean canApply(Expression original) {
        // A prod merge rule can be applied to a prod expression,
        // AND, at least one operand is a prod expression
        if (original.getType() == ExpressionType.Product) {
            ProductExpression prodExp = (ProductExpression) original;
            for (Expression oprand : prodExp.getOperands()) {
                if (oprand.getType() == ExpressionType.Product) {
                    return true;
                }
            }
        }
        return false;
    }
}
