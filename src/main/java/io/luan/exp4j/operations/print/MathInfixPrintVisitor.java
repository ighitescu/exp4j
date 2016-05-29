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

package io.luan.exp4j.operations.print;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.PowerExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.comparison.ComparisonExpression;
import io.luan.exp4j.expressions.conditional.ConditionalExpression;
import io.luan.exp4j.expressions.function.FunctionExpression;
import io.luan.exp4j.expressions.logical.LogicalAndExpression;
import io.luan.exp4j.expressions.logical.LogicalNotExpression;
import io.luan.exp4j.expressions.symbolic.MemberExpression;
import io.luan.exp4j.expressions.symbolic.MethodExpression;
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import io.luan.exp4j.expressions.value.BooleanValueExpression;
import io.luan.exp4j.expressions.value.NumberExpression;
import io.luan.exp4j.util.NumberFormatter;
import io.luan.exp4j.operations.base.BaseExpressionVisitor;

/// <summary>
/// This is used to print Expression as pretty Math expression.
/// Use MathMLPrintVisitor to print to MathML
/// </summary>
public class MathInfixPrintVisitor extends BaseExpressionVisitor {

    private StringBuilder builder = new StringBuilder();

    public String getText() {
        return builder.toString();
    }

    @Override
    public Expression visitBooleanValue(BooleanValueExpression expression) {
        builder.append(expression.getBooleanValue() ? "true" : "false");
        return super.visitBooleanValue(expression);
    }

    @Override
    public Expression visitComparison(ComparisonExpression expression) {
        visit(expression.getLeftOperand(), !expression.getLeftOperand().isSimpleOperand());
        switch (expression.getOperator()) {
            case GreaterThan:
                builder.append(" > ");
                break;
            case GreaterThanOrEqual:
                builder.append(" >= ");
                break;
            case LessThan:
                builder.append(" < ");
                break;
            case LessThanOrEqual:
                builder.append(" <= ");
                break;
            case Equal:
                builder.append(" == ");
                break;
            case NotEqual:
                builder.append(" != ");
                break;
            default:
                throw new UnsupportedOperationException();
        }
        visit(expression.getRightOperand(), !expression.getRightOperand().isSimpleOperand());
        return super.visitComparison(expression);
    }

    @Override
    public Expression visitConditional(ConditionalExpression expression) {
        visit(expression.getCondition(), !expression.getCondition().isSimpleOperand());
        builder.append(" ? ");
        visit(expression.getTrueExpression(), !expression.getTrueExpression().isSimpleOperand());
        builder.append(" : ");
        visit(expression.getFalseExpression(), !expression.getFalseExpression().isSimpleOperand());
        return super.visitConditional(expression);
    }

    public Expression visitFunction(FunctionExpression expression) {
        builder.append(expression.getName() + "(");
        for (int i = 0; i < expression.getFuncParams().length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            visit(expression.getFuncParams()[i]);
        }
        builder.append(")");

        return super.visitFunction(expression);
    }

    @Override
    public Expression visitLogicalAnd(LogicalAndExpression expression) {
        visit(expression.getOperands()[0], !expression.getOperands()[0].isSimpleOperand());

        for (int i = 1; i < expression.getOperands().length; i++) {
            builder.append(" && ");
            visit(expression.getOperands()[i], !expression.getOperands()[i].isSimpleOperand());
        }

        return super.visitLogicalAnd(expression);
    }

    @Override
    public Expression visitLogicalNot(LogicalNotExpression expression) {
        builder.append("!");
        visit(expression.getOperand(), !expression.getOperand().isSimpleOperand());
        return super.visitLogicalNot(expression);
    }

    @Override
    public Expression visitMember(MemberExpression expression) {
        visit(expression.getOwner(), false);
        builder.append(".");
        builder.append(expression.getName());
        return super.visitMember(expression);
    }

    @Override
    public Expression visitMethod(MethodExpression expression) {
        visit(expression.getOwner(), false);
        builder.append(".");
        builder.append(expression.getName());
        builder.append("(");
        for (int i = 0; i < expression.getParameters().length; i++) {
            if (i > 0) {
                builder.append(",");
            }
            visit(expression.getParameters()[i]);
        }
        builder.append(")");
        return super.visitMethod(expression);
    }

    public Expression visitNumber(NumberExpression expression) {
        Number number = expression.getNumber();
        String formatted = NumberFormatter.format(number);
        builder.append(formatted);
        return super.visitNumber(expression);
    }

    public Expression visitPower(PowerExpression expression) {
        visit(expression.getBase(), !expression.getBase().isSimpleOperand());
        builder.append("^");
        visit(expression.getExponent(), !expression.getExponent().isSimpleOperand());
        return super.visitPower(expression);
    }

    /// <summary>
    /// Single Operand is {x}^{exp}
    /// Multiple Operand is {x}^{e1} * {y}^{e2}
    /// each {e} of negative is divide rather than multiply
    /// </summary>
    public Expression visitProduct(ProductExpression expression) {
        for (int i = 0; i < expression.getOperands().length; i++) {
            // build interconnect operator
            Expression operand = expression.getOperands()[i];
            NumericExpression exp = expression.getExponents()[i];
            if (i > 0 && exp.compareTo(NumberExpression.Zero) > 0) {
                builder.append(" * ");
            }
            if (i > 0 && exp.compareTo(NumberExpression.Zero) < 0) {
                builder.append(" / ");
                exp = (NumericExpression) exp.negate();
            }

            if (exp == NumberExpression.One) {
                visit(operand, operand.getType() == ExpressionType.Sum);
            } else {
                visit(operand, !operand.isSimpleOperand());
                builder.append("^");
                visit(exp);
            }
        }

        return super.visitProduct(expression);
    }

    /// <summary>
    /// The resulting expression depends on the coeffient and operand.
    /// if operand count == 1 AND its coefficient != 1 (Note: single oprand
    /// should never have coefficient = 1)
    /// coef * oprand
    /// if (operand count > 1) : {x} + {x} + {x} (Note: no need to worry about
    /// operator precedence)
    /// - for the first operand: if coef is negative, put a negSign;
    /// </summary>
    public Expression visitSum(SumExpression expression) {

        // First operand:
        if (expression.getCoefficients()[0].equate(NumberExpression.One)) {
            // Do nothing
        } else if (expression.getCoefficients()[0].equate(NumberExpression.MinusOne)) {
            builder.append("-");
        } else {
            visit(expression.getCoefficients()[0]);
            builder.append(" * ");
        }
        visit(expression.getOperands()[0]);


        for (int i = 1; i < expression.getCoefficients().length; i++) {
            if (expression.getCoefficients()[i].equals(NumberExpression.One)) {
                if (expression.getOperands()[i].isNumeric()
                        && ((NumericExpression) expression.getOperands()[i]).compareTo(NumberExpression.Zero) < 0) {
                    builder.append(" - ");
                    visit(((NumericExpression) expression.getOperands()[i]).negate());
                } else {
                    builder.append(" + ");
                    visit(expression.getOperands()[i]);
                }
            } else if (expression.getCoefficients()[i].equate(NumberExpression.MinusOne)) {
                builder.append(" - ");
                visit(expression.getOperands()[i]);
            } else {
                if (expression.getCoefficients()[i].compareTo(NumberExpression.Zero) > 0) {
                    builder.append(" + ");
                    visit(expression.getCoefficients()[i]);
                } else {
                    builder.append(" - ");
                    visit(expression.getCoefficients()[i].negate());
                }
                builder.append(" * ");
                visit(expression.getOperands()[i]);
            }
        }

        return super.visitSum(expression);
    }

    public Expression visitVariable(VariableExpression expression) {
        builder.append(expression.getName());
        return super.visitVariable(expression);
    }

    private void visit(Expression expression) {
        visit(expression, false);
    }

    private void visit(Expression expression, boolean bracketed) {
        if (bracketed) {
            builder.append("(");
        }
        expression.accept(this);
        if (bracketed) {
            builder.append(")");
        }
    }
}
