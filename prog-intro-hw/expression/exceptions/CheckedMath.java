package expression.exceptions;

public class CheckedMath {
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

    public static int pow(int x, int y) {
        if (y < 0) {
            throw new PowException();
        }
        if (x == 0 && y == 0) {
            throw new PowException();
        }
        if (x == 1 || y == 0) {
            return 1;
        }
        int ret = 1;
        while (y > 0) {
            if (y % 2 == 1) {
                ret = CheckedMath.multiply(ret, x);
                y--;
            } else {
                x = CheckedMath.multiply(x, x);
                y >>= 1;
            }
        }
        return ret;
    }

    public static int log(int x, int y) {
        if (y <= 1) {
            throw new LogBaseException();
        }
        if (x <= 0) {
            throw new LogException();
        }
        int powed = 1;
        int count = 0;
        while (powed <= x) {
            count++;
            if (canMultiply(powed, y)) {
                powed = CheckedMath.multiply(powed, y);
            } else {
                break;
            }
        }
        return count - 1;
    }
}
