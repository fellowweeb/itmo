package expression.generic;

import expression.TripleExpression;
import expression.number.BigInteger;
import expression.number.Double;
import expression.number.Int;
import expression.number.Number;

import java.util.function.Function;

import static expression.parser.ExpressionParser.getCheckedParser;

public class GenericTabulator implements Tabulator {
    private static <T extends Number<T>> TripleExpression<T> parse(String expression, Function<String, T> parseConst) {
        return getCheckedParser(parseConst).parse(expression);
    }

    @Override
    public Object[][][] tabulate(String mode, String expression,
                                 int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {

        switch (mode) {
            case "i":
                return solve(expression, x1, x2, y1, y2, z1, z2,
                        a -> new Int(a), Int::parse);
            case "d":
                return solve(expression, x1, x2, y1, y2, z1, z2,
                        a -> new Double(java.lang.Double.valueOf(a)), Double::parse);
            case "bi":
                return solve(expression, x1, x2, y1, y2, z1, z2,
                        a -> new BigInteger(java.math.BigInteger.valueOf(a)), BigInteger::parse);
        }
        throw new IllegalArgumentException("bad mode");
    }

    private <T extends Number<T>> Object[][][] solve(String expression,
                                                     int x1, int x2, int y1, int y2, int z1, int z2,
                                                     Function<Integer, T> constructor, Function<String, T> parseConst)
            throws Exception {
        TripleExpression<T> expr = parse(expression, parseConst);
        Object[][][] ret = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        for (int i = 0; x1 + i <= x2; i++) {
            for (int j = 0; j + y1 <= y2; j++) {
                for (int k = 0; k + z1 <= z2; k++) {
                    try {
                        ret[i][j][k] = expr.evaluate(
                                constructor.apply(x1 + i),
                                constructor.apply(y1 + j),
                                constructor.apply(z1 + k)
                        ).unbox();
                    } catch (ArithmeticException e) {
                        ret[i][j][k] = null;
                    }
                }
            }
        }
        return ret;
    }
}
