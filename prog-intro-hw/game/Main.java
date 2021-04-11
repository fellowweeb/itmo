package game;

public class Main {
    public static void main(String[] args) {
        new Match(true, new HumanPlayer(), new RandomPlayer(), new MNKBoard(3, 3, 3), 2).play();
    }
}
