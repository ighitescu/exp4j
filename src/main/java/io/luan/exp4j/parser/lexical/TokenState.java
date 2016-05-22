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

public class TokenState {

    private List<Predicate<Character>> rules = new ArrayList<>();
    private List<Boolean> rulesConsume = new ArrayList<>();
    private List<TokenState> resultStates = new ArrayList<>();

    private String name;

    private boolean _isTerminal;

    private boolean _isError;

    private boolean _isStart;

    private TokenType terminalTokenType;

    private TokenState(String name) {
        this.name = name;
    }

    public static TokenState CreateError(String name) {
        TokenState state = new TokenState(name);
        state._isError = true;
        return state;
    }

    public static TokenState CreateNonTerminal(String name) {
        TokenState state = new TokenState(name);
        return state;
    }

    public static TokenState CreateStart(String name) {
        TokenState state = new TokenState(name);
        state._isStart = true;
        return state;
    }

    public static TokenState CreateTerminal(String name) {
        TokenState state = new TokenState(name);
        state._isTerminal = true;
        return state;
    }

    public void addTransition(Predicate<Character> canApply, TokenState resultState) {
        addTransition(canApply, resultState, true);
    }

    public void addTransition(Predicate<Character> canApply, TokenState resultState, boolean consume) {
        this.rules.add(canApply);
        this.rulesConsume.add(consume);
        this.resultStates.add(resultState);
    }

    public String getName() {
        return name;
    }

    public TokenType getTerminalTokenType() {
        return terminalTokenType;
    }

    public boolean isError() {
        return _isError;
    }

    public boolean isStart() {
        return _isStart;
    }

    public boolean isTerminal() {
        return _isTerminal;
    }

    public TokenResult tryConsumeNextChar(char c) {
        for (int i = 0; i < rules.size(); i++) {
            Predicate<Character> rule = rules.get(i);
            if (rule.test(c)) {
                TokenState result = this.resultStates.get(i);
                boolean consumed = rulesConsume.get(i);
                return new TokenResult(result, consumed);
            }
        }

        return new TokenResult(this, false);
    }

    public TokenState withTokenType(TokenType tokenType) {
        this.terminalTokenType = tokenType;
        return this;
    }
}
