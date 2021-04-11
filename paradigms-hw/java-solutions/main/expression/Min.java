package expression;

import expression.number.Number;

public class Min<T extends Number<T>> extends AbstractBinaryOperator<T> {
    public Min(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String operatorToSting() {
        return "min";
    }

    @Override
    protected T calc(T a, T b) {
        return a.min(b);
    }

    @Override
    public int priority() {
        return 20;
    }

    @Override
    public boolean isOrdered() {
        return false;
    }
}
