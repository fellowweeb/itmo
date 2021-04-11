package expression.parser;

public class ParsingException extends RuntimeException {
    private final int pos;

    public ParsingException(int pos) {
        this.pos = pos;
    }

    public ParsingException(String message, int pos) {
        super(message);
        this.pos = pos;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " at pos " + pos;
    }

    public int getPos() {
        return pos;
    }
}
