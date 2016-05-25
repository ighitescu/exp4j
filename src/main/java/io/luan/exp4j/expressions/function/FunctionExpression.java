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

package io.luan.exp4j.expressions.function;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.base.BaseExpression;

import java.util.function.Function;

public class FunctionExpression extends BaseExpression {

    private String name;
    private Expression[] funcParams;
    private Function<NumericExpression[], NumericExpression> func;

    public FunctionExpression(String name, Expression[] parameters,
                              Function<NumericExpression[], NumericExpression> func) {
        this.name = name;
        this.funcParams = parameters;
        this.func = func;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitFunction(this);
    }

    public boolean equals(Expression other) {
        if (other instanceof FunctionExpression) {
            FunctionExpression otherExp = (FunctionExpression) other;

            if (!name.equals(otherExp.name)) {
                return false;
            }

            if (funcParams.length != otherExp.funcParams.length) {
                return false;
            }

            // BUG: For now assume perfectly ordered. For later use HashCode
            // ordering
            for (int i = 0; i < funcParams.length; i++) {
                if (!funcParams[i].equals(otherExp.funcParams[i])) {
                    return false;
                }
            }

            return true;
        }
        return false;
    }

    public Function<NumericExpression[], NumericExpression> getFunc() {
        return func;
    }

    public Expression[] getFuncParams() {
        return funcParams;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return 0;
    }

    public ExpressionType getType() {
        return ExpressionType.Function;
    }
}
