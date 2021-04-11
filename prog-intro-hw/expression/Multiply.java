package expression;

public class Multiply extends AbstractBinaryOperator {
    public Multiply(CommonExpression leftOperand, CommonExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String operatorToSting() {
        return "*";
    }

    @Override
    protected int calc(int x, int y) {
        return x * y;
    }

    @Override
    public int priority() {
        return 5;
    }

    @Override
    public boolean isOrdered() {
        return false;
    }
}
