package io.luan.exp4j.visitors;

import io.luan.exp4j.Expression;
import io.luan.exp4j.expressions.arithmetic.PowerExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.function.FunctionExpression;
import io.luan.exp4j.expressions.symbolic.VariableExpression;

import java.util.HashSet;
import java.util.Set;

public class VariableVisitor extends BaseExpressionVisitor {

    private HashSet<VariableExpression> variables = new HashSet<VariableExpression>();

    public Set<VariableExpression> getVariables() {
        return variables;
    }

    public Expression visitFunction(FunctionExpression expression) {
        for (Expression param : expression.getFuncParams()) {
            param.accept(this);
        }
        return super.visitFunction(expression);
    }

    public Expression visitPower(PowerExpression expression) {
        expression.getBase().accept(this);
        expression.getExponent().accept(this);
        return super.visitPower(expression);
    }

    public Expression visitProduct(ProductExpression expression) {
        for (Expression operand : expression.getOperands()) {
            operand.accept(this);
        }
        return super.visitProduct(expression);
    }

    public Expression visitSum(SumExpression expression) {
        for (Expression operand : expression.getOperands()) {
            operand.accept(this);
        }
        return super.visitSum(expression);
    }

    public Expression visitVariable(VariableExpression expression) {
        variables.add(expression);
        return super.visitVariable(expression);
    }
}
