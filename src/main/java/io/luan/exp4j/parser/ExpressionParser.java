package io.luan.exp4j.parser;

import io.luan.exp4j.Config;
import io.luan.exp4j.Expression;
import io.luan.exp4j.expressions.BooleanExpression;
import io.luan.exp4j.expressions.NumericExpression;
import io.luan.exp4j.expressions.arithmetic.PowerExpression;
import io.luan.exp4j.expressions.arithmetic.ProductExpression;
import io.luan.exp4j.expressions.arithmetic.SumExpression;
import io.luan.exp4j.expressions.comparison.ComparisonExpression;
import io.luan.exp4j.expressions.conditional.ConditionalExpression;
import io.luan.exp4j.expressions.function.FunctionExpression;
import io.luan.exp4j.expressions.logical.LogicalAndExpression;
import io.luan.exp4j.expressions.logical.LogicalNotExpression;
import io.luan.exp4j.expressions.logical.LogicalOrExpression;
import io.luan.exp4j.expressions.symbolic.ConstantExpression;
import io.luan.exp4j.expressions.symbolic.ParameterExpression;
import io.luan.exp4j.expressions.symbolic.VariableExpression;
import io.luan.exp4j.expressions.type.NumberExpression;
import io.luan.exp4j.parser.syntactic.ParserException;
import io.luan.exp4j.parser.syntactic.SyntaxNode;
import io.luan.exp4j.parser.syntactic.SyntaxNodeType;
import io.luan.exp4j.parser.syntactic.SyntaxParser;
import io.luan.exp4j.util.NumberUtil;
import io.luan.exp4j.visitors.EvaluationVisitor;

import java.util.ArrayList;

public class ExpressionParser {
    private SyntaxParser parser;

    public ExpressionParser(String expression) {
        parser = new SyntaxParser(expression);
    }

    /// <summary>
    /// An AddNode is converted to two nodes into two expressions with weight =
    /// 1
    /// </summary>
    public Expression buildAdd(SyntaxNode node) {
        Expression[] oprands = new Expression[]{buildNode(node.getChildNodes().get(0)),
                buildNode(node.getChildNodes().get(1))};
        NumericExpression[] coefs = new NumericExpression[]{NumberExpression.One, NumberExpression.One};
        Expression sumExp = new SumExpression(oprands, coefs);
        return sumExp;
    }

    private Expression buildComparison(SyntaxNode node) {
        Expression left = buildNode(node.getChildNodes().get(0));
        Expression right = buildNode(node.getChildNodes().get(1));
        ComparisonExpression.ComparisonOperator operator;

        switch (node.getType()) {
            case GreaterThan:
                operator = ComparisonExpression.ComparisonOperator.GreaterThan;
                break;
            case GreaterThanOrEqual:
                operator = ComparisonExpression.ComparisonOperator.GreaterThanOrEqual;
                break;
            case LessThan:
                operator = ComparisonExpression.ComparisonOperator.LessThan;
                break;
            case LessThanOrEqual:
                operator = ComparisonExpression.ComparisonOperator.LessThanOrEqual;
                break;
            case Equal:
                operator = ComparisonExpression.ComparisonOperator.Equal;
                break;
            case NotEqual:
                operator = ComparisonExpression.ComparisonOperator.NotEqual;
                break;
            default:
                throw new UnsupportedOperationException();
        }

        return new ComparisonExpression(left, right, operator);
    }

    public Expression buildConstant(SyntaxNode node) {
        return new ConstantExpression(node.getToken().getText());
    }

    public Expression buildDivide(SyntaxNode node) {
        Expression[] oprands = new Expression[]{buildNode(node.getChildNodes().get(0)),
                buildNode(node.getChildNodes().get(1))};
        NumericExpression[] exponents = new NumericExpression[]{NumberExpression.One, NumberExpression.MinusOne};
        Expression prodExp = new ProductExpression(oprands, exponents);
        return prodExp;
    }

    public Expression buildFunction(SyntaxNode node) {
        String name = node.getToken().getText();
        ArrayList<Expression> paramExps = new ArrayList<Expression>();
        for (SyntaxNode childNode : node.getChildNodes()) {
            Expression nodeExp = buildNode(childNode);
            paramExps.add(nodeExp);
        }

        // if (InternalFunctions.IsKnownFunction(name)) {
        // var result = InternalFunctions.GetKnownFunction(name,
        // paramExps.ToArray());
        // if (result == null) {
        // throw new Exception();
        // }
        // return result;
        // }

        // BUG: how to deal with external functions?
        return new FunctionExpression(name, paramExps.toArray(new Expression[0]), null);
    }

    private Expression buildLogicalAnd(SyntaxNode node) {
        Expression leftNode = buildNode(node.getChildNodes().get(0));
        Expression rightNode = buildNode(node.getChildNodes().get(1));
        if (leftNode instanceof BooleanExpression) {
            BooleanExpression left = (BooleanExpression) leftNode;
            if (rightNode instanceof BooleanExpression) {
                BooleanExpression right = (BooleanExpression) rightNode;

                BooleanExpression[] operands = new BooleanExpression[]{left, right};
                return new LogicalAndExpression(operands);
            }
        }
        throw new ParserException("All operands must be subtype of BooleanExpression");
    }

    private Expression buildLogicalNot(SyntaxNode node) {
        Expression expression = buildNode(node.getChildNodes().get(0));
        if (expression instanceof BooleanExpression) {
            return new LogicalNotExpression((BooleanExpression) expression);
        }
        throw new ExpressionParserException("Cannot negate a non-boolean expression");
    }

    private Expression buildLogicalOr(SyntaxNode node) {
        Expression leftNode = buildNode(node.getChildNodes().get(0));
        Expression rightNode = buildNode(node.getChildNodes().get(1));
        if (leftNode instanceof BooleanExpression) {
            BooleanExpression left = (BooleanExpression) leftNode;
            if (rightNode instanceof BooleanExpression) {
                BooleanExpression right = (BooleanExpression) rightNode;

                BooleanExpression[] operands = new BooleanExpression[]{left, right};
                return new LogicalOrExpression(operands);
            }
        }
        throw new ParserException("All operands must be subtype of BooleanExpression");
    }

    /**
     * A MultiplyNode is converted to Product Expression with two nodes weight = 1
     */
    public Expression buildMultiply(SyntaxNode node) {
        Expression[] operands = new Expression[]{buildNode(node.getChildNodes().get(0)), buildNode(node.getChildNodes().get(1))};
        NumericExpression[] exponents = new NumericExpression[]{NumberExpression.One, NumberExpression.One};
        return new ProductExpression(operands, exponents);
    }

    public Expression buildNegation(SyntaxNode node) {
        Expression[] operands = new Expression[]{buildNode(node.getChildNodes().get(0))};
        NumericExpression[] coefs = new NumericExpression[]{NumberExpression.MinusOne};
        return new SumExpression(operands, coefs);
    }

    public Expression buildNode(SyntaxNode node) {
        switch (node.getType()) {
            case BinaryAdd:
                return buildAdd(node);
            case BinarySubtract:
                return buildSubtract(node);
            case BinaryMultiply:
                return buildMultiply(node);
            case BinaryDivide:
                return buildDivide(node);
            case BinaryPower:
                return buildPower(node);
            case UnaryNegative:
                return buildNegation(node);
            case Variable:
                return buildVariable(node);
            case Parameter:
                return buildParameter(node);
            case Constant:
                return buildConstant(node);
            case Number:
                return buildNumber(node);
            case Function:
                return buildFunction(node);
            case GreaterThan:
            case GreaterThanOrEqual:
            case LessThan:
            case LessThanOrEqual:
            case Equal:
            case NotEqual:
                return buildComparison(node);
            case LogicalNot:
                return buildLogicalNot(node);
            case LogicalAnd:
                return buildLogicalAnd(node);
            case LogicalOr:
                return buildLogicalOr(node);
            case TernaryQuestion:
                return buildTernary(node);
            case TernaryColon:
                throw new ParserException("Ternary Colon must go after a Ternary QuestionMark");
        }
        throw new ParserException("Cannot parse node type: " + node.getType());
    }

    public Expression buildNumber(SyntaxNode node) {
        String text = node.getToken().getText();
        Number number = NumberUtil.parse(text);
        return new NumberExpression(number);
    }

    public Expression buildParameter(SyntaxNode node) {
        return new ParameterExpression(node.getToken().getText(), "", "");
    }

    public Expression buildPower(SyntaxNode node) {
        Expression leftNode = buildNode(node.getChildNodes().get(0));
        Expression rightNode = buildNode(node.getChildNodes().get(1));

        EvaluationVisitor visitor = new EvaluationVisitor();

        // Case 1 - If the right node can be evaluated (without inputs) as a numeric expression then this result in a ProductExprssion
        Expression eval = rightNode.accept(visitor);

        if (eval instanceof NumericExpression) {
            NumericExpression evaluatedRight = (NumericExpression) eval;
            Expression[] oprands = new Expression[]{leftNode};
            NumericExpression[] exponents = new NumericExpression[]{evaluatedRight};
            Expression prodExp = new ProductExpression(oprands, exponents);
            return prodExp;
        }

        return new PowerExpression(leftNode, rightNode);
    }

    /**
     * An SubtractNode is converted to two nodes with weight = 1 and -1
     */
    public Expression buildSubtract(SyntaxNode node) {
        Expression[] operands = new Expression[]{buildNode(node.getChildNodes().get(0)), buildNode(node.getChildNodes().get(1))};
        NumericExpression[] coefs = new NumericExpression[]{NumberExpression.One, NumberExpression.MinusOne};
        return new SumExpression(operands, coefs);
    }

    public Expression buildTernary(SyntaxNode node) {
        Expression cond = buildNode(node.getChildNodes().get(0));
        if (!(cond instanceof BooleanExpression)) {
            throw new ParserException("conditional expression must be subtype of BooleanExpression");
        }

        SyntaxNode colonNode = node.getChildNodes().get(1);
        if (colonNode.getType() != SyntaxNodeType.TernaryColon) {
            throw new ParserException("Ternary colon is required to follow a ternary question");
        }

        Expression ifTrue = buildNode(colonNode.getChildNodes().get(0));
        Expression ifFalse = buildNode(colonNode.getChildNodes().get(1));

        return new ConditionalExpression((BooleanExpression) cond, ifTrue, ifFalse);
    }

    public Expression buildVariable(SyntaxNode node) {
        return new VariableExpression(node.getToken().getText());
    }

    public Expression parse() {
        SyntaxNode syntaxRoot = parser.parse();
        if (Config.DEBUG) {
            System.out.println(syntaxRoot);
        }
        return buildNode(syntaxRoot).simplify();
    }
}
