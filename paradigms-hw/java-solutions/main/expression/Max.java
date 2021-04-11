package expression;

import expression.number.Number;

public class Max<T extends Number<T>> extends AbstractBinaryOperator<T> {
    public Max(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String operatorToSting() {
        return "max";
    }

    @Override
    protected T calc(T a, T b) {
        return a.max(b);
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
