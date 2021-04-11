package reverse;

import scanner.Scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Reverse {
    private static final int MIN_ARRAY_SIZE = 8;

    public static void main(String[] args) {
        List<int[]> matrix = new ArrayList<>();
        Scanner scanStr = new Scanner(System.in, "utf8");
        while (scanStr.hasNextLine()) {
            matrix.add(new int[MIN_ARRAY_SIZE]);
            Scanner scanInt = new Scanner(scanStr.nextLine(), "utf8");
            int currLen = 0;
            int[] arr = matrix.get(matrix.size() - 1);
            while (scanInt.hasNextInt()) {
                if (currLen == arr.length) {
                    arr = Arrays.copyOf(arr, arr.length * 2);
                    matrix.set(matrix.size() - 1, arr);
                }
                arr[currLen++] = scanInt.nextInt();
            }
            arr = Arrays.copyOf(arr, currLen);
            matrix.set(matrix.size() - 1, arr);
            scanInt.close();
        }
        scanStr.close();
        for (int i = matrix.size() - 1; i >= 0; i--) {
            int[] currArr = matrix.get(i);
            for (int j = currArr.length - 1; j >= 0; j--) {
                System.out.print(currArr[j] + " ");
            }
            System.out.println();
        }
    }
}
