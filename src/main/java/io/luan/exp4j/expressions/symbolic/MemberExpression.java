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
import io.luan.exp4j.expressions.SymbolicExpression;
import io.luan.exp4j.expressions.base.BaseExpression;

public class MemberExpression extends BaseExpression implements SymbolicExpression {

    private String memberName;
    private SymbolicExpression owner;

    public MemberExpression(SymbolicExpression owner, String memberName) {
        this.owner = owner;
        this.memberName = memberName;
    }

    @Override
    public Expression accept(ExpressionVisitor visitor) {
        return visitor.visitMember(this);
    }

    public String getName() {
        return memberName;
    }

    public SymbolicExpression getOwner() {
        return owner;
    }

    @Override
    public ExpressionType getType() {
        return ExpressionType.Member;
    }
}
