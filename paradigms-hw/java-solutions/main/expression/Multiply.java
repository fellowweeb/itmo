package expression;

import expression.number.Number;

public class Multiply<T extends Number<T>> extends AbstractBinaryOperator<T> {
    public Multiply(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String operatorToSting() {
        return "*";
    }

    @Override
    protected T calc(T x, T y) {
        return x.mul(y);
    }

    @Override
    public int priority() {
        return 5;
    }

    @Override
    public boolean isOrdered() {
        return false;
    }
}
