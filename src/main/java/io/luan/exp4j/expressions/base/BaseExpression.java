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

package io.luan.exp4j.expressions.base;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import io.luan.exp4j.operations.evaluation.EvaluationVisitor;
import io.luan.exp4j.operations.print.MathInfixPrintVisitor;
import io.luan.exp4j.operations.simplification.SimplificationVisitor;
import io.luan.exp4j.operations.substitution.SubstitutionVisitor;

import java.util.Map;

public abstract class BaseExpression implements Expression {

    protected BaseExpression() {
        // empty
    }

    @Override
    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitExpression(this);
    }

    @Override
    public boolean equals(Expression other) {
        return this == other;
    }

    @Override
    public Expression evaluate(Map<String, Object> values) {
        ExpressionVisitor evalVisitor = new EvaluationVisitor(values);
        return accept(evalVisitor).simplify();
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public boolean isNumeric() {
        return getType() == ExpressionType.Number
                || getType() == ExpressionType.Rational
                || getType() == ExpressionType.Complex;
    }

    /**
     * A Simple operand expression is one that do not need to be wrapped in bracket.
     * Examples are Numbers, variables, parameters and function calls.
     */
    @Override
    public boolean isSimpleOperand() {
        return isNumeric() || isSymbolic()
                || getType() == ExpressionType.Function
                || getType() == ExpressionType.LogicalNot;
    }

    @Override
    public boolean isSymbolic() {
        return getType() == ExpressionType.Variable
                || getType() == ExpressionType.Member
                || getType() == ExpressionType.Method;
    }

    @Override
    public Expression simplify() {
        ExpressionVisitor simpVisitor = new SimplificationVisitor();
        return accept(simpVisitor);
    }

    @Override
    public Expression substitute(String variable, Expression substitute) {
        return substitute(new VariableExpression(variable), substitute);
    }

    @Override
    public Expression substitute(Expression subExp, Expression substitute) {
        ExpressionVisitor visitor = new SubstitutionVisitor(subExp, substitute);
        return accept(visitor).simplify();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Expression)) {
            return false;
        }
        Expression other = (Expression) obj;
        return equals(other);
    }

    @Override
    public String toString() {
        MathInfixPrintVisitor prettyVisitor = new MathInfixPrintVisitor();
        accept(prettyVisitor);
        return prettyVisitor.getText();
    }
}
