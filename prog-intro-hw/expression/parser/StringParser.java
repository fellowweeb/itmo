package expression.parser;

import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public abstract class StringParser {
    protected String string;
    protected int pos;

    protected void setString(String str) {
        this.string = str;
        pos = 0;
    }

    protected char currChar() {
        if (pos >= string.length()) {
            throw new NoSuchElementException();
        }
        return string.charAt(pos);
    }

    protected char pollChar() {
        char ret = currChar();
        pos++;
        return ret;
    }


    protected <T extends ParsingException> void expect(String expect, BiFunction<String, Integer, T> constructor)
            throws ParsingException {
        int savedPos = pos;
        for (char c : expect.toCharArray()) {
            if (!(pos < string.length() && string.charAt(pos) == c)) {
                pos = savedPos;
                throw constructor.apply("expected: '" + expect + "'", pos);
            }
            pos++;
        }
    }


    protected boolean hasNext(String expect) {
        int savedPos = pos;
        for (char c : expect.toCharArray()) {
            if (!(pos < string.length() && string.charAt(pos) == c)) {
                pos = savedPos;
                return false;
            }
            pos++;
        }
        pos = savedPos;
        return true;
    }

    protected String curr(int size) {
        int savedPos = pos;
        try {
            return poll(size);
        } finally {
            pos = savedPos;
        }
    }

    protected String poll(int size) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            builder.append(pollChar());
        }
        return builder.toString();
    }

    protected boolean ready() {
        return pos < string.length();
    }
}
