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

package io.luan.exp4j.expressions.value;

import io.luan.exp4j.Expression;
import io.luan.exp4j.ExpressionType;
import io.luan.exp4j.ExpressionVisitor;
import io.luan.exp4j.expressions.base.BaseExpression;

/**
 * Base of all value-type expressions:
 * - NumberExpression
 * - BooleanValueExpression
 * - ObjectExpression
 * - StringExpression // TODO: add this?
 */
public abstract class ValueExpression extends BaseExpression {

    private Object obj;

    public ValueExpression(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        this.obj = obj;
    }

    public Object getObject() {
        return obj;
    }

    @Override
    public int hashCode() {
        return obj != null ? obj.hashCode() : 0;
    }

}
