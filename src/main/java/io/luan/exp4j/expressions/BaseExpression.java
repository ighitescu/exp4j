package io.luan.exp4j.expressions;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.symbolic.ConstantExpression;
import io.luan.exp4j.expressions.symbolic.ParameterExpression;
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import io.luan.exp4j.visitors.EvaluationVisitor;
import io.luan.exp4j.visitors.MathInfixPrintVisitor;
import io.luan.exp4j.visitors.SimplificationVisitor;
import io.luan.exp4j.visitors.SubstitutionVisitor;

import java.util.Map;
import java.util.Set;

public abstract class BaseExpression implements Expression {
    private Set<ParameterExpression> parameters;
    private Set<VariableExpression> variables;
    private Set<ConstantExpression> constants;

    @Override
    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitExpression(this);
    }

    @Override
    public Expression evaluate(Map<String, Object> values) {
        ExpressionVisitor evalVisitor = new EvaluationVisitor(values);
        return accept(evalVisitor).simplify();
    }

    @Override
    public boolean isNumeric() {
        return getType() == ExpressionType.Number || getType() == ExpressionType.Rational
                || getType() == ExpressionType.Complex;
    }

    /**
     * A Simple operand expression is one that do not need to be wrapped in bracket.
     * Examples are Numbers, variables, parameters and function calls.
     */
    @Override
    public boolean isSimpleOperand() {
        return isNumeric() || isSymbolic() || getType() == ExpressionType.Function
                || getType() == ExpressionType.LogicalNot;
    }

    @Override
    public boolean isSymbolic() {
        return getType() == ExpressionType.Variable || getType() == ExpressionType.Parameter
                || getType() == ExpressionType.Constant;
    }


    @Override
    public Expression simplify() {
        ExpressionVisitor simpVisitor = new SimplificationVisitor();
        return accept(simpVisitor);
    }

    public Expression substitute(String variable, Expression substitute) {
        return substitute(new VariableExpression(variable), substitute);
    }

    public Expression substitute(Expression subExp, Expression substitute) {
        ExpressionVisitor visitor = new SubstitutionVisitor(subExp, substitute);
        return accept(visitor).simplify();
    }

    @Override
    public String toString() {
        MathInfixPrintVisitor prettyVisitor = new MathInfixPrintVisitor();
        accept(prettyVisitor);
        return prettyVisitor.getText();
    }


}
