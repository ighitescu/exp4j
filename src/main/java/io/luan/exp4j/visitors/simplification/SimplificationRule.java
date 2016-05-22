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
