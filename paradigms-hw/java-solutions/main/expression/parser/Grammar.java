package expression.parser;

import expression.CommonExpression;
import expression.number.Number;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public final class Grammar<T extends Number<T>> {
    private final Set<String> variables;
    private final List<Map<String, BinaryOperator<CommonExpression<T>>>> binaryOperatorsToConstructors;
    private final Map<String, UnaryOperator<CommonExpression<T>>> unaryOperatorsToConstructor;

    public Grammar(Set<String> variables, List<Map<String,
            BinaryOperator<CommonExpression<T>>>> binaryOperatorsToConstructors,
                   Map<String, UnaryOperator<CommonExpression<T>>> unaryOperatorsToConstructor) {
        this.variables = Collections.unmodifiableSet(variables);
        this.binaryOperatorsToConstructors = Collections.unmodifiableList(
                binaryOperatorsToConstructors);
        this.unaryOperatorsToConstructor = Collections.unmodifiableMap(unaryOperatorsToConstructor);
    }

    public Set<String> getVariables() {
        return variables;
    }


    public List<Map<String, BinaryOperator<CommonExpression<T>>>> getBinaryOperatorsToConstructors() {
        return binaryOperatorsToConstructors;
    }

    public Map<String, UnaryOperator<CommonExpression<T>>> getUnaryOperatorsToConstructor() {
        return unaryOperatorsToConstructor;
    }
}
