package expression;

import expression.number.Number;

import java.util.Objects;

public abstract class AbstractUnaryOperator<T extends Number<T>> implements CommonExpression<T> {
    private final CommonExpression<T> operand;

    public AbstractUnaryOperator(CommonExpression<T> operand) {
        this.operand = operand;
    }

    protected CommonExpression<T> getOperand() {
        return operand;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return calc(operand.evaluate(x, y, z));
    }

    @Override
    public int priority() {
        return -1;
    }

    @Override
    public boolean isOrdered() {
        return false;
    }

    protected abstract String operatorToString();

    protected abstract T calc(T value);

    @Override
    public String toString() {
        return operatorToString() + " " + operand.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractUnaryOperator<?> that = (AbstractUnaryOperator<?>) o;
        return Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand, operatorToString());
    }
}
