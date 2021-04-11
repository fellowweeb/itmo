package expression;

import expression.number.Number;

public class Const<T extends Number<T>> implements CommonExpression<T> {
    private final T value;

    public Const(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Const<?> aConst = (Const<?>) o;
        return value == aConst.value;
    }

    @Override
    public int hashCode() {
        return value.toString().hashCode();
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
