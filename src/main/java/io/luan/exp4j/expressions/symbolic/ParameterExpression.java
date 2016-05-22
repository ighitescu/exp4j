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
