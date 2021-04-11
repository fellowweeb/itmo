package expression;

public interface CommonExpression extends Expression, TripleExpression {
    int priority();

    boolean isOrdered();
}
