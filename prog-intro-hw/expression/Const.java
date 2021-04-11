package expression;

public class Const implements CommonExpression {
    private final int value;

    public Const(final int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int evaluate(int a) {
        return value;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Const aConst = (Const) o;
        return value == aConst.value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public int priority() {
        return -1;
    }

    @Override
    public boolean isOrdered() {
        return false;
    }
}
