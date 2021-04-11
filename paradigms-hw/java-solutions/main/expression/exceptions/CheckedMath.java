package expression.exceptions;

public class CheckedMath<T extends expression.number.Number<T>> {
    public static int add(int x, int y) {
        if (y > 0 ? x > Integer.MAX_VALUE - y
                : x < Integer.MIN_VALUE - y) {
            throw new IntegerOverflowException();
        }
        return x + y;
    }

    public static int substract(int x, int y) {
        if (y > 0 ? x < Integer.MIN_VALUE + y
                : x > Integer.MAX_VALUE + y) {
            throw new IntegerOverflowException();
        }
        return x - y;
    }

    public static int multiply(int x, int y) {
        if (!canMultiply(x, y)) {
            throw new IntegerOverflowException();
        }
        return x * y;
    }

    private static boolean canMultiply(int x, int y) {
        return !(y > 0
                ? x > Integer.MAX_VALUE / y || x < Integer.MIN_VALUE / y
                : y < -1
                ? x > Integer.MIN_VALUE / y || x < Integer.MAX_VALUE / y
                : y == -1 && x == Integer.MIN_VALUE);
    }

    public static int divide(int x, int y) {
        if (y == 0) {
            throw new DivisionByZeroException();
        }
        if (y == -1 && x == Integer.MIN_VALUE) {
            throw new IntegerOverflowException();
        }
        return x / y;
    }

    public T divide(T x, T y) {
        if (y.compareTo(y.getZero()) == 0) {
            throw new DivisionByZeroException();
        }
        return x.div(y);
    }
}
