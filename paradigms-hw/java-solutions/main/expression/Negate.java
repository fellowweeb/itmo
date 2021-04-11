package expression;

import expression.number.Number;

public class Negate<T extends Number<T>> extends AbstractUnaryOperator<T> {
    public Negate(CommonExpression<T> value) {
        super(value);
    }

    @Override
    protected String operatorToString() {
        return "-";
    }

    @Override
    protected T calc(T value) {
        return value.negate();
    }
}
