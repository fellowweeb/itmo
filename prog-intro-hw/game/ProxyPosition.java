package game;

public class ProxyPosition implements Position {
    private final Position position;

    public ProxyPosition(final Position position) {
        this.position = position;
    }

    @Override
    public boolean isValid(Move move) {
        return position.isValid(move);
    }

    @Override
    public Cell getCell(int r, int c) {
        return position.getCell(r, c);
    }

    @Override
    public String toString() {
        return position.toString();
    }

    @Override
    public int getRowSize() {
        return position.getRowSize();
    }

    @Override
    public int getColumnSize() {
        return position.getColumnSize();
    }
}
