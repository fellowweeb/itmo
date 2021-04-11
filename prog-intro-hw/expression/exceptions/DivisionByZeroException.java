package expression.exceptions;

public class DivisionByZeroException extends ArithmeticException {
    public DivisionByZeroException() {
    }

    @Override
    public String getMessage() {
        return "division by zero";
    }
}
