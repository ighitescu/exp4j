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

package io.luan.exp4j.test.parser.syntactic;

import io.luan.exp4j.parser.syntactic.SyntaxNode;
import io.luan.exp4j.parser.syntactic.SyntaxNodeType;
import io.luan.exp4j.parser.syntactic.SyntaxParser;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testAdd() {
        SyntaxParser parser = new SyntaxParser("a + b");
        SyntaxNode root = parser.parse();
        Assert.assertEquals(SyntaxNodeType.BinaryAdd, root.getType());
        Assert.assertEquals(2, root.getChildNodes().size());
    }

    @Test
    public void testComplex() {
        SyntaxParser parser = new SyntaxParser("a.b.c() + d(e,f) * g.h");
        SyntaxNode root = parser.parse();
        System.out.println(root);
        assertNode(root, SyntaxNodeType.BinaryAdd, 2);
        assertNode(root.get(0), SyntaxNodeType.Dot, 2);
        assertNode(root.get(0).get(0), SyntaxNodeType.Dot, 2);
        assertNode(root.get(0).get(0).get(0), SyntaxNodeType.Variable, 0);
        assertNode(root.get(0).get(0).get(1), SyntaxNodeType.Variable, 0);
        assertNode(root.get(0).get(1), SyntaxNodeType.Function, 0);

        assertNode(root.get(1), SyntaxNodeType.BinaryMultiply, 2);
        assertNode(root.get(1).get(0), SyntaxNodeType.Function, 2);
        assertNode(root.get(1).get(1), SyntaxNodeType.Dot, 2);
    }

    @Test
    public void testComplexFuncParam() {
        SyntaxParser parser = new SyntaxParser("a(b+c,d)");
        SyntaxNode root = parser.parse();
        System.out.println(root);

        assertNode(root, SyntaxNodeType.Function, 2);
        assertNode(root.get(0), SyntaxNodeType.BinaryAdd, 2);
        assertNode(root.get(0).get(0), SyntaxNodeType.Variable, 0);
        assertNode(root.get(0).get(1), SyntaxNodeType.Variable, 0);
        assertNode(root.get(1), SyntaxNodeType.Variable, 0);

    }

    @Test
    public void testFunc() {
        SyntaxParser parser = new SyntaxParser("a(b,c)");
        SyntaxNode root = parser.parse();
        System.out.println(root);
        Assert.assertEquals(SyntaxNodeType.Function, root.getType());
        Assert.assertEquals(2, root.getChildNodes().size());
    }

    @Test
    public void testMember() {
        SyntaxParser parser = new SyntaxParser("a.b.c");
        SyntaxNode root = parser.parse();
        System.out.println(root);
        Assert.assertEquals(SyntaxNodeType.Dot, root.getType());
        Assert.assertEquals(2, root.getChildNodes().size());

        SyntaxNode left = root.getChildNodes().get(0);
        SyntaxNode right = root.getChildNodes().get(1);
        Assert.assertEquals(SyntaxNodeType.Dot, left.getType());
        Assert.assertEquals(2, left.getChildNodes().size());

        Assert.assertEquals(SyntaxNodeType.Variable, right.getType());
    }

    @Test
    public void testMethod() {
        SyntaxParser parser = new SyntaxParser("a.b(c)");
        SyntaxNode root = parser.parse();
        System.out.println(root);
        Assert.assertEquals(SyntaxNodeType.Dot, root.getType());
        Assert.assertEquals(2, root.getChildNodes().size());

        SyntaxNode left = root.getChildNodes().get(0);
        SyntaxNode right = root.getChildNodes().get(1);
        Assert.assertEquals(SyntaxNodeType.Variable, left.getType());

        Assert.assertEquals(SyntaxNodeType.Function, right.getType());
        Assert.assertEquals(1, right.getChildNodes().size());
    }

    @Test
    public void testNestFunc() {
        SyntaxParser parser = new SyntaxParser("a(b(c))");
        SyntaxNode root = parser.parse();
        System.out.println(root);

        assertNode(root, SyntaxNodeType.Function, 1);
        assertNode(root.get(0), SyntaxNodeType.Function, 1);
        assertNode(root.get(0).get(0), SyntaxNodeType.Variable, 0);
    }

    @Test
    public void testNestFuncComplex() {
        SyntaxParser parser = new SyntaxParser("1 + a(b + c(d,e), f)");
        SyntaxNode root = parser.parse();
        System.out.println(root);

        assertNode(root, SyntaxNodeType.BinaryAdd, 2);
        assertNode(root.get(0), SyntaxNodeType.Number, 0);
        assertNode(root.get(1), SyntaxNodeType.Function, 2);
        assertNode(root.get(1).get(0), SyntaxNodeType.BinaryAdd, 2);
        assertNode(root.get(1).get(0).get(0), SyntaxNodeType.Variable, 0);
        assertNode(root.get(1).get(0).get(1), SyntaxNodeType.Function, 2);
        assertNode(root.get(1).get(0).get(1).get(0), SyntaxNodeType.Variable, 0);
        assertNode(root.get(1).get(0).get(1).get(1), SyntaxNodeType.Variable, 0);
        assertNode(root.get(1).get(1), SyntaxNodeType.Variable, 0);
    }

    private void assertNode(SyntaxNode node, SyntaxNodeType type, int childSize) {
        Assert.assertEquals(type, node.getType());
        Assert.assertEquals(childSize, node.getChildNodes().size());
    }
}
