package expression.number;

import expression.exceptions.CheckedMath;
import expression.exceptions.IntegerOverflowException;

public class Int implements Number<Int> {
    private final Integer value;

    public Int(Integer value) {
        this.value = value;
    }

    public static Int parse(String str) {
        return new Int(Integer.parseInt(str));
    }

    @Override
    public Int add(Int other) {
        return new Int(value + other.value);
    }

    @Override
    public Int sub(Int other) {
        return new Int(value - other.value);
    }

    @Override
    public Int mul(Int other) {
        return new Int(value * other.value);
    }

    @Override
    public Int div(Int other) {
        return new Int(value / other.value);
    }

    @Override
    public Int checkedAdd(Int other) {
        return new Int(CheckedMath.add(value, other.value));
    }

    @Override
    public Int checkedSub(Int other) {
        return new Int(CheckedMath.substract(value, other.value));
    }

    @Override
    public Int checkedMul(Int other) {
        return new Int(CheckedMath.multiply(value, other.value));
    }

    @Override
    public Int checkedDiv(Int other) {
        return new Int(CheckedMath.divide(value, other.value));
    }

    @Override
    public int compareTo(Int other) {
        return value.compareTo(other.value);
    }

    @Override
    public Int getZero() {
        return new Int(0);
    }

    @Override
    public Int negate() {
        return new Int(-value);
    }

    @Override
    public Int checkedNegate() {
        if (value == Integer.MIN_VALUE)
            throw new IntegerOverflowException();
        return negate();
    }

    @Override
    public java.lang.Integer unbox() {
        return value;
    }

    @Override
    public Int bitsCount() {
        return new Int(Integer.bitCount(value));
    }

    @Override
    public Int min(Int other) {
        return value < other.value ? this : other;
    }

    @Override
    public Int max(Int other) {
        return value > other.value ? this : other;
    }
}
