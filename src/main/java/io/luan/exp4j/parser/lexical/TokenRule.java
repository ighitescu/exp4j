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

package io.luan.exp4j.parser.lexical;

import java.util.function.Predicate;

class TokenRule {
    private Predicate<Character> predicate;
    private boolean consume;
    private TokenState resultState;
    private boolean fatal;

    TokenRule(Predicate<Character> predicate, TokenState resultState, boolean consume, boolean fatal) {
        this.predicate = predicate;
        this.resultState = resultState;
        this.consume = consume;
        this.fatal = fatal;
    }

    TokenState getResultState() {
        return resultState;
    }

    boolean isConsumed() {
        return consume;
    }

    boolean isFatal() {
        return fatal;
    }

    boolean test(char c) {
        return predicate.test(c);
    }
}