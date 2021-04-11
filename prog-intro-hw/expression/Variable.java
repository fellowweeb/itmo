package expression;

import java.util.Objects;

public class Variable implements CommonExpression {
    private final String var;

    public Variable(final String var) {
        this.var = var;
    }

    @Override
    public String toString() {
        return var;
    }


    @Override
    public int evaluate(int a) {
        return a;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (var) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
            default:
                throw new IllegalStateException("var = " + var);
        }
    }


    @Override
    public int priority() {
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(var, variable.var);
    }

    @Override
    public int hashCode() {
        return var.hashCode();
    }

    @Override
    public boolean isOrdered() {
        return false;
    }
}
