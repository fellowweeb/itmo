package expression;

public class Divide extends AbstractBinaryOperator {
    public Divide(CommonExpression leftOperand, CommonExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String operatorToSting() {
        return "/";
    }

    @Override
    protected int calc(int a, int b) {
        return a / b;
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
