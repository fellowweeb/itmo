package expression;

public class Square extends AbstactUnaryOperator {
    public Square(CommonExpression operand) {
        super(operand);
    }

    @Override
    protected String operatorToString() {
        return "square";
    }

    @Override
    protected int calc(int value) {
        return value * value;
    }
}
