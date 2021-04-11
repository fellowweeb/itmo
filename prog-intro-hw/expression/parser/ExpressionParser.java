package expression.parser;

import expression.CommonExpression;
import expression.Const;
import expression.Variable;

import java.util.Map;
import java.util.function.BinaryOperator;


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
public class ExpressionParser extends StringParser implements Parser {
    private final Grammar grammar;

    public ExpressionParser() {
        grammar = Grammar.GRAMMAR_FOR_PARSE_TESTS;
    }

    public ExpressionParser(Grammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public CommonExpression parse(String expression) {
        setString(expression);
        return parse();
    }

    private CommonExpression parse() {
        CommonExpression res = parseTerm();
        skip();
        if (ready()) {
            throw new ParsingUnexpectedException(currChar(), pos);
        }
        return res;
    }

    private CommonExpression parseBinaryOperator(int priority) {
        skip();
        if (priority >= grammar.getBinaryOperatorsToConstructors().size()) {
            return parseArgument();
        }
        return accumulate(parseNextPriority(priority), priority);
    }

    private CommonExpression accumulate(CommonExpression accumulator, int priority) {
        skip();
        for (Map.Entry<String, BinaryOperator<CommonExpression>> entry :
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

    private CommonExpression parseNextPriority(int priority) {
        return parseBinaryOperator(priority + 1);
    }

    private CommonExpression parseTerm() {
        return parseBinaryOperator(0);
    }

    private CommonExpression parseArgument() {
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

    private CommonExpression parseUnaryOperator(String operator) {
        expect(operator, (a, b) -> new ParsingException(b));
        skip();
        return grammar.getUnaryOperatorsToConstructor().get(operator).apply(parseArgument());
    }

    private CommonExpression parseBrackets() {
        expect("(", ParsingException::new);
        CommonExpression ret = parseTerm();
        expect(")", (a, b) -> new ClosingParenthesisException(b));
        return ret;
    }

    private CommonExpression parseNumber() {
        int savedPos = pos;
        StringBuilder builder = new StringBuilder();
        if (currChar() == '-') {
            builder.append(pollChar());
        }
        skip();
        while (ready() && Character.isDigit(currChar()) && builder.length() < 100) {
            builder.append(pollChar());
        }
        if (builder.length() == 0) {
            throw new IllegalStateException("expected number at pos " + savedPos);
        }
        try {
            return new Const(Integer.parseInt(builder.toString()));
        } catch (NumberFormatException e) {
            throw new ConstOverflowException(builder.toString(), savedPos);
        }
    }

    private CommonExpression parseVariable() {
        for (String var : grammar.getVariables()) {
            if (hasNext(var)) {
                return new Variable(poll(var.length()));
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
