package expression.exceptions;

public class IntegerOverflowException extends ArithmeticException {
    public IntegerOverflowException() {
        super("integer overflow");
    }
}
