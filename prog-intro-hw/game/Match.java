package game;

public class Match {
    private final Player player1;
    private final Player player2;
    private final Board board;
    private final int[] playersWinCnt = {0, 0, 0};
    private final int winCnt;
    private final boolean log;
    private int turn = 1;

    public Match(final boolean log, final Player player1, final Player player2, final Board board, final int winCnt) {
        if (winCnt < 1) {
            throw new IllegalArgumentException("Win count < 1");
        }
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
        this.winCnt = winCnt;
        this.log = log;
    }

    public void play() {
        while (playersWinCnt[1] < winCnt && playersWinCnt[2] < winCnt) {
            System.out.println("Starting new game. Player " + turn + " will be first");
            int result;
            switch (turn) {
                case 1:
                    result = new Game(log, player1, player2).play(board.cleared());
                    break;
                case 2:
                    result = new Game(log, player2, player1).play(board.cleared());
                    break;
                default:
                    result = 0;
            }
            if (result != 0) {
                System.out.println("In this game player " + (turn == 1 ? result : 3 - result) + " won!");
                playersWinCnt[result]++;
                turn = 3 - turn;
            } else {
                System.out.println("Draw");
            }
        }
    }

}
