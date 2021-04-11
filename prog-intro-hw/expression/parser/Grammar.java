package expression.parser;

import expression.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public final class Grammar {
    public static final Grammar GRAMMAR_FOR_PARSE_TESTS = new Grammar(
            Set.of("x", "y", "z"),
            List.of(
                    Map.of("<<", ShiftLeft::new,
                            ">>", ShiftRight::new),
                    Map.of("+", Add::new,
                            "-", Subtract::new),
                    Map.of("*", Multiply::new,
                            "/", Divide::new)
            ),
            Map.of(
                    "-", Negate::new,
                    "abs", Abs::new,
                    "square", Square::new,
                    "digits", Digits::new,
                    "reverse", Reverse::new
            )
    );
    private final Set<String> variables;
    private final List<Map<String, BinaryOperator<CommonExpression>>> binaryOperatorsToConstructors;
    private final Map<String, UnaryOperator<CommonExpression>> unaryOperatorsToConstructor;

    public Grammar(Set<String> variables, List<Map<String,
            BinaryOperator<CommonExpression>>> binaryOperatorsToConstructors,
                   Map<String, UnaryOperator<CommonExpression>> unaryOperatorsToConstructor) {
        this.variables = Collections.unmodifiableSet(variables);
        this.binaryOperatorsToConstructors = Collections.unmodifiableList(
                binaryOperatorsToConstructors);
        this.unaryOperatorsToConstructor = Collections.unmodifiableMap(unaryOperatorsToConstructor);
    }

    public Set<String> getVariables() {
        return variables;
    }


    public List<Map<String, BinaryOperator<CommonExpression>>> getBinaryOperatorsToConstructors() {
        return binaryOperatorsToConstructors;
    }

    public Map<String, UnaryOperator<CommonExpression>> getUnaryOperatorsToConstructor() {
        return unaryOperatorsToConstructor;
    }
}
