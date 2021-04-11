package expression.exceptions;

public class LogBaseException extends IllegalArgumentException {
    public LogBaseException() {
    }

    @Override
    public String getMessage() {
        return "log base <= 0 || == 1";
    }
}
