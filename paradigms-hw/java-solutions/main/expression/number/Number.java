package expression.number;


public interface Number<T extends Number<T>> extends Comparable<T> {
    T add(T other);

    T sub(T other);

    T mul(T other);

    T div(T other);

    default T checkedAdd(T other) {
        return add(other);
    }

    default T checkedSub(T other) {
        return sub(other);
    }

    default T checkedMul(T other) {
        return mul(other);
    }

    default T checkedDiv(T other) {
        return div(other);
    }

    T getZero();

    T negate();

    T checkedNegate();

    java.lang.Number unbox();

    T bitsCount();

    T min(T other);

    T max(T other);
}
