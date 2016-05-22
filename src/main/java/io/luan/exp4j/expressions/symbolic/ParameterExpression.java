package io.luan.exp4j.expressions.symbolic;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.BaseExpression;

/// <summary>
/// Parameter Expression is converted into type when the equation is put into EquationSystem
/// </summary>
public class ParameterExpression extends BaseExpression {
    private String name;
    private String method;
    private String formula;

    public ParameterExpression(String propertyMethod, String paramName, String formula) {
        this.name = paramName;
        this.method = propertyMethod;
        this.formula = formula;
    }

    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitParameter(this);
    }

    /// <summary>
    /// Two variable expressions are equal if they are both variable expressions
    /// AND the variable names are the same.
    /// </summary>
    public boolean equals(Expression other) {
        if (other instanceof ParameterExpression) {
            ParameterExpression otherExp = (ParameterExpression) other;
            return name.equals(otherExp.name);
        }
        return false;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return 0;
    }

    public ExpressionType getType() {
        return ExpressionType.Parameter;
    }

}
