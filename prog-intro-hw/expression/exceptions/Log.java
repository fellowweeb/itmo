package expression.exceptions;

import expression.AbstractBinaryOperator;
import expression.CommonExpression;

public class Log extends AbstractBinaryOperator {
    public Log(CommonExpression leftOperand, CommonExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String operatorToSting() {
        return "//";
    }

    @Override
    protected int calc(int a, int b) {
        return CheckedMath.log(a, b);
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public boolean isOrdered() {
        return true;
    }
}
