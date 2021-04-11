package expression;

public class Add extends AbstractBinaryOperator {
    public Add(CommonExpression leftOperand, CommonExpression rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public int evaluate(int a) {
        return leftOperand.evaluate(a) + rightOperand.evaluate(a);
    }

    public static Add of(CommonExpression leftOperand, CommonExpression rightOperand) {
        return new Add(leftOperand, rightOperand);
    }

    @Override
    protected String operatorToSting() {
        return "+";
    }

    @Override
    public int priority() {
        return 10;
    }

    @Override
    public boolean isOrdered() {
        return false;
    }

    @Override
    protected int calc(int x, int y) {
        return x + y;
    }
}
