package expression.number;

import expression.exceptions.CheckedMath;

public class BigInteger implements Number<BigInteger> {
    private final java.math.BigInteger value;

    public BigInteger(java.math.BigInteger value) {
        this.value = value;
    }

    public static BigInteger parse(String str) {
        return new BigInteger(new java.math.BigInteger(str));
    }

    @Override
    public BigInteger add(BigInteger other) {
        return new BigInteger(value.add(other.value));
    }

    @Override
    public BigInteger sub(BigInteger other) {
        return new BigInteger(value.subtract(other.value));
    }

    @Override
    public BigInteger mul(BigInteger other) {
        return new BigInteger(value.multiply(other.value));
    }

    @Override
    public BigInteger div(BigInteger other) {
        return new BigInteger(value.divide(other.value));
    }

    @Override
    public BigInteger checkedDiv(BigInteger other) {
        return new CheckedMath<BigInteger>().divide(this, other);
    }

    @Override
    public int compareTo(BigInteger other) {
        return value.compareTo(other.value);
    }

    @Override
    public BigInteger getZero() {
        return new BigInteger(new java.math.BigInteger("0"));
    }

    @Override
    public BigInteger negate() {
        return new BigInteger(value.negate());
    }

    @Override
    public BigInteger checkedNegate() {
        return negate();
    }

    @Override
    public java.math.BigInteger unbox() {
        return value;
    }

    @Override
    public BigInteger bitsCount() {
        return new BigInteger(java.math.BigInteger.valueOf(value.bitCount()));
    }

    @Override
    public BigInteger min(BigInteger other) {
        return value.compareTo(other.value) < 0 ? this : other;
    }

    @Override
    public BigInteger max(BigInteger other) {
        return value.compareTo(other.value) > 0 ? this : other;
    }
}
