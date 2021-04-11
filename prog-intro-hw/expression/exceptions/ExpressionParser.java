package expression.exceptions;


import expression.CommonExpression;
import expression.parser.Grammar;
import expression.parser.ParsingException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpressionParser extends expression.parser.ExpressionParser implements Parser {

    public ExpressionParser() {
        super(new Grammar(
                Set.of("x", "y", "z"),
                List.of(
                        Map.of("+", CheckedAdd::new,
                                "-", CheckedSubtract::new),
                        Map.of("*", CheckedMultiply::new,
                                "/", CheckedDivide::new),
                        Map.of("**", Pow::new,
                                "//", Log::new)
                ),
                Map.of(
                        "-", CheckedNegate::new
                )
        ));
    }

    @Override
    public CommonExpression parse(String expression) throws ParsingException {
        return super.parse(expression);
    }
}