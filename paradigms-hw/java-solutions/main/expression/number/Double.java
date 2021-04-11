package expression.number;

public class Double implements Number<Double> {
    private final java.lang.Double value;

    public Double(java.lang.Double value) {
        this.value = value;
    }

    public static Double parse(String str) {
        return new Double(java.lang.Double.parseDouble(str));
    }

    @Override
    public Double add(Double other) {
        return new Double(value + other.value);
    }

    @Override
    public Double sub(Double other) {
        return new Double(value - other.value);
    }

    @Override
    public Double mul(Double other) {
        return new Double(value * other.value);
    }

    @Override
    public Double div(Double other) {
        return new Double(value / other.value);
    }

    @Override
    public int compareTo(Double other) {
        return value.compareTo(other.value);
    }

    @Override
    public Double getZero() {
        return new Double(0.);
    }

    @Override
    public Double negate() {
        return new Double(-value);
    }

    @Override
    public Double checkedNegate() {
        return negate();
    }

    @Override
    public java.lang.Double unbox() {
        return value;
    }

    @Override
    public Double bitsCount() {
        return new Double((double) Long.bitCount(java.lang.Double.doubleToLongBits(value)));
    }

    @Override
    public Double min(Double other) {
        return value < other.value ? this : other;
    }

    @Override
    public Double max(Double other) {
        return value > other.value ? this : other;
    }
}