package expression.exceptions;

import expression.CommonExpression;
import expression.Negate;
import expression.number.Number;

public class CheckedNegate<T extends Number<T>> extends Negate<T> {
    public CheckedNegate(CommonExpression<T> value) {
        super(value);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return getOperand().evaluate(x, y, z).checkedNegate();
    }
}
