package io.luan.exp4j.parser.lexical;

public class TokenResult {
    private TokenState state;
    private boolean consumed;

    public TokenResult(TokenState state, boolean consumed) {
        this.state = state;
        this.consumed = consumed;
    }

    public TokenState getState() {
        return state;
    }

    public boolean isConsumed() {
        return consumed;
    }
}
