package expression.exceptions;

public class IntegerOverflowException extends ArithmeticException {
    public IntegerOverflowException() {
    }

    @Override
    public String getMessage() {
        return "integer overflow";
    }
}
