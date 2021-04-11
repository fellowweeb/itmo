package expression;

public class Digits extends AbstactUnaryOperator {
    public Digits(CommonExpression operand) {
        super(operand);
    }

    private static int parseDigit(int ch) {
        return (ch >= '0' && ch <= '9') ? ch - '0' : 0;
    }

    @Override
    protected int calc(int value) {
        return String.valueOf(value).chars().map(Digits::parseDigit).sum();
    }

    @Override
    protected String operatorToString() {
        return "digits";
    }
}
