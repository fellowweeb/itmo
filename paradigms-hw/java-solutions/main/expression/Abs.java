package expression;

import expression.number.Number;

public class Abs<T extends Number<T>> extends AbstractUnaryOperator<T> {
    public Abs(CommonExpression<T> operand) {
        super(operand);
    }

    @Override
    protected T calc(T value) {
        if (value.compareTo(value.getZero()) < 0)
            return value.negate();
        return value;
    }

    @Override
    protected String operatorToString() {
        return "abs";
    }
}
