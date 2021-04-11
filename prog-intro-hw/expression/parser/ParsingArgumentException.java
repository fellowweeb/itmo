package expression.parser;

public class ParsingArgumentException extends ParsingException {
    public ParsingArgumentException(int pos) {
        super("expected argument", pos);
    }
}
