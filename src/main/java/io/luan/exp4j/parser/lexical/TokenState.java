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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class TokenState {

    private List<TokenRule> rules = new ArrayList<>();
    private String name;
    private boolean isTerminal;
    private boolean isError;
    private TokenType terminalTokenType;

    private TokenState(String name) {
        this.name = name;
    }

    static TokenState error(String name) {
        TokenState state = new TokenState(name);
        state.isError = true;
        return state;
    }

    static TokenState nonTerminal(String name) {
        return new TokenState(name);
    }

    static TokenState terminal(String name, TokenType terminalType) {
        TokenState state = new TokenState(name);
        state.isTerminal = true;
        state.terminalTokenType = terminalType;
        return state;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName() + (isTerminal ? " Terminal" : " NonTerminal");
    }

    void addTransition(Predicate<Character> canApply, TokenState resultState) {
        addTransition(canApply, resultState, true, false);
    }

    void addTransition(Predicate<Character> canApply, TokenState resultState, boolean consume) {
        addTransition(canApply, resultState, consume, false);
    }

    void addTransition(Predicate<Character> canApply, TokenState resultState, boolean consume, boolean fatal) {
        TokenRule rule = new TokenRule(canApply, resultState, consume, fatal);
        rules.add(rule);
    }

    void addFatal(Predicate<Character> canApply) {
        addTransition(canApply, null, false, true);
    }

    TokenType getTerminalTokenType() {
        return terminalTokenType;
    }

    boolean isError() {
        return isError;
    }

    boolean isTerminal() {
        return isTerminal;
    }

    TokenRule tryConsume(char c) {
        for (TokenRule rule : rules) {
            if (rule.test(c)) {
                return rule;
            }
        }
        return new TokenRule(null, this, false, false);
    }


}
