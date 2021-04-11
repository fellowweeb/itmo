package expression.exceptions;

import expression.CommonExpression;
import expression.Multiply;
import expression.number.Number;

public class CheckedMultiply<T extends Number<T>> extends Multiply<T> {
    public CheckedMultiply(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected T calc(T x, T y) {
        return x.checkedMul(y);
    }
}
