package game;

import java.util.Arrays;
import java.util.Map;

public class MNKBoard implements Board, Position {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    private final int m;
    private final int n;
    private final int k;
    private final String weightTemplate;
    private Cell turn;
    private int left;

    public MNKBoard(int m, int n, int k) {
        if (m < 1 || n < 1 || k < 1) {
            throw new IllegalArgumentException("m | n | k < 1");
        }
        this.m = m;
        this.n = n;
        this.k = k;
        left = m * n;
        weightTemplate = "%" + (Math.max(weight(n), weight(m)) + 1) + "s";
        this.cells = new Cell[m][n];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
        turn = Cell.X;
    }

    private int weight(int n) {
        int cnt = 0;
        while (n > 0) {
            cnt++;
            n /= 10;
        }
        return cnt;
    }

    @Override
    public Position getPosition() {
        return new ProxyPosition(this);
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }

        cells[move.getRow()][move.getColumn()] = move.getValue();
        left--;

        int inDiag1 = count(move, 1, 1);
        int inDiag2 = count(move, 2, 2);
        int inRow = count(move, 1, 0);
        int inCol = count(move, 0, 1);


        if (inRow == k || inCol == k || inDiag1 == k || inDiag2 == k)
            return Result.WIN;

        if (left == 0)
            return Result.DRAW;

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    private int count(final Move move, int dx, int dy) {
        int cnt = 0;
        for (int i = move.getRow(), j = move.getColumn();
             i < m && i >= 0 && j < n && j >= 0 && cells[i][j] == move.getValue(); i += dx, j += dy)
            cnt++;
        dx *= -1;
        dy *= -1;
        for (int i = move.getRow() + dx, j = move.getColumn() + dy;
             i < m && i >= 0 && j < n && j >= 0 && cells[i][j] == move.getValue(); i += dx, j += dy)
            cnt++;
        return cnt;
    }

    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < m
                && 0 <= move.getColumn() && move.getColumn() < n
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == getCell();
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public int getRowSize() {
        return m;
    }

    @Override
    public int getColumnSize() {
        return n;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(String.format(weightTemplate, ""));
        for (int i = 0; i < n; i++) {
            sb.append(String.format(weightTemplate, i));
        }
        for (int r = 0; r < m; r++) {
            sb.append("\n");
            sb.append(String.format(weightTemplate, r));
            for (int c = 0; c < n; c++) {
                sb.append(String.format(weightTemplate, SYMBOLS.get(cells[r][c])));
            }
        }
        return sb.toString();
    }

    @Override
    public Board cleared() {
        return new MNKBoard(m, n, k);
    }
}