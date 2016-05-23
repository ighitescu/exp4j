package io.luan.exp4j.test.parser.lexical;

import io.luan.exp4j.parser.lexical.Lexer;
import io.luan.exp4j.parser.lexical.Token;
import io.luan.exp4j.parser.lexical.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by luangm on 16/5/23.
 */
public class LexerTest {

    @Test
    public void LexerTestInteger() {
        List<Token> list = toList("123");

        assertSize(list, 1);
        assertToken(list, 0, TokenType.Integer, "123");
    }

    @Test
    public void LexerTestDecimal() {
        List<Token> list = toList("123.4");

        assertSize(list, 1);
        assertToken(list, 0, TokenType.Decimal, "123.4");
    }

    @Test
    public void LexerTestVariable() {
        List<Token> list = toList("abc123");

        assertSize(list, 1);
        assertToken(list, 0, TokenType.Variable, "abc123");
    }

    @Test
    public void LexerTestAdd() {
        List<Token> list = toList("123 + abc");

        assertSize(list, 3);
        assertToken(list, 0, TokenType.Integer, "123");
        assertToken(list, 1, TokenType.Plus, "+");
        assertToken(list, 2, TokenType.Variable, "abc");
    }

    private List<Token> toList(String exp) {
        Lexer lexer = new Lexer(exp);
        return lexer.getTokens();
    }

    private void assertSize(List<Token> list, int expectedSize) {
        Assert.assertEquals(expectedSize, list.size());
    }

    private void assertToken(List<Token> list, int pos, TokenType expectedType, String expectedText) {
        Assert.assertEquals(expectedType, list.get(pos).getType());
        Assert.assertEquals(expectedText, list.get(pos).getText());
    }
}
