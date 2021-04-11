package expression.parser;

public class ClosingParenthesisException extends ParsingException {
    public ClosingParenthesisException(int pos) {
        super("expected ')'", pos);
    }
}
