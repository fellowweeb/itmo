package expression;

public class Negate extends AbstactUnaryOperator {
    public Negate(CommonExpression value) {
        super(value);
    }

    @Override
    protected String operatorToString() {
        return "-";
    }

    @Override
    protected int calc(int value) {
        return -value;
    }
}
