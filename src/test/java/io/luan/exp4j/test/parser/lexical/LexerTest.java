package io.luan.exp4j.test.parser.lexical;

import io.luan.exp4j.parser.lexical.Lexer;
import io.luan.exp4j.parser.lexical.LexerException;
import io.luan.exp4j.parser.lexical.Token;
import io.luan.exp4j.parser.lexical.TokenType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class LexerTest {

    @Test
    public void testAdd() {
        List<Token> list = toList("123 + abc");

        assertSize(list, 3);
        assertToken(list, 0, TokenType.Integer, "123");
        assertToken(list, 1, TokenType.Plus, "+");
        assertToken(list, 2, TokenType.Variable, "abc");
    }

    @Test(expected = LexerException.class)
    public void testBadNumberInput1() {
        try {
            Lexer lexer = new Lexer("123abc");
            lexer.take();
        } catch (LexerException e) {
            Assert.assertEquals(3, e.getPosition());
            throw e;
        }
    }

    @Test(expected = LexerException.class)
    public void testBadNumberInput2() {
        try {
            Lexer lexer = new Lexer("123.");
            lexer.take();
        } catch (LexerException e) {
            Assert.assertEquals(4, e.getPosition());
            throw e;
        }
    }


    @Test(expected = LexerException.class)
    public void testBadNumberInput4() {
        try {
            Lexer lexer = new Lexer("2.a");
            lexer.take();
        } catch (LexerException e) {
            Assert.assertEquals(2, e.getPosition());
            throw e;
        }
    }

    @Test
    public void testFunc() {
        List<Token> list = toList("a(b,c)");

        assertSize(list, 6);
        assertToken(list, 0, TokenType.Function, "a");
        assertToken(list, 1, TokenType.LeftParen, "(");
        assertToken(list, 2, TokenType.Variable, "b");
        assertToken(list, 3, TokenType.Comma, ",");
        assertToken(list, 4, TokenType.Variable, "c");
        assertToken(list, 5, TokenType.RightParen, ")");
    }

    @Test(expected = LexerException.class)
    public void testBadNumberInput5() {
        try {
            Lexer lexer = new Lexer("2.1a");
            lexer.take();
        } catch (LexerException e) {
            Assert.assertEquals(3, e.getPosition());
            throw e;
        }
    }

    @Test
    public void testDecimal() {
        List<Token> list = toList("123.4");

        assertSize(list, 1);
        assertToken(list, 0, TokenType.Decimal, "123.4");
    }

    @Test
    public void testDecimalDot() {
        List<Token> list = toList(".5");
        assertSize(list, 1);
        assertToken(list, 0, TokenType.Decimal, ".5");
    }

    @Test
    public void testEmpty1() {
        Lexer lexer = new Lexer("");
        Token take = lexer.take();
        Assert.assertNull(take);
    }

    @Test
    public void testEmpty2() {
        Lexer lexer = new Lexer("  \t\r  ");
        Token take = lexer.take();
        Assert.assertNull(take);
    }

    @Test
    public void testInteger() {
        List<Token> list = toList("123");

        assertSize(list, 1);
        assertToken(list, 0, TokenType.Integer, "123");
    }

    @Test
    public void testList() {
        List<Token> list = toList("123 234 345");

        assertSize(list, 3);
        assertToken(list, 0, TokenType.Integer, "123");
        assertToken(list, 1, TokenType.Integer, "234");
        assertToken(list, 2, TokenType.Integer, "345");
    }

    @Test
    public void testNoMore() {
        Lexer lexer = new Lexer("123");
        lexer.take();

        Token peek = lexer.peek();
        Assert.assertNull(peek);

        Token take = lexer.take();
        Assert.assertNull(take);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull() {
        Lexer lexer = new Lexer(null);
        lexer.take();
    }

    @Test
    public void testOperators() {
        List<Token> list = toList("+ - * /");

        assertSize(list, 4);
        assertToken(list, 0, TokenType.Plus, "+");
        assertToken(list, 1, TokenType.Minus, "-");
        assertToken(list, 2, TokenType.Asterisk, "*");
        assertToken(list, 3, TokenType.Slash, "/");
    }

    @Test
    public void testMember() {
        List<Token> list = toList("a b.c d.e.f.g");

        assertSize(list, 7);
        assertToken(list, 0, TokenType.Variable, "a");
        assertToken(list, 1, TokenType.Variable, "b");
        assertToken(list, 2, TokenType.Member, ".c");
        assertToken(list, 3, TokenType.Variable, "d");
        assertToken(list, 4, TokenType.Member, ".e");
        assertToken(list, 5, TokenType.Member, ".f");
        assertToken(list, 6, TokenType.Member, ".g");
    }

    @Test
    public void testMethod() {
        List<Token> list = toList("a.b().c(d,e)");

        assertSize(list, 10);
        assertToken(list, 0, TokenType.Variable, "a");
        assertToken(list, 1, TokenType.Method, ".b");
        assertToken(list, 2, TokenType.LeftParen, "(");
        assertToken(list, 3, TokenType.RightParen, ")");
        assertToken(list, 4, TokenType.Method, ".c");
        assertToken(list, 5, TokenType.LeftParen, "(");
        assertToken(list, 6, TokenType.Variable, "d");
        assertToken(list, 7, TokenType.Comma, ",");
        assertToken(list, 8, TokenType.Variable, "e");
        assertToken(list, 9, TokenType.RightParen, ")");
    }



    @Test
    public void testPeekTake() {
        Lexer lexer = new Lexer("123 abc");

        Token peek = lexer.peek();
        assertToken(peek, TokenType.Integer, "123");

        Token take = lexer.take();
        assertToken(take, TokenType.Integer, "123");

        Assert.assertSame(peek, take);
    }

    @Test
    public void testTakePeek() {
        Lexer lexer = new Lexer("123 abc");

        Token take = lexer.take();
        assertToken(take, TokenType.Integer, "123");

        Token peek = lexer.peek();
        assertToken(peek, TokenType.Variable, "abc");
    }

    @Test
    public void testTokenToString() {
        // Only for test coverage purposes

        Lexer lexer = new Lexer("123");
        Token take = lexer.take();

        Token token = new Token(TokenType.Integer, "123");
        Assert.assertEquals(token.toString(), take.toString());
    }

    @Test
    public void testTokenTypes() {
        // for coverage only
        Assert.assertSame(true, TokenType.Integer.isOperand());
        Assert.assertSame(true, TokenType.Plus.isOperator());
        Assert.assertSame(true, TokenType.UnaryNegative.isUnaryOperator());
    }

    @Test
    public void testVariable() {
        List<Token> list = toList("abc123");

        assertSize(list, 1);
        assertToken(list, 0, TokenType.Variable, "abc123");
    }

    @Test
    public void testWhitespaces() {
        List<Token> list = toList("  \r   123 \t  \n\r\t  +  \r\t\n   abc  \n  ");

        assertSize(list, 3);
        assertToken(list, 0, TokenType.Integer, "123");
        assertToken(list, 1, TokenType.Plus, "+");
        assertToken(list, 2, TokenType.Variable, "abc");
    }

    private void assertSize(List<Token> list, int expectedSize) {
        Assert.assertEquals(expectedSize, list.size());
    }

    private void assertToken(Token token, TokenType expectedType, String expectedText) {
        Assert.assertEquals(expectedType, token.getType());
        Assert.assertEquals(expectedText, token.getText());
    }

    private void assertToken(List<Token> list, int pos, TokenType expectedType, String expectedText) {
        assertToken(list.get(pos), expectedType, expectedText);
    }

    private List<Token> toList(String exp) {
        Lexer lexer = new Lexer(exp);
        return lexer.getTokens();
    }
}
