package expression.exceptions;

import expression.CommonExpression;
import expression.Negate;

public class CheckedNegate extends Negate {
    public CheckedNegate(CommonExpression value) {
        super(value);
    }

    @Override
    public int evaluate(int x) {
        int value = getOperand().evaluate(x);
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("integer overflow");
        }
        return -value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int value = getOperand().evaluate(x, y, z);
        if (value == Integer.MIN_VALUE) {
            throw new ArithmeticException("integer overflow");
        }
        return -value;
    }
}
