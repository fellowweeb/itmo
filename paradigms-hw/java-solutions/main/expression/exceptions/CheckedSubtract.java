package expression.exceptions;

import expression.CommonExpression;
import expression.Subtract;
import expression.number.Number;

public class CheckedSubtract<T extends Number<T>> extends Subtract<T> {
    public CheckedSubtract(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected T calc(T x, T y) {
        return x.checkedSub(y);
    }
}
