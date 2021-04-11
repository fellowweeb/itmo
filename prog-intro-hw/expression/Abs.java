package expression;

public class Abs extends AbstactUnaryOperator {
    public Abs(CommonExpression operand) {
        super(operand);
    }

    @Override
    protected int calc(int value) {
        return Math.abs(value);
    }

    @Override
    protected String operatorToString() {
        return "abs";
    }
}
