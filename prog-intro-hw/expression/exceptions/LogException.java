package expression.exceptions;

public class LogException extends IllegalArgumentException {
    public LogException() {
    }

    @Override
    public String getMessage() {
        return "log by <= 0";
    }
}
