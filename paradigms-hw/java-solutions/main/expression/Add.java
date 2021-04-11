package expression;

import expression.number.Number;

public class Add<T extends Number<T>> extends AbstractBinaryOperator<T> {
    public Add(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String operatorToSting() {
        return "+";
    }

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public boolean isOrdered() {
        return false;
    }

    @Override
    protected T calc(T x, T y) {
        return x.add(y);
    }
}
