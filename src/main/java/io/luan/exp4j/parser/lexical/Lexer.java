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

public class Lexer {

    private String expression = "";

    // start of the token
    private int startPos = 0;

    // One position AFTER the end of the token.
    // Use string.substring(start, end-start).
    private int currentPos = 0;

    // last successful match
    private int lastPos = 0;

    private TokenState currState = TokenStates.Start;
    private TokenState succState;

    public Lexer(String expression) {
        this.expression = expression.trim();
    }

    public String getRemainingString() {
        return this.expression.substring(this.currentPos);
    }

    /**
     * Convinence method to get all tokens for debugging purposes
     * Real parsers only read one token + n lookup aheads to save on memory
     */
    public List<Token> getTokens() {
        List<Token> list = new ArrayList<Token>();
        while (this.hasMoreToken()) {
            list.add(take());
        }
        return list;
    }

    public boolean hasMoreToken() {
        return this.currentPos < this.expression.length();
    }

    /**
     * Peek the next Token but not consume it.
     */
    public Token peek() {
        return null;
    }

    private void reset() {
        this.startPos = 0;
        this.currentPos = 0;
        this.lastPos = 0;
    }

    private void skipWhitespaces() {
        if (currentPos >= expression.length()) {
            return;
        }

        while (Character.isWhitespace(expression.charAt(currentPos))) {
            currentPos++;
        }
    }

    /**
     * Take the next Token and advance to next.
     */
    public Token take() {
        if (!hasMoreToken()) {
            return null;
        }

        // starting state, if in the end still at the starting state, then
        // stream ended, return false
        currState = TokenStates.Start;

        // state where last success was seen. if (success == 0) then an error
        // encountered.
        succState = TokenStates.Error;

        skipWhitespaces();

        startPos = currentPos;
        while (currentPos < expression.length()) {

            char nextChar = expression.charAt(currentPos);
            TokenResult result = currState.tryConsumeNextChar(nextChar);

            TokenState nextState = result.getState();
            if (false == result.isConsumed()) {
                // if the next char cannot be consumed:
                // If throws exception: means the char is unrecognized

                currState = nextState;
                if (currState.isTerminal()) {
                    // When you hit a terminal token, store the last success
                    // token and the location of it.
                    // So when hit error the success state can be returned.
                    succState = currState;
                }
                break;
            }

            this.currState = nextState;
            if (this.currState.isTerminal()) {
                // When you hit a terminal token, store the last success token
                // and the location of it.
                // So when hit error the success state can be returned.
                this.succState = this.currState;
                this.lastPos = this.currentPos;
            }
            this.currentPos++;
        }

        if (this.succState.isError()) {
            throw new LexerException();
        }

        // TODO: find a way to store last read position and revert at the end for Peak.
        this.currentPos = this.lastPos + 1;
        TokenType successType = this.succState.getTerminalTokenType();
        String token = this.expression.substring(this.startPos, this.currentPos);
        return new Token(successType, token);
    }
}
