package expression;

import expression.number.Number;

public class Count<T extends Number<T>> extends AbstractUnaryOperator<T> {
    public Count(CommonExpression<T> operand) {
        super(operand);
    }

    @Override
    protected String operatorToString() {
        return "count";
    }

    @Override
    protected T calc(T value) {
        return value.bitsCount();
    }
}
