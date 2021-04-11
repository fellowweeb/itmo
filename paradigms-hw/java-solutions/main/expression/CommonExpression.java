package expression;

import expression.number.Number;

public interface CommonExpression<T extends Number<T>> extends TripleExpression<T> {
    int priority();

    boolean isOrdered();
}
