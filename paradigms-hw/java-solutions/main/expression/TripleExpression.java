package expression;

import expression.number.Number;

public interface TripleExpression<T extends Number<T>> extends ToMiniString {
    T evaluate(T x, T y, T z);
}
