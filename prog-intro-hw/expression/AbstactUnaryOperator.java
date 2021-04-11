package expression;

import java.util.Objects;

public abstract class AbstactUnaryOperator implements CommonExpression {
    private final CommonExpression operand;

    public AbstactUnaryOperator(CommonExpression operand) {
        this.operand = operand;
    }

    protected CommonExpression getOperand() {
        return operand;
    }

    @Override
    public int evaluate(int x) {
        return calc(operand.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
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

    protected abstract int calc(int value);

    @Override
    public String toString() {
        return operatorToString() + " " + operand.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstactUnaryOperator that = (AbstactUnaryOperator) o;
        return Objects.equals(operand, that.operand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operand, operatorToString());
    }
}
