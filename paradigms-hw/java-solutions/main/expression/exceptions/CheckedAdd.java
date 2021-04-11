package expression.exceptions;

import expression.Add;
import expression.CommonExpression;
import expression.number.Number;

public class CheckedAdd<T extends Number<T>> extends Add<T> {
    public CheckedAdd(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected T calc(T x, T y) {
        return x.checkedAdd(y);
    }
}
