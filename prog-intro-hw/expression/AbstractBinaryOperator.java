package expression;

import java.util.Objects;

public abstract class AbstractBinaryOperator implements CommonExpression {
    protected final CommonExpression leftOperand;
    protected final CommonExpression rightOperand;

    protected AbstractBinaryOperator(CommonExpression leftOperand, CommonExpression rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    protected abstract String operatorToSting();

    protected abstract int calc(int a, int b);

    @Override
    public int evaluate(int x) {
        return calc(leftOperand.evaluate(x), rightOperand.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
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

    private String toBrackets(CommonExpression operand, boolean brackets) {
        return (brackets ? "(" : "") + operand.toMiniString() + (brackets ? ")" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBinaryOperator that = (AbstractBinaryOperator) o;
        return Objects.equals(leftOperand, that.leftOperand) &&
                Objects.equals(rightOperand, that.rightOperand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftOperand, rightOperand, operatorToSting());
    }
}