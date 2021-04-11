package expression;

import expression.number.Number;

public class Square<T extends Number<T>> extends AbstractUnaryOperator<T> {
    public Square(CommonExpression<T> operand) {
        super(operand);
    }

    @Override
    protected String operatorToString() {
        return "square";
    }

    @Override
    protected T calc(T value) {
        return value.mul(value);
    }
}
