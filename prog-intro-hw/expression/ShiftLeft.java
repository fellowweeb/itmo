package expression;

public class ShiftLeft extends AbstractBinaryOperator {
    public ShiftLeft(CommonExpression leftOperand, CommonExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String operatorToSting() {
        return "<<";
    }

    @Override
    protected int calc(int a, int b) {
        return a << b;
    }

    @Override
    public int priority() {
        return 20;
    }

    @Override
    public boolean isOrdered() {
        return true;
    }
}
