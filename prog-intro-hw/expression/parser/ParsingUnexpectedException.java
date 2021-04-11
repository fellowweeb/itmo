package expression.parser;

public class ParsingUnexpectedException extends ParsingException {
    public ParsingUnexpectedException(char unexpected, int pos) {
        super("unexpected '" + unexpected + "'", pos);
    }
}
