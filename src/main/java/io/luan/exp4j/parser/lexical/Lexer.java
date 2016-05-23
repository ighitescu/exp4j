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

/**
 * Convert a String expression into a list of Tokens.
 * The tokenization process is greedy.
 */
public class Lexer {

    private String expression = "";

    // current cursor position.
    private int currentPos = 0;

    // the END of last successful math
    private int lastPos = 0;

    // the last token. If exists, take consumes it, peek does not
    private Token lastToken;

    public Lexer(String expression) {
        if (expression == null) {
            throw new IllegalArgumentException("expression cannot be null");
        }
        this.expression = expression;
    }

    /**
     * Convenience method to get all tokens for debugging purposes
     * Real parsers only read one token + n lookup ahead to save on memory
     */
    public List<Token> getTokens() {
        List<Token> list = new ArrayList<>();
        Token token = take();
        while (token != null) {
            list.add(token);
            token = take();
        }
        return list;
    }

    /**
     * Check to see if additional token is possible
     */
    public boolean hasMore() {
        if (currentPos >= expression.length()) {
            return false;
        }

        skipWhitespaces();

        return currentPos < expression.length();
    }

    /**
     * Peek the next Token but not consume it.
     */
    public Token peek() {
        if (lastToken == null) {
            lastToken = takeInternal();
        }
        return lastToken;
    }

    /**
     * Take the next Token and advance to next.
     */
    public Token take() {
        Token token;
        // doesn't exist yet. Need to check if no more token
        if (lastToken == null) {
            if (!hasMore()) {
                return null;
            }
            token = takeInternal();
        } else {
            token = lastToken;
        }
        lastToken = takeInternal();
        return token;
    }

    private void skipWhitespaces() {
        int len = expression.length();
        while (currentPos < len && Character.isWhitespace(expression.charAt(currentPos))) {
            currentPos++;
        }
    }

    private Token takeInternal() {
        if (!hasMore()) {
            return null;
        }

        // starting state, if in the end still at the starting state, then stream ended, return false
        TokenState currState = TokenStates.Start;

        // state where last success was seen. if (success == 0) then an error encountered.
        TokenState succState = TokenStates.Error;

        int startPos = currentPos;
        while (currentPos < expression.length()) {
            char nextChar = expression.charAt(currentPos);
            TokenRule result = currState.tryConsume(nextChar);
            if (result.isFatal()) {
                throw new LexerException(currentPos);
            }

            currState = result.getResultState();
            if (!result.isConsumed()) {
                if (currState.isTerminal()) {
                    succState = currState;
                }
                break;
            }

            if (currState.isTerminal()) {
                // When you hit a terminal token, store the last success token and the location of it.
                // So when hit error the success state can be returned.
                succState = currState;
                lastPos = currentPos;
            }
            currentPos++;
        }

        if (succState.isError()) {
            throw new LexerException(currentPos);
        }

        currentPos = lastPos + 1; // Reset currentPos
        TokenType type = succState.getTerminalTokenType();
        String text = expression.substring(startPos, currentPos);
        return new Token(type, text);
    }
}
