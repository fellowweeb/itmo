package expression;

import expression.number.Number;

import java.util.Objects;

public abstract class AbstractBinaryOperator<T extends Number<T>> implements CommonExpression<T> {
    protected final CommonExpression<T> leftOperand;
    protected final CommonExpression<T> rightOperand;

    protected AbstractBinaryOperator(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    protected abstract String operatorToSting();

    protected abstract T calc(T a, T b);

    @Override
    public T evaluate(T x, T y, T z) {
        return calc(leftOperand.evaluate(x, y, z), rightOperand.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "(" + leftOperand.toString() + " " + operatorToSting() + " " + rightOperand.toString() + ")";
    }

    @Override
    public String toMiniString() {
        return toBrackets(leftOperand,
                leftOperand.priority() != -1 && leftOperand.priority() > this.priority())
                + " " + operatorToSting() + " "
                + toBrackets(rightOperand,
                rightOperand.priority() != -1 && (
                        rightOperand.priority() > this.priority() || (
                                (this.isOrdered() || rightOperand.isOrdered()) &&
                                        rightOperand.priority() == this.priority()
                        )
                )
        );
    }

    private String toBrackets(CommonExpression<T> operand, boolean brackets) {
        return (brackets ? "(" : "") + operand.toMiniString() + (brackets ? ")" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBinaryOperator<?> that = (AbstractBinaryOperator<?>) o;
        return Objects.equals(leftOperand, that.leftOperand) &&
                Objects.equals(rightOperand, that.rightOperand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOperand, rightOperand, operatorToSting());
    }
}