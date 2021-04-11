package reverse;

import scanner.Scanner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReverseMin {
    private static final int MIN_ARRAY_SIZE = 8;

    public static void main(String[] args) {
        List<int[]> matrix = new ArrayList<>();
        Scanner scanStr = new Scanner(System.in, "utf8");
        int maxStrLen = 0;
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
            maxStrLen = Integer.max(maxStrLen, currLen);
            scanInt.close();
        }
        scanStr.close();
        int[] strMin = new int[matrix.size()];
        int[] colMin = new int[maxStrLen];
        Arrays.fill(strMin, Integer.MAX_VALUE);
        Arrays.fill(colMin, Integer.MAX_VALUE);
        for (int i = 0; i < strMin.length; i++) {
            for (int j = 0; j < matrix.get(i).length; j++) {
                strMin[i] = Integer.min(matrix.get(i)[j], strMin[i]);
                colMin[j] = Integer.min(matrix.get(i)[j], colMin[j]);
            }
        }
        for (int i = 0; i < strMin.length; i++) {
            for (int j = 0; j < matrix.get(i).length; j++) {
                System.out.print(Integer.min(strMin[i], colMin[j]) + " ");
            }
            System.out.println();
        }
    }
}
