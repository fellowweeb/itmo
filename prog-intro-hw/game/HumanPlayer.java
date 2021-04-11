package game;

import java.io.PrintStream;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final PrintStream out;
    private final Scanner in;

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in));
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            out.println("Position");
            out.println(position);
            out.println(cell + "'s move");
            out.println("Enter row and column");
            int[] point = read();
            final Move move = new Move(point[0], point[1], cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Move " + move + " is invalid");
        }
    }

    private int[] read() {
        int[] res = new int[2];
        while (true) {
            String str = in.nextLine();
            String[] splitted = str.split("\\p{javaWhitespace}");
            try {
                res[0] = Integer.parseInt(splitted[0]);
                res[1] = Integer.parseInt(splitted[1]);
                if (splitted.length != 2)
                    throw new IndexOutOfBoundsException();
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                out.print("Invalid input, you must enter 2 numbers, try again\n");
                continue;
            }
            return res;
        }
    }
}
