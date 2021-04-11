package expression.parser;

public class ConstOverflowException extends ParsingException {
    private final String num;

    public ConstOverflowException(String num, int pos) {
        super(num + " more than int", pos);
        this.num = num;
    }

    public String getConst() {
        return num;
    }
}
