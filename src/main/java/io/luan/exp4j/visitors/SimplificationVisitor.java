package io.luan.exp4j.visitors;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.expressions.arithmetic.PowerExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.function.FunctionExpression;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.visitors.simplification.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SimplificationVisitor extends BaseExpressionVisitor {

    private HashMap<ExpressionType, ArrayList<SimplificationRule>> rules;

    public SimplificationVisitor() {
        this.rules = new HashMap<ExpressionType, ArrayList<SimplificationRule>>();
        registerRules();
    }

    private void registerRule(ExpressionType type, SimplificationRule rule) {
        ArrayList<SimplificationRule> list = this.rules.get(type);
        if (list == null) {
            list = new ArrayList<SimplificationRule>();
            this.rules.put(type, list);
        }

        list.add(rule);
    }

    private void registerRules() {
        this.rules.put(ExpressionType.Sum, new ArrayList<SimplificationRule>());
        this.rules.put(ExpressionType.Product, new ArrayList<SimplificationRule>());

        registerRule(ExpressionType.Sum, new SumMergeSimplificationRule());
        registerRule(ExpressionType.Sum, new MergeCoefficientsSimplificationRule());
        registerRule(ExpressionType.Sum, new ZeroCoefficientSimplificationRule());
        registerRule(ExpressionType.Sum, new NumericSumSimplificationRule());
        registerRule(ExpressionType.Sum, new SingleChildOneCoefficientSimplificationRule());

        registerRule(ExpressionType.Product, new ZeroExponentSimplificationRule());
        registerRule(ExpressionType.Product, new ProductMergeSimplificationRule());
        registerRule(ExpressionType.Product, new MergeExponentsSimplificationRule());
        registerRule(ExpressionType.Product, new NumericFactorSimplificationRule());
        registerRule(ExpressionType.Product, new SingleChildOneExponentSimplificationRule());
        registerRule(ExpressionType.Product, new SingleChildSumProductSimplificationRule());
    }

    public Expression visitFunction(FunctionExpression expression) {
        boolean success = false;
        ArrayList<Expression> newParams = new ArrayList<Expression>();
        for (Expression paramExp : expression.getFuncParams()) {
            Expression simplifiedSub = paramExp.accept(this);
            if (!paramExp.equals(simplifiedSub)) {
                success = true;
            }
            newParams.add(simplifiedSub);
        }

        if (success) {
            FunctionExpression newFunc = new FunctionExpression(expression.getName(), newParams.toArray(new Expression[0]), expression.getFunc());
            return newFunc.accept(this);
        }
        return super.visitFunction(expression);
    }

    public Expression visitPower(PowerExpression expression) {
        Expression simplifiedExponent = expression.getExponent().accept(this);
        Expression simplifiedBase = expression.getBase().accept(this);

        // At least one has changed
        if (!expression.getExponent().equals(simplifiedExponent) || !expression.getBase().equals(simplifiedBase)) {
            PowerExpression newPow = new PowerExpression(simplifiedBase, simplifiedExponent);
            return newPow.accept(this);
        }
        return super.visitPower(expression);
    }

    public Expression visitProduct(ProductExpression expression) {
        for (SimplificationRule rule : this.rules.get(ExpressionType.Product)) {
            if (rule.canApply(expression)) {
                Expression simplified = rule.apply(expression);
                if (!simplified.equals(expression)) {
                    return simplified.accept(this);
                }
            }
        }

        // If reach to this point, there is no simplification rule applied.
        // If no rule is applied, then should try to simplify children nodes
        boolean success = false;
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newExponents = new ArrayList<NumericExpression>();
        for (int i = 0; i < expression.getOperands().length; i++) {
            Expression simplifiedSub = expression.getOperands()[i].accept(this);
            if (!expression.getOperands()[i].equals(simplifiedSub)) {
                success = true;
            }
            newOprands.add(simplifiedSub);
            newExponents.add(expression.getExponents()[i]);
        }

        if (success) {
            ProductExpression newProd = new ProductExpression(newOprands.toArray(new Expression[0]), newExponents.toArray(new NumericExpression[0]));
            return newProd.accept(this);
        }
        return super.visitProduct(expression);
    }

    public Expression visitSum(SumExpression expression) {
        for (SimplificationRule rule : this.rules.get(ExpressionType.Sum)) {
            if (rule.canApply(expression)) {
                Expression simplified = rule.apply(expression);
                if (!simplified.equals(expression)) {
                    return simplified.accept(this);
                }
            }
        }

        // If reach to this point, there is no simplification rule applied.
        // If no rule is applied, then should try to simplify children nodes
        boolean success = false;
        ArrayList<Expression> newOprands = new ArrayList<Expression>();
        ArrayList<NumericExpression> newCoefs = new ArrayList<NumericExpression>();
        for (int i = 0; i < expression.getOperands().length; i++) {
            Expression simplifiedSub = expression.getOperands()[i].accept(this);
            if (!expression.getOperands()[i].equals(simplifiedSub)) {
                success = true;
            }
            newOprands.add(simplifiedSub);
            newCoefs.add(expression.getCoefficients()[i]);
        }

        if (success) {
            SumExpression newSum = new SumExpression(newOprands.toArray(new Expression[0]), newCoefs.toArray(new NumericExpression[0]));
            return newSum.accept(this);
        }
        return super.visitSum(expression);
    }
}
