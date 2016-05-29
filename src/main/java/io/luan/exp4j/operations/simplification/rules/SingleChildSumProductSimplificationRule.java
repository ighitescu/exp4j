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

import java.util.ArrayList;

/// <summary>
///  Applies to PROD node
/// 	If one of the child is a Sum Node with only ONE operand.
/// 	Can merge the oprand to the PROD node, and move the weight to another oprand
/// </summary>
public class SingleChildSumProductSimplificationRule implements SimplificationRule {

    public Expression apply(Expression original) {
        ProductExpression prodExp = (ProductExpression) original;
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newExponents = new ArrayList<NumericExpression>();

        for (int i = 0; i < prodExp.getOperands().length; i++) {
            Expression op = prodExp.getOperands()[i];
            if (op instanceof SumExpression) {
                SumExpression sumNode = (SumExpression) op;
                if (sumNode.getOperands().length == 1) {

                    // Merge up the sumNode.Operand
                    newOprands.add(sumNode.getOperands()[0]);
                    newExponents.add(prodExp.getExponents()[i]);

                    // Merge up the coefficient
                    newOprands.add(sumNode.getCoefficients()[0]);
                    newExponents.add(prodExp.getExponents()[i]);
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
        if (original.getType() == ExpressionType.Product) {
            ProductExpression prodExp = (ProductExpression) original;
            for (int i = 0; i < prodExp.getOperands().length; i++) {
                if (prodExp.getOperands()[i].getType() == ExpressionType.Sum) {
                    SumExpression sumNode = (SumExpression) prodExp.getOperands()[i];
                    if (sumNode.getOperands().length == 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
