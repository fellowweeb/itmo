package expression.parser;

import expression.*;
import expression.exceptions.*;
import expression.number.Number;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;


/**
 * All whitespaces will be skipped
 * Parse tests grammar:
 * term = lowest priority link
 * shift = addSub (">>" | "<<") addSub | addSub
 * addSub = mulDiv ('+' | '-') mulDiv | mulDiv
 * mulDiv = argument ('*' | '/') argument | argument
 * argument = num | var | brackets | unaryOperators
 * unaryOperators = unaryMinus | reverse | digits | abs | square
 * reverse = "reverse" argument
 * digits = "digits" argument
 * unaryMinus = '-' argument
 * brackets = '(' term ')'
 * num = Integer
 * var = 'x' | 'y' | 'z'
 */
public class ExpressionParser<T extends Number<T>> extends StringParser implements Parser<T> {
    private final Grammar<T> grammar;
    private final Function<String, T> parseConst;

    public ExpressionParser(Grammar<T> grammar, Function<String, T> parseConst) {
        this.grammar = grammar;
        this.parseConst = parseConst;
    }

    public static <T extends Number<T>> ExpressionParser<T> getParser(Function<String, T> parseConst) {
        return new ExpressionParser<>(new Grammar<>(
                Set.of("x", "y", "z"),
                List.of(
                        Map.of("+", Add::new,
                                "-", Subtract::new),
                        Map.of("*", Multiply::new,
                                "/", Divide::new)
                ),
                Map.of(
                        "-", Negate::new,
                        "abs", Abs::new,
                        "square", Square::new
                )), parseConst);
    }

    public static <T extends Number<T>> ExpressionParser<T> getCheckedParser(Function<String, T> parseConst) {
        return new ExpressionParser<>(new Grammar<>(
                Set.of("x", "y", "z"),
                List.of(
                        Map.of("min", Min::new,
                                "max", Max::new),
                        Map.of("+", CheckedAdd::new,
                                "-", CheckedSubtract::new),
                        Map.of("*", CheckedMultiply::new,
                                "/", CheckedDivide::new)
                ),
                Map.of(
                        "-", CheckedNegate::new,
                        "abs", Abs::new,
                        "square", Square::new,
                        "count", Count::new
                )), parseConst);
    }

    @Override
    public CommonExpression<T> parse(String expression) {
        setString(expression);
        return parse();
    }

    private CommonExpression<T> parse() {
        CommonExpression<T> res = parseTerm();
        skip();
        if (ready()) {
            throw new ParsingUnexpectedException(currChar(), pos);
        }
        return res;
    }

    private CommonExpression<T> parseBinaryOperator(int priority) {
        skip();
        if (priority >= grammar.getBinaryOperatorsToConstructors().size()) {
            return parseArgument();
        }
        return accumulate(parseNextPriority(priority), priority);
    }

    private CommonExpression<T> accumulate(CommonExpression<T> accumulator, int priority) {
        skip();
        for (Map.Entry<String, BinaryOperator<CommonExpression<T>>> entry :
                grammar.getBinaryOperatorsToConstructors().get(priority).entrySet()) {
            String op = entry.getKey();
            if (hasNext(op)) {
                poll(op.length());
                accumulator = entry.getValue().apply(accumulator, parseNextPriority(priority));
                return accumulate(accumulator, priority);
            }
        }
        return accumulator;
    }

    private CommonExpression<T> parseNextPriority(int priority) {
        return parseBinaryOperator(priority + 1);
    }

    private CommonExpression<T> parseTerm() {
        return parseBinaryOperator(0);
    }

    private CommonExpression<T> parseArgument() {
        if (!ready()) {
            throw new ParsingArgumentException(pos);
        }
        if (hasNext("(")) {
            return parseBrackets();
        }
        if (Character.isDigit(currChar())) {
            return parseNumber();
        }


        // -             Integer.MIN_VALUE
        if (currChar() == '-') {
            int savedPos = pos;
            pollChar();
            skip();
            if (ready() && Character.isDigit(currChar())) {
                pos = savedPos;
                return parseNumber();
            }
            pos = savedPos;
        }
        for (String unaryOperator : grammar.getUnaryOperatorsToConstructor().keySet()) {
            if (hasNext(unaryOperator)) {
                return parseUnaryOperator(unaryOperator);
            }
        }
        try {
            return parseVariable();
        } catch (IllegalStateException ignored) {
            throw new ParsingArgumentException(pos);
        }
    }

    private CommonExpression<T> parseUnaryOperator(String operator) {
        expect(operator, (a, b) -> new ParsingException(b));
        skip();
        return grammar.getUnaryOperatorsToConstructor().get(operator).apply(parseArgument());
    }

    private CommonExpression<T> parseBrackets() {
        expect("(", ParsingException::new);
        CommonExpression<T> ret = parseTerm();
        expect(")", (a, b) -> new ClosingParenthesisException(b));
        return ret;
    }

    private CommonExpression<T> parseNumber() {
        int savedPos = pos;
        StringBuilder builder = new StringBuilder();
        if (currChar() == '-') {
            builder.append(pollChar());
        }
        skip();
        while (ready() && Character.isDigit(currChar())) {
            builder.append(pollChar());
        }
        if (builder.length() == 0) {
            throw new IllegalStateException("expected number at pos " + savedPos);
        }
        try {
            return new Const<>(parseConst.apply(builder.toString()));
        } catch (NumberFormatException e) {
            throw new ConstOverflowException(builder.toString(), savedPos);
        }
    }

    private CommonExpression<T> parseVariable() {
        for (String var : grammar.getVariables()) {
            if (hasNext(var)) {
                return new Variable<>(poll(var.length()));
            }
        }
        throw new IllegalStateException("expected variable at pos " + pos);
    }

    private void skip() {
        while (ready() && Character.isWhitespace(currChar())) {
            pollChar();
        }
    }
}
