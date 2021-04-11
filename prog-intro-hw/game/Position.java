package game;

public interface Position {
    boolean isValid(Move move);

    int getRowSize();

    int getColumnSize();

    Cell getCell(int r, int c);
}
