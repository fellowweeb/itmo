package expression;

public class Reverse extends AbstactUnaryOperator {
    public Reverse(CommonExpression operand) {
        super(operand);
    }

    @Override
    protected int calc(int value) {
        String valStr = String.valueOf(value);
        if (value < 0) {
            valStr = valStr.substring(1);
        }
        StringBuilder builder = new StringBuilder(valStr);
        if (value < 0) {
            builder.append('-');
        }
        return (int) Long.parseLong(builder.reverse().toString());
    }

    @Override
    protected String operatorToString() {
        return "reverse";
    }
}
