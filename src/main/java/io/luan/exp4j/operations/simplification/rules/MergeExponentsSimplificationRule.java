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

import java.util.ArrayList;

/// <summary>
/// This merges the exponents of two or more oprands that are the same.
/// Applies to Product Node
/// </summary>
public class MergeExponentsSimplificationRule implements SimplificationRule {

    /// <summary>
    /// Applies ONLY if there can be two operands of the same value
    /// Algorithm:
    ///   For each oprand, check if it already exists in the new array, if not, add, otherwise, update the coefs
    /// </summary>
    public Expression apply(Expression original) {
        boolean success = false;
        ProductExpression prodExp = (ProductExpression) original;
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newExponents = new ArrayList<NumericExpression>();

        for (int i = 0; i < prodExp.getOperands().length; i++) {
            boolean exists = false;
            for (int j = 0; j < newOprands.size(); j++) {
                if (newOprands.get(j).equals(prodExp.getOperands()[i])) {
                    // Already exists, just update the coef
                    newExponents.set(j, newExponents.get(j).add(prodExp.getExponents()[i]));
                    exists = true;
                    success = true;
                }
            }

            if (!exists) {
                newOprands.add(prodExp.getOperands()[i]);
                newExponents.add(prodExp.getExponents()[i]);
            }
        }

        if (success) {
            ProductExpression simplified = new ProductExpression(newOprands.toArray(new Expression[0]),
                    newExponents.toArray(new NumericExpression[0]));
            return simplified;
        }
        return original;
    }

    public boolean canApply(Expression original) {
        // Can be applied if there is more than one oprand.
        if (original.getType() == ExpressionType.Product) {
            ProductExpression prodExp = (ProductExpression) original;
            return prodExp.getOperands().length > 1;
        }
        return false;
    }
}
