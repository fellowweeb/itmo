package expression.exceptions;

public class PowException extends IllegalArgumentException {
    public PowException() {
    }

    @Override
    public String getMessage() {
        return "indeterminate expression";
    }
}
