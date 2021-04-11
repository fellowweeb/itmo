package expression.exceptions;

import expression.CommonExpression;
import expression.Divide;
import expression.number.Number;

public class CheckedDivide<T extends Number<T>> extends Divide<T> {
    public CheckedDivide(CommonExpression<T> leftOperand, CommonExpression<T> rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected T calc(T x, T y) {
        return x.checkedDiv(y);
    }
}
