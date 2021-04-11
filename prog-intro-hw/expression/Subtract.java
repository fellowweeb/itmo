package expression;

public class Subtract extends AbstractBinaryOperator {
    public Subtract(CommonExpression leftOperand, CommonExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String operatorToSting() {
        return "-";
    }

    public static Subtract of(CommonExpression leftOperand, CommonExpression rightOperand) {
        return new Subtract(leftOperand, rightOperand);
    }

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public boolean isOrdered() {
        return true;
    }

    @Override
    protected int calc(int x, int y) {
        return x - y;
    }
}
