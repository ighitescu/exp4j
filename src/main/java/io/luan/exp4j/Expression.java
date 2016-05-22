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

package io.luan.exp4j;

import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.symbolic.ConstantExpression;
import io.luan.exp4j.expressions.symbolic.ParameterExpression;
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import io.luan.exp4j.expressions.type.NumberExpression;
import io.luan.exp4j.parser.ExpressionParser;
import io.luan.exp4j.visitors.ConstantVisitor;
import io.luan.exp4j.visitors.ParameterVisitor;
import io.luan.exp4j.visitors.VariableVisitor;
import io.luan.exp4j.visitors.algebra.DifferentiationVisitor;

import java.util.Map;
import java.util.Set;

public interface Expression {

    //region Static Methods

    static Expression add(Expression op1, Expression op2) {
        if (op1 == null || op2 == null) {
            throw new IllegalArgumentException("op1 and op2 cannot be null");
        }

        Expression[] operands = new Expression[]{op1, op2};
        return new SumExpression(operands).simplify();
    }

    static Expression constant(String name) {
        return new ConstantExpression(name);
    }

    static Set<ConstantExpression> constants(Expression expression) {
        ConstantVisitor visitor = new ConstantVisitor();
        expression.accept(visitor);
        return visitor.getConstants();
    }

    /**
     * 微分用
     */
    static Expression differentiate(Expression expression, String variable) {
        DifferentiationVisitor visitor = new DifferentiationVisitor(variable);
        return expression.accept(visitor).simplify();
    }

    static Expression divide(Expression op1, Expression op2) {
        if (op1 == null || op2 == null) {
            throw new IllegalArgumentException("op1 and op2 cannot be null");
        }

        Expression[] operands = new Expression[]{op1, op2};
        NumericExpression[] exponents = new NumericExpression[]{NumberExpression.One, NumberExpression.MinusOne};
        return new ProductExpression(operands, exponents).simplify();
    }

    static Expression multiply(Expression op1, Expression op2) {
        if (op1 == null || op2 == null) {
            throw new IllegalArgumentException("op1 and op2 cannot be null");
        }

        Expression[] operands = new Expression[]{op1, op2};
        return new ProductExpression(operands).simplify();
    }

    static Expression negate(Expression expression) {
        Expression[] operands = new Expression[]{expression};
        NumericExpression[] coefs = new NumericExpression[]{NumberExpression.MinusOne};
        Expression sumNode = new SumExpression(operands, coefs);
        return sumNode.simplify();
    }

    static Expression parameter(String name, String formula, String lookup) {
        return new ParameterExpression(lookup, name, formula);
    }

    static Set<ParameterExpression> parameters(Expression expression) {
        ParameterVisitor visitor = new ParameterVisitor();
        expression.accept(visitor);
        return visitor.getParameters();
    }

    static Expression parse(String expression) {
        ExpressionParser expParser = new ExpressionParser(expression);
        return expParser.parse().simplify();
    }

    static Expression power(Expression op1, int power) {
        Expression[] operands = new Expression[]{op1};
        NumericExpression[] coefs = new NumericExpression[]{new NumberExpression(power)};
        return new ProductExpression(operands, coefs).simplify();
    }

    static Expression variable(String name) {
        return new VariableExpression(name);
    }

    static Set<VariableExpression> variables(Expression expression) {
        VariableVisitor visitor = new VariableVisitor();
        expression.accept(visitor);
        return visitor.getVariables();
    }

    //endregion

    /**
     * Visitor pattern
     */
    Expression accept(ExpressionVisitor visitor);

    /**
     * This method should return if the current expression is the same as other.
     * If two expressions are equal they must be syntactically equal and value equal.
     */
    boolean equals(Expression other);

    Expression evaluate(Map<String, Object> values);

    /**
     * Size of the expression tree
     */
    int getSize();

    /**
     * Type
     */
    ExpressionType getType();

    /**
     * Numeric can be casted to NumericExpression
     */
    boolean isNumeric();

    /*
     * A Simple operand expression is one that do not need to be wrapped in bracket.
     * Examples are Numbers, variables, parameters and function calls.
     */
    boolean isSimpleOperand();

    /**
     * Symbolic expressions can be replaced during evaluation
     */
    boolean isSymbolic();

    /**
     * Simplify/Reduce to best structure
     */
    Expression simplify();

    /**
     * Substitute Variable
     */
    Expression substitute(String variable, Expression substitute);

    /**
     * substitute an expression node with another expression node
     */
    Expression substitute(Expression subExp, Expression substitute);
}
