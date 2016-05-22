package io.luan.exp4j;

public enum ExpressionType {
    None(0),

    Rational(2),
    Complex(4),
    Number(9),

    Variable(11),
    Parameter(12),
    Constant(13),

    Function(21),

    Sum(101),
    Product(102),
    Power(103),

    Conditional(201),

    BooleanValue(1001),
    LogicalAnd(2001),
    LogicalOr(2002),
    LogicalNot(2003),

    Comparison(3001);

    private int value;

    ExpressionType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
