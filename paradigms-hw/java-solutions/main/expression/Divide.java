package expression;

import expression.number.Number;

public class Divide<T extends Number<T>> extends AbstractBinaryOperator<T> {
    public Divide(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String operatorToSting() {
        return "/";
    }

    @Override
    protected T calc(T a, T b) {
        return a.div(b);
    }

    @Override
    public int priority() {
        return 5;
    }

    @Override
    public boolean isOrdered() {
        return true;
    }
}
