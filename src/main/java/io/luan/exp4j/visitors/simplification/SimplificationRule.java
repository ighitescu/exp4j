package io.luan.exp4j.visitors.simplification;

import io.luan.exp4j.Expression;

public interface SimplificationRule {
    /// <summary>
    /// Try apply the rule to the expression.
    /// If the rule causes a change in the expression, returns the resulting
    /// expression
    /// Otherwise, return the original
    /// The success is tested by object.Equals
    /// </summary>
    Expression apply(Expression original);

    /// <summary>
    /// Return true if the Expression meets all the criteria of the rule and
    /// thus the rule can be applied.
    /// Does NOT indicate if the expression NEED to be applied
    /// </summary>
    boolean canApply(Expression original);

}
